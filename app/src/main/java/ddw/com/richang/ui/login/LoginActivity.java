package ddw.com.richang.ui.login;

import android.os.Bundle;
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
import ddw.com.richang.util.CommonUtils;
import ddw.com.richang.util.StringUtils;

/**
 * 登录
 * Created by zzp on 2016/5/12.
 */
public class LoginActivity extends BaseActivity {

    private TextView mGoToRegister;
    private EditText mPhoneNum;
    private EditText mPassword;
    private Button mLogin;
    private TextView mForgotPassword;
    private TextView mDirectEnter;
    private RiUserLogin riUserLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.login_in_activity_layout);
        initWidgets();
    }

    private void initWidgets() {
        mGoToRegister = (TextView) findViewById(R.id.login_in_txt_goRegister);
        mPhoneNum = (EditText) findViewById(R.id.login_in_edt_phone);
        mPassword = (EditText) findViewById(R.id.login_in_edt_password);
        mLogin = (Button) findViewById(R.id.login_in_btn_login);
        mForgotPassword = (TextView) findViewById(R.id.login_in_txt_forgot_password);
        mDirectEnter = (TextView) findViewById(R.id.login_in_txt_directEnter);
        ImageView mFinish = (ImageView) findViewById(R.id.login_in_img_finish);

        mFinish.setOnClickListener(new LoginOnClickListener());
        mGoToRegister.setOnClickListener(new LoginOnClickListener());
        mLogin.setOnClickListener(new LoginOnClickListener());
        mForgotPassword.setOnClickListener(new LoginOnClickListener());
        mDirectEnter.setOnClickListener(new LoginOnClickListener());

    }

    /**
     * 登录
     */
    private void loginRiChang(String phoneNum, String password) {
        showWaitDialog("登录中...", R.color.transparent);
        RequestParams params = new RequestParams(InterFace.getInstance().getUSRInfo);
        params.addBodyParameter("usr_phone", phoneNum);
        params.addBodyParameter("act_passwd", password);
        params.addBodyParameter("op_type", "1");
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
                                    riUserLogin = JSON.parseObject(result,
                                            RiUserLogin.class);

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_CITY_ID, riUserLogin.getData().getCt_id());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_CITY_NAME, riUserLogin.getData().getCt_name());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_GENDER, riUserLogin.getData().getUsr_sex());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_ID, riUserLogin.getData().getUsr_id());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_MAIL, riUserLogin.getData().getUsr_mail());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_NAME, riUserLogin.getData().getUsr_name());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_PHONE, riUserLogin.getData().getUsr_phone());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_SIGN, riUserLogin.getData().getUsr_sign());

                                    SharePreferenceManager.getInstance().setString(ConstantData
                                            .USER_PIC, riUserLogin.getData().getUsr_pic());

                                    //登录成功后跳转主界面
                                    riChangActivityManager.startNextActivity(HomeActivity.class);
                                    LoginActivity.this.finish();

                                    Toast.makeText(LoginActivity.this, riUserLogin.getMsg(),
                                            Toast.LENGTH_SHORT).show();
                                } else if (code.equals("210")) {//密码错误

                                    Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast
                                            .LENGTH_SHORT).show();

                                } else if (code.equals("310")) {//手机号不存在
                                    Toast.makeText(LoginActivity.this, "用户不存在", Toast
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
     * 点击监听事件
     */
    private class LoginOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //去注册
                case R.id.login_in_txt_goRegister:

                    riChangActivityManager.startNextActivity(RegisterActivity.class);

                    break;
                //登录
                case R.id.login_in_btn_login:

                    if (!CommonUtils.isNetworkAvailable(getApplication())) {
                        Toast.makeText(LoginActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String phoneNum = mPhoneNum.getText().toString().trim();
                    String password = mPassword.getText().toString().trim();

                    if (StringUtils.isEmpty(phoneNum)) {
                        Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!CommonUtils.isMobileNO(phoneNum)) {
                        Toast.makeText(LoginActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    loginRiChang(phoneNum,password);

                    break;

                //忘记密码
                case R.id.login_in_txt_forgot_password:
                    riChangActivityManager.startNextActivity(ForgotPasswordActivity.class);

                    break;

                //直接进入
                case R.id.login_in_txt_directEnter:

                    riChangActivityManager.startNextActivity(HomeActivity.class);
                    LoginActivity.this.finish();

                    break;

                //finish直接进入
                case R.id.login_in_img_finish:

                    riChangActivityManager.startNextActivity(HomeActivity.class);
                    LoginActivity.this.finish();

                    break;

                default:
                    break;
            }

        }
    }

}
