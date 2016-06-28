package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;

/**
 * 发布者个人资料
 * Created by zzp on 2016/6/28.
 */
@ContentView(R.layout.everyday_publisher_detail_layout)
public class PublisherDetailActivity extends BaseActivity {

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
}
