package ddw.com.richang.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import ddw.com.richang.HomeActivity;
import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.ui.login.LoginActivity;

/**
 * Created by zzp on 2016/6/24.
 */
@ContentView(R.layout.mine_setting_layout)
public class MineSetting extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);

    }

    /**
     * 关闭当前界面
     *
     * @param v 布局中的点击事件
     */
    public void closeCurrentWin(View v) {
        finish();
    }

    /**
     * 退出登录
     *
     * @param v 布局中的点击事件
     */
    public void exitLogin(View v) {
        SharePreferenceManager.getInstance().clearAll();
        riChangActivityManager.startNextActivity(LoginActivity.class);
        riChangActivityManager.popOtherActivity(LoginActivity.class);
        finish();
    }
}
