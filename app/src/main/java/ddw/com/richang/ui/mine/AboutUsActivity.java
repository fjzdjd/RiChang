package ddw.com.richang.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiGetVersion;
import ddw.com.richang.update.UpdateHelperHttpUrlConnection;
import ddw.com.richang.util.CommonUtils;

/**
 * 关于我们
 * Created by zzp on 2016/6/15.
 */
@ContentView(R.layout.mine_about_activity_layout)
public class AboutUsActivity extends BaseActivity {

    @ViewInject(R.id.about_txt_version)
    TextView mVersion;

    @ViewInject(R.id.about_rlt_update)
    RelativeLayout mUpdate;

    @ViewInject(R.id.about_txt_update)
    TextView mTxtUpdate;

    private String mOldVersion;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);

        mOldVersion = CommonUtils.getVersion(AboutUsActivity.this);

        mVersion.setText("日常V" + mOldVersion);

        getVersionInfo("1");

    }

    /**
     * 关闭当前界面
     *
     * @param v 布局中的点击事件
     */
    public void closeCurrentWin(View v) {
        finish();
    }

    @Event(value = {R.id.about_rlt_introduce, R.id.about_rlt_weChat}, type
            = View.OnClickListener.class)
    private void setOnClickEvent(View view) {
        switch (view.getId()) {

            //产品介绍
            case R.id.about_rlt_introduce:

                break;

            //微信平台
            case R.id.about_rlt_weChat:

                break;

            default:
                break;
        }

    }


    /**
     * 获取版本信息
     *
     * @param app_type Android 1
     */
    private void getVersionInfo(String app_type) {
        RequestParams params = new RequestParams(InterFace.getInstance().getVersion);
        params.addBodyParameter("app_type", app_type);
        x.http().post(params, new Callback
                .CommonCallback<String>() {

            private boolean hasError = false;

            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {

                    final RiGetVersion mRiGetVersion = JSON.parseObject(result, RiGetVersion.class);

                    //将获取的版本信息存入内存
                    SharePreferenceManager.getInstance().setString(ConstantData.APK_DOWNLOAD_URL,
                            mRiGetVersion.getData().getApp_url());
                    SharePreferenceManager.getInstance().setString(ConstantData.APK_UPDATE_CONTENT,
                            mRiGetVersion.getData().getApp_msg());
                    SharePreferenceManager.getInstance().setString(ConstantData.APK_VERSION_CODE,
                            mRiGetVersion.getData().getApp_version());

                    if (!mRiGetVersion.equals(mOldVersion)) {
                        mTxtUpdate.setText(Html.fromHtml("版本升级 &nbsp;<font color='#ff0000'>" +
                                mRiGetVersion.getData().getApp_version() + "</font>"));
                    }

                    //在网络请求完成之后进行点击时间的设置
                    mUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOldVersion.equals(mRiGetVersion.getData().getApp_version())) {
                                Toast.makeText(AboutUsActivity.this, "当前为最新版本", Toast
                                        .LENGTH_SHORT).show();
                            } else {

                                //升级方法
                                UpdateHelperHttpUrlConnection helper = new
                                        UpdateHelperHttpUrlConnection.Builder(AboutUsActivity.this)
                                        .checkUrl(null)
                                        .isAutoInstall(false)
                                        .build();
                                helper.check();

                            }
                        }
                    });
                }
            }
        });
    }
}
