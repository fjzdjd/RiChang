package ddw.com.richang.ui.mine;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.custom.refresh.PtrClassicFrameLayout;
import ddw.com.richang.custom.refresh.PtrDefaultHandler;
import ddw.com.richang.custom.refresh.PtrFrameLayout;
import ddw.com.richang.custom.refresh.PtrHandler;

@ContentView(R.layout.mine_publish_activity_layout)
public class PublishActivity extends BaseActivity {

    @ViewInject(R.id.publish_txt_had)
    TextView mHadPublish;

    @ViewInject(R.id.publish_txt_audit)
    TextView mAudit;

    @ViewInject(R.id.publish_txt_finish)
    TextView mFinish;

    @ViewInject(R.id.publish_list_data)
    ListView mListView;

    @ViewInject(R.id.publish_refresh_ptr)
    PtrClassicFrameLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);
        refreshHandler();
    }


    /**
     * 刷新
     */
    private void refreshHandler() {
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        // the following are default settings
        mRefreshLayout.setResistance(3.5f);
        mRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mRefreshLayout.setDurationToClose(200);
        mRefreshLayout.setDurationToCloseHeader(1000);
        // default is false
        mRefreshLayout.setPullToRefresh(false);
        // default is true
        mRefreshLayout.setKeepHeaderWhenRefresh(true);
        mRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {



                        mRefreshLayout.refreshComplete();
                    }
                }, 0);
            }
        });


    }


    /**
     * 关闭当前界面
     *
     * @param v 布局中的点击事件
     */
    public void closeCurrentWin(View v) {
        finish();
    }

    @Event(value = {R.id.publish_txt_had, R.id.publish_txt_audit, R.id.publish_txt_finish}, type
            = View.OnClickListener.class)
    private void setOnClickEvent(View view) {
        switch (view.getId()) {

            //已发布
            case R.id.publish_txt_had:

                mHadPublish.setTextColor(getResources().getColor(R.color.mainColor));
                mFinish.setTextColor(getResources().getColor(R.color.mainGrayTxtColor));
                mAudit.setTextColor(getResources().getColor(R.color.mainGrayTxtColor));

                Toast.makeText(PublishActivity.this, "111", Toast.LENGTH_SHORT).show();

                break;

            //审核中
            case R.id.publish_txt_audit:

                mHadPublish.setTextColor(getResources().getColor(R.color.mainGrayTxtColor));
                mFinish.setTextColor(getResources().getColor(R.color.mainGrayTxtColor));
                mAudit.setTextColor(getResources().getColor(R.color.mainColor));

                Toast.makeText(PublishActivity.this, "222", Toast.LENGTH_SHORT).show();

                break;

            //已完成
            case R.id.publish_txt_finish:

                mHadPublish.setTextColor(getResources().getColor(R.color.mainGrayTxtColor));
                mFinish.setTextColor(getResources().getColor(R.color.mainColor));
                mAudit.setTextColor(getResources().getColor(R.color.mainGrayTxtColor));

                Toast.makeText(PublishActivity.this, "333", Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
    }
}
