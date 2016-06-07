package ddw.com.richang.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import ddw.com.richang.base.BaseActivity;

/**
 * Created by zzp on 2016/6/7.
 */
public class ModifyInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
