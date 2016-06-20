package ddw.com.richang.update;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.util.NetWorkUtils;


/**
 * 应用升级
 *
 * @author zzp
 */
public class UpdateHelperHttpUrlConnection {

    private static final int UPDATE_NOTIFICATION_PROGRESS = 0x1;
    private static final int COMPLETE_DOWNLOAD_APK = 0x2;
    private static final int DOWNLOAD_NOTIFICATION_ID = 0x3;
    private static final String PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String SUFFIX = ".apk";
    private static final String APK_PATH = "APK_PATH";
    private static final String APP_NAME = "APP_NAME";
    private Context mContext;
    private String checkUrl;
    private boolean isAutoInstall;
    private boolean isHintVersion;
    private OnUpdateListener updateListener;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder ntfBuilder;
    private SharedPreferences preferences_update;

    private HashMap<String, String> cache = new HashMap<String, String>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_NOTIFICATION_PROGRESS:
                    showDownloadNotificationUI(msg.arg1);
                    break;
                case COMPLETE_DOWNLOAD_APK:
                    if (UpdateHelperHttpUrlConnection.this.isAutoInstall) {
                        installApk(Uri.parse("file://" + cache.get(APK_PATH)));
                    } else {
                        if (ntfBuilder == null) {
                            ntfBuilder = new NotificationCompat.Builder(mContext);
                        }
                        ntfBuilder.setSmallIcon(mContext.getApplicationInfo().icon)
                                .setContentTitle(cache.get(APP_NAME))
                                .setContentText("下载完成，点击安装").setTicker("任务下载完成");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + cache.get(APK_PATH)),
                                "application/vnd.android.package-archive");
                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                                intent, 0);
                        ntfBuilder.setContentIntent(pendingIntent);
                        if (notificationManager == null) {
                            notificationManager = (NotificationManager) mContext
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                        }
                        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, ntfBuilder.build());
                    }
                    break;
            }
        }
    };

    private UpdateHelperHttpUrlConnection(Builder builder) {
        this.mContext = builder.context;
        this.checkUrl = builder.checkUrl;
        this.isAutoInstall = builder.isAutoInstall;
        this.isHintVersion = builder.isHintNewVersion;
        preferences_update = mContext.getSharedPreferences("Updater", Context.MODE_PRIVATE);
    }

    /**
     * 检查app是否有新版本，check之前先Builer所需参数
     */
    public void check() {
        check(null);
    }

    public void check(OnUpdateListener listener) {
        if (listener != null) {
            this.updateListener = listener;
        }
        if (mContext == null) {
            Log.e("NullPointerException", "The context must not be null.");
            return;
        }

        SharedPreferences.Editor editor = preferences_update.edit();

        showUpdateUI();
        editor.putBoolean("hasNewVersion", true);
        editor.putString("lastestVersionCode", SharePreferenceManager.getInstance().getString
                (ConstantData.APK_VERSION_CODE, ""));
        editor.putString("lastestVersionName", "日常");

        editor.putString("currentVersionCode", getPackageInfo().versionCode + "");
        editor.putString("currentVersionName", getPackageInfo().versionName);
        editor.commit();
        if (UpdateHelperHttpUrlConnection.this.updateListener != null) {
            UpdateHelperHttpUrlConnection.this.updateListener.onFinishCheck();
        }
    }

    /**
     * 2014-10-27新增流量提示框，当网络为数据流量方式时，下载就会弹出此对话框提示
     */
    private void showNetDialog() {
        AlertDialog.Builder netBuilder = new AlertDialog.Builder(mContext);
        netBuilder.setTitle("下载提示");
        netBuilder.setMessage("您在目前的网络环境下继续下载将可能会消耗手机流量，请确认是否继续下载？");
        netBuilder.setNegativeButton("取消下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        netBuilder.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
                asyncDownLoad.execute();
            }
        });
        AlertDialog netDialog = netBuilder.create();
        netDialog.setCanceledOnTouchOutside(false);
        netDialog.show();
    }

    /**
     * 弹出提示更新窗口
     */
    private void showUpdateUI() {
        AlertDialog.Builder upDialogBuilder = new AlertDialog.Builder(mContext);
        upDialogBuilder.setTitle("日常更新");
        upDialogBuilder.setMessage(SharePreferenceManager.getInstance().getString(ConstantData
                .APK_UPDATE_CONTENT, ""));
        upDialogBuilder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        upDialogBuilder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                NetWorkUtils netWorkUtils = new NetWorkUtils(mContext);
                int type = netWorkUtils.getNetType();
                if (type != 1) {
                    showNetDialog();
                } else {
                    AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
                    asyncDownLoad.execute();
                }
            }
        });
        AlertDialog updateDialog = upDialogBuilder.create();
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
    }

    /**
     * 通知栏弹出下载提示进度
     *
     * @param progress
     */
    private void showDownloadNotificationUI(final int progress) {
        if (mContext != null) {
            String contentText = new StringBuffer().append(progress).append("%").toString();
            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(),
                    PendingIntent.FLAG_CANCEL_CURRENT);
            if (notificationManager == null) {
                notificationManager = (NotificationManager) mContext.getSystemService(Context
                        .NOTIFICATION_SERVICE);
            }
            if (ntfBuilder == null) {
                ntfBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(mContext
                        .getApplicationInfo().icon)
                        .setTicker("开始下载...").setContentTitle("日常").setContentIntent(contentIntent);
            }
            ntfBuilder.setContentText(contentText);
            ntfBuilder.setProgress(100, progress, false);
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, ntfBuilder.build());
        }
    }

    /**
     * 获取当前app版本
     *
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    private PackageInfo getPackageInfo() {
        PackageInfo pinfo = null;
        if (mContext != null) {
            try {
                pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pinfo;
    }

    private void installApk(Uri data) {
        if (mContext != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(data, "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
            if (notificationManager != null) {
                notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
            }
        } else {
            Log.e("NullPointerException", "The context must not be null.");
        }

    }

    public static class Builder {
        private Context context;
        private String checkUrl;
        private boolean isAutoInstall = true;
        private boolean isHintNewVersion = true;

        public Builder(Context ctx) {
            this.context = ctx;
        }

        /**
         * 检查是否有新版本App的URL接口路径
         *
         * @param checkUrl
         * @return
         */
        public Builder checkUrl(String checkUrl) {
            this.checkUrl = checkUrl;
            return this;
        }

        /**
         * 是否需要自动安装, 不设置默认自动安装
         *
         * @param isAuto true下载完成后自动安装，false下载完成后需在通知栏手动点击安装
         * @return
         */
        public Builder isAutoInstall(boolean isAuto) {
            this.isAutoInstall = isAuto;
            return this;
        }

        /**
         * 当没有新版本时，是否Toast提示
         *
         * @param isHint
         * @return true提示，false不提示
         */
        public Builder isHintNewVersion(boolean isHint) {
            this.isHintNewVersion = isHint;
            return this;
        }

        /**
         * 构造UpdateManager对象
         *
         * @return
         */
        public UpdateHelperHttpUrlConnection build() {
            return new UpdateHelperHttpUrlConnection(this);
        }
    }

    /**
     * 异步下载app任务
     */
    private class AsyncDownLoad extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL(SharePreferenceManager.getInstance().getString(ConstantData
                        .APK_DOWNLOAD_URL, ""));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int total = connection.getContentLength();
                InputStream inputStream = connection.getInputStream();
                String apkName = "RiChang" + SharePreferenceManager.getInstance().getString
                        (ConstantData.APK_VERSION_CODE, "") + SUFFIX;
                cache.put(APP_NAME, "RiChang");
                cache.put(APK_PATH, PATH + File.separator + "RiChang" + File.separator + apkName);
                File savePath = new File(PATH + File.separator + "RiChang");
                if (!savePath.exists())
                    savePath.mkdirs();
                File apkFile = new File(savePath, apkName);
                if (apkFile.exists() && apkFile.length() == (long) connection.getContentLength()) {
                    return true;
                }
                FileOutputStream fos = new FileOutputStream(apkFile);
                byte[] buf = new byte[1024];
                int count = 0;
                int length = -1;
                while ((length = inputStream.read(buf)) != -1) {
                    fos.write(buf, 0, length);
                    count += length;
                    int progress = (int) ((count / (float) total) * 100);
                    if (progress % 5 == 0) {
                        handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS, progress, -1)
                                .sendToTarget();
                    }
                    if (UpdateHelperHttpUrlConnection.this.updateListener != null) {
                        UpdateHelperHttpUrlConnection.this.updateListener.onDownloading(progress);
                    }
                }
                inputStream.close();
                fos.close();

            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            if (flag) {
                handler.obtainMessage(COMPLETE_DOWNLOAD_APK).sendToTarget();
                if (UpdateHelperHttpUrlConnection.this.updateListener != null) {
                    UpdateHelperHttpUrlConnection.this.updateListener.onFinshDownload();
                }
            } else {
                Log.e("Error", "下载失败。");
            }
        }
    }

}