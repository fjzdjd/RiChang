package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;

/**
 * Created by zzp on 2016/7/13.
 */
@ContentView(R.layout.everyday_remind_activity_layout)
public class RemindTimeActivity extends BaseActivity {

    @ViewInject(R.id.remind_img_never)
    ImageView mImageNeverMind;

    @ViewInject(R.id.remind_img_oneH)
    ImageView mImageOneHour;

    @ViewInject(R.id.remind_img_oneD)
    ImageView mImageOneDay;

    @ViewInject(R.id.remind_img_threeD)
    ImageView mImageThreeDay;

    @ViewInject(R.id.remind_txt_time)
    TextView mRemindTime;

    /**
     * 提醒时间
     */
    private String mStringTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);
        initChoice();

        mStringTime = getIntent().getStringExtra("remindHour");

        mRemindTime.setText(mStringTime);
    }


    /**
     * 关闭当前界面
     *
     * @param v 布局中的点击事件
     */
    public void closeCurrentWin(View v) {
        finish();
    }

    private void initChoice() {
        mImageOneDay.setVisibility(View.INVISIBLE);
        mImageOneHour.setVisibility(View.INVISIBLE);
        mImageThreeDay.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取设置后的日期值
     *
     * @param date 活动日期
     * @param type 操作 hour=10,day=7
     * @param num  加or减
     * @return 日期
     */
    private String getCurrentDate(String date, int type, int num) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateHour = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateHour);
            calendar.add(type, num);
            String currentTime = dateFormat.format(calendar.getTime());
            return currentTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Event(value = {R.id.remind_rlt_never, R.id.remind_rlt_oneD, R.id.remind_rlt_oneH, R.id
            .remind_rlt_threeD, R.id.remind_txt_confirm}, type = View.OnClickListener.class)
    private void onRemindClickEvent(View view) {
        switch (view.getId()) {
            //不提醒
            case R.id.remind_rlt_never:

                initChoice();
                mImageNeverMind.setVisibility(View.VISIBLE);

                mRemindTime.setText(mStringTime);

                break;

            //one hour
            case R.id.remind_rlt_oneH:

                mImageOneHour.setVisibility(View.VISIBLE);
                mImageOneDay.setVisibility(View.INVISIBLE);
                mImageNeverMind.setVisibility(View.INVISIBLE);
                mImageThreeDay.setVisibility(View.INVISIBLE);
                mRemindTime.setText(getCurrentDate(mStringTime, 10, -1));
                break;

            //one day
            case R.id.remind_rlt_oneD:

                mImageOneDay.setVisibility(View.VISIBLE);
                mImageNeverMind.setVisibility(View.INVISIBLE);
                mImageOneHour.setVisibility(View.INVISIBLE);
                mImageThreeDay.setVisibility(View.INVISIBLE);

                mRemindTime.setText(getCurrentDate(mStringTime, 7, -1));

                break;

            //three days
            case R.id.remind_rlt_threeD:

                mImageThreeDay.setVisibility(View.VISIBLE);
                mImageNeverMind.setVisibility(View.INVISIBLE);
                mImageOneHour.setVisibility(View.INVISIBLE);
                mImageOneDay.setVisibility(View.INVISIBLE);

                mRemindTime.setText(getCurrentDate(mStringTime, 7, -3));

                break;

            //confirm
            case R.id.remind_txt_confirm:

                break;

            default:
                break;
        }

    }

}
