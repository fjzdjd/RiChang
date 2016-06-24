package ddw.com.richang.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.util.CommonUtils;

/**
 * 忘记密码
 * Created by zzp on 2016/5/13.
 */
@ContentView(R.layout.login_forgot_psd_activty_layout)
public class ForgotPasswordActivity extends BaseActivity {

    @ViewInject(R.id.forgot_edt_phone)
    EditText mPhoneNum;

    @ViewInject(R.id.forgot_edt_edt_code)
    EditText mVerifyCode;

    @ViewInject(R.id.forgot_txt_sendCode)
    TextView mGetCode;

    @ViewInject(R.id.forgot_edt_password)
    EditText mPassword;

    @ViewInject(R.id.forgot_edt_passwordCom)
    EditText mComPassword;

    @ViewInject(R.id.forgot_btn_reset)
    Button mResetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);
    }


    @Event(value = {R.id.forgot_txt_sendCode,R.id.forgot_btn_reset},type = View.OnClickListener.class)
    private  void setCllickEvent(View view){
        if (!CommonUtils.isNetworkAvailable(getApplication())) {
            Toast.makeText(ForgotPasswordActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()){
            case R.id.forgot_txt_sendCode:




                break;

            case R.id.forgot_btn_reset:



                break;

            default:
                break;
        }

    }

    /**
     * 重置密码
     * @param mobile 手机号
     * @param passwd 密码
     */
    private void resetPassword(String mobile,String passwd){

    }


    /**
     * 关闭当前界面
     *
     * @param v 布局中的点击事件
     */
    public void closeCurrentWin(View v) {
        finish();
    }
}
