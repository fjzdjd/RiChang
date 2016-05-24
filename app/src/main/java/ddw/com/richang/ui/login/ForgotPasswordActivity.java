package ddw.com.richang.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;

/**
 * 忘记密码
 * Created by zzp on 2016/5/13.
 */
public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.login_forgot_psd_activty_layout);
    }
}
