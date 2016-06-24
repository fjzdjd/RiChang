package ddw.com.richang.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import ddw.com.richang.HomeActivity;
import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiUserLogin;

/**
 * 注册
 * Created by zzp on 2016/5/12.
 */
public class RegisterActivity extends BaseActivity {

    private ImageView mFinishPage;
    private EditText mPhoneNum;
    private EditText mPassword;
    private EditText mPasswordCom;
    private EditText mVerificationCode;
    private Button mRegister;
    private TextView mSendCode;
    private TimeCount mTimeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.login_register_activity_layout);
        initWidgets();
        initData();
    }

    private void initData() {
        mTimeCount = new TimeCount(60000, 1000);

    }

    /**
     * 初始化界面
     */
    private void initWidgets() {
        mFinishPage = (ImageView) findViewById(R.id.login_register_img_goLogin);
        mPhoneNum = (EditText) findViewById(R.id.login_register_edt_phone);
        mPassword = (EditText) findViewById(R.id.login_register_edt_password);
        mPasswordCom = (EditText) findViewById(R.id.login_register_edt_passwordCom);
        mVerificationCode = (EditText) findViewById(R.id.login_register_edt_code);
        mRegister = (Button) findViewById(R.id.login_register_btn_register);
        mSendCode = (TextView) findViewById(R.id.login_register_txt_sendCode);

        mFinishPage.setOnClickListener(new RegisterOnClickListener());
        mRegister.setOnClickListener(new RegisterOnClickListener());
        mSendCode.setOnClickListener(new RegisterOnClickListener());
    }


    /**
     * 注册后自动登录
     */
    private void registerRiChang(String phoneNum, String password) {
        showWaitDialog("登录中...", R.color.transparent);
        RequestParams params = new RequestParams(InterFace.getInstance().getUSRInfo);
        params.addBodyParameter("usr_phone", phoneNum);
        params.addBodyParameter("act_passwd", password);
        params.addBodyParameter("op_type", "2");
        Callback.Cancelable cancelable = x.http().post(params,
                new Callback.CacheCallback<String>() {
                    private boolean hasError = false;
                    private String result = null;


                    @Override
                    public boolean onCache(String result) {
                        this.result = result;
                        return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
                    }

                    @Override
                    public void onSuccess(String result) {
                        // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                        if (result != null) {
                            this.result = result;
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        hasError = true;
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        dismissWaitDialog();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                        dismissWaitDialog();
                    }

                    @Override
                    public void onFinished() {
                        if (!hasError && result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.optString("code");
                                if (code.equals("200")) {

                                    RiUserLogin registData = JSON.parseObject(result, RiUserLogin
                                            .class);

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_CITY_ID, registData.getData().getCt_id());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_CITY_NAME, registData.getData().getCt_name());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_GENDER, registData.getData().getUsr_sex());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_ID, registData.getData().getUsr_id());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_MAIL, registData.getData().getUsr_mail());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_NAME, registData.getData().getUsr_name());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_PHONE, registData.getData().getUsr_phone());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_SIGN, registData.getData().getUsr_sign());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_PIC, registData.getData().getUsr_pic());

                                    riChangActivityManager.startNextActivity(HomeActivity.class);

                                    Toast.makeText(RegisterActivity.this, "登录成功", Toast
                                            .LENGTH_SHORT).show();

                                    finish();

                                } else if (code.equals("210")) {//密码错误

                                    Toast.makeText(RegisterActivity.this, "用户已注册!", Toast
                                            .LENGTH_SHORT).show();

                                } else if (code.equals("310")) {//手机号不存在
                                    Toast.makeText(RegisterActivity.this, "用户不存在", Toast
                                            .LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        dismissWaitDialog();
                    }
                });
    }


    /**
     * 注册点击事件
     */
    private class RegisterOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //finish注册
                case R.id.login_register_img_goLogin:

                    finish();

                    break;

                //注册
                case R.id.login_register_btn_register:

                    break;

                //发送验证码
                case R.id.login_register_txt_sendCode:
                    mTimeCount.start();

                    break;

                default:
                    break;


            }
        }
    }


    /**
     * 倒计时
     */
    private class TimeCount extends CountDownTimer {

        /***
         * @param millisInFuture    总时长
         * @param countDownInterval 计时的时间间隔
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mSendCode.setText("重新验证");
            mSendCode.setClickable(true);
            mSendCode.setBackground(getDrawable(R.drawable.corners_bg_all_orange));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mSendCode.setClickable(false);
            mSendCode.setText("重发 " + millisUntilFinished / 1000 + " 秒");
            mSendCode.setBackground(getDrawable(R.drawable.corners_bg_all_gray));

        }
    }

}
