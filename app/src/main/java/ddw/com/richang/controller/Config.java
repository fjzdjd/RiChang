package ddw.com.richang.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.model.USR;
import ddw.com.richang.model.common.IdNameMap;

/**
 * Created by dingdewen on 15/11/23.
 * 配置文件
 */
public class Config {
    //版本号
    public static String VERSION = "1.01";
    //页面尺寸
    public static int SCALE = 2;
    //短信验证码有效时间
    public static int CHECKTIME = 60;
    //接口域名
    public static String DOMAIN = "";
    //每次传递数
    public static int ACTSumOnce = 10;
    //城市：
    public static ArrayList<IdNameMap> CITYS = new ArrayList<IdNameMap>();//从网络更新
    //个性标签：
    public static ArrayList<IdNameMap> TAGS = new ArrayList<IdNameMap>();//从网络更新
    //专栏：
    public static ArrayList<IdNameMap> COLUMNS = new ArrayList<IdNameMap>();//从网络更新
    //提醒：
    public static boolean HINT;
    public static boolean SHAKE;
    //页面返回码
    public static int REQ = 0;

    //接口详情(因为要先加载域名，所以不能把接口作为静态类)
    public static InterFace getInterface() {
        return InterFace.getInstance();
    }

    //以下需要网络更新的东西都放在USR的getInfo()里，因为那是必须要做的事情。
    //用户信息
    public static USR getUSR() {
        return USR.getInstance();
    }

    public static USR getUSR(Context context) {
        return USR.getInstance(context);
    }

    //新版本
    public static void cheVersion(final Activity activity, final boolean silence) {
        //getVersion
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String versionJson = WebHTTP.getStr(Config.getInterface().getVersion);
                if (activity != null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject(versionJson);
                                if (200 == obj.getInt("code")) {
                                    JSONObject ver = obj.getJSONObject("data");
                                    final String version = ver.getString("app_version");
                                    final String url = ver.getString("app_url");
                                    String msg = ver.getString("app_msg");
                                    Log.e("outcv", version + "" + url);
                                    if (!version.equals(Config.VERSION)) {
                                        new AlertDialog.Builder(activity)
                                                .setTitle("检测到新版本" + version)
                                                .setMessage(msg)
                                                .setPositiveButton("升级", new DialogInterface
                                                        .OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        Toast.makeText(activity
                                                                        .getApplicationContext(),
                                                                "正在下载",
                                                                Toast.LENGTH_SHORT).show();
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    WebHTTP w = new WebHTTP(url);
                                                                    final String uri =
                                                                            Environment
                                                                                    .getExternalStorageDirectory().getAbsolutePath().
                                                                                    toString() +
                                                                                    "/Richang/cache/" + version + ".apk";
                                                                    w.save(uri);
                                                                    w.close();
                                                                    //安装
                                                                    Intent intent = new Intent
                                                                            (Intent.ACTION_VIEW);
                                                                    intent.setDataAndType(Uri
                                                                                    .fromFile(new
                                                                                            File
                                                                                            (uri)),
                                                                            "application/vnd" +
                                                                                    ".android" +
                                                                                    ".package-archive");
                                                                    activity.startActivity(intent);
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        }).start();
                                                    }
                                                })
                                                .setNegativeButton("取消", null).show();
                                    } else if (!silence)
                                        Toast.makeText(activity.getApplicationContext(),
                                                "已是最新版本", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(activity.getApplicationContext(), "获取错误",
                                            Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(activity.getApplicationContext(), "获取错误" + e
                                        .getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        }).start();
    }
}
