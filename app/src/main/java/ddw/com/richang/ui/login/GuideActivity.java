package ddw.com.richang.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;

/**
 * 2.0引导界面
 * Created by zzp on 2016/5/11.
 */
public class GuideActivity extends BaseActivity implements ViewPager
        .OnPageChangeListener {

    /**
     * splash
     */
    private ConvenientBanner mGuidePictures;

    private ArrayList<Integer> localImages = new ArrayList<>();
    private Button mStartRiChang;

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName 文件名
     * @param c            获取类
     * @return 返回Int值
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.login_guide_activity_layout);
        initWidgets();
        initDatas();

    }

    /**
     * 初始化图片数据
     */
    private void initDatas() {
        for (int position = 1; position < 5; position++) {
            localImages.add(getResId("tutor" + position, R.mipmap.class));
        }

        mGuidePictures.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages);

        //设置两个点图片作为翻页指示器，不设置则没有指示器,可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        //.setPageIndicator(new int[]{R.mipmap.dot_white, R.mipmap.dot_orange})

        //控制循环，但是在xml中必须设为true
        mGuidePictures.setCanLoop(false);

        //监听翻页
        mGuidePictures.setOnPageChangeListener(this);


    }

    /**
     * 初始化界面
     */
    private void initWidgets() {
        mGuidePictures = (ConvenientBanner) findViewById(R.id.login_guide_convenientBanner);
        mStartRiChang = (Button) findViewById(R.id.login_guide_btn_start);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //开始自动执行
//        mGuidePictures.startTurning(5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //停止翻页
//        mGuidePictures.stopTurning();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 3) {
            mStartRiChang.setVisibility(View.VISIBLE);
            mStartRiChang.setOnClickListener(new GuideOnClickListener());
        } else {
            mStartRiChang.setVisibility(View.GONE);
        }

//        Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
    }

    private class GuideOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_guide_btn_start:
                    riChangActivityManager.startNextActivity(LoginActivity.class);

                    GuideActivity.this.finish();

                    break;

                default:
                    break;
            }

        }
    }

}
