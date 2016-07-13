package ddw.com.richang.ui.everyday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.ui.login.LoginActivity;
import ddw.com.richang.util.CommonUtils;
import ddw.com.richang.util.LogN;
import ddw.com.richang.util.StringUtils;

/**
 * 日常
 * Created by zzp on 2016/5/13.
 */
public class EverydayFragment extends BaseFragment {


    private TextView mChoiceCity;
    /**
     * 应用广播
     */
    private GetLocalBroadcastToRefresh mGetLocalBroadcastToRefresh;
    private TextView mChoiceTag;
    private TabLayout mTabMenu;
    private ViewPager mViewPager;

    private String[] mMenuTitle = {"活动推荐", "发布者推荐"};

    /**
     * viewpager子页面
     */
    private ArrayList<BaseFragment> mArrayListFragment = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.everyday_fragment_layout, container, false);

        registerBroadcastToRefresh();//注册广播

        initWidgets(view);

        initChildFragment();

        return view;
    }


    /**
     * 初始化子页面
     */
    private void initChildFragment() {
        RecommendActivitiesFragment mRecommendActivitiesFragment = new
                RecommendActivitiesFragment();

        RecommendPublisherFragment mRecommendPublisherFragment = new RecommendPublisherFragment();

        mArrayListFragment.add(mRecommendActivitiesFragment);
        mArrayListFragment.add(mRecommendPublisherFragment);

    }


    /**
     * 初始化界面
     */
    private void initWidgets(View view) {

        mChoiceCity = (TextView) view.findViewById(R.id.everyday_fragment_txt_choiceCity);

        mChoiceTag = (TextView) view.findViewById(R.id.everyday_fragment_txt_choiceTag);

        mTabMenu = (TabLayout) view.findViewById(R.id.everyday_tlt_menu);
        mViewPager = (ViewPager) view.findViewById(R.id.everyday_vpr_pager);

        View mSearch = view.findViewById(R.id.everyday_view_search);

        mSearch.setOnClickListener(new EverydayOnClickListener());

        mChoiceCity.setOnClickListener(new EverydayOnClickListener());
        mChoiceTag.setOnClickListener(new EverydayOnClickListener());

        //获取当前用户的城市名称
        if (!StringUtils.isEmpty(SharePreferenceManager.getInstance().getString(ConstantData
                .USER_CITY_NAME, ""))) {
            mChoiceCity.setText(SharePreferenceManager.getInstance().getString(ConstantData
                    .USER_CITY_NAME, ""));
        }

        mViewPager.setAdapter(new ColumnChildFragment(getFragmentManager(), mMenuTitle));

        mTabMenu.setTabMode(TabLayout.MODE_FIXED);


        mTabMenu.setupWithViewPager(mViewPager);


        mTabMenu.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //注销广播
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver
                (mGetLocalBroadcastToRefresh);
    }


    /**
     * 注册选择城市广播
     */
    private void registerBroadcastToRefresh() {
        mGetLocalBroadcastToRefresh = new GetLocalBroadcastToRefresh();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ChoseCityActivity.mBroadcastRegistFlag);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver
                (mGetLocalBroadcastToRefresh, filter);
    }


    /**
     * 获取广播信息，针对不同消息进行刷新
     */
    private class GetLocalBroadcastToRefresh extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra(ChoseCityActivity.mBroadcastRegistFlag);
            if (message.equals("choseCity")) {

                mChoiceCity.setText(SharePreferenceManager.getInstance().getString(ConstantData
                        .USER_CITY_NAME, ""));

            }

        }
    }

    /**
     * 当前页面点击事件
     */
    private class EverydayOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!CommonUtils.isNetworkAvailable(getActivity())) {
                Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (v.getId()) {
                //选择城市
                case R.id.everyday_fragment_txt_choiceCity:


                    startActivity(new Intent(getActivity(), ChoseCityActivity.class));

                    break;

                //搜索
                case R.id.everyday_view_search:

                    startActivity(new Intent(getActivity(), SearchActivity.class));

                    break;


                //选择标签
                case R.id.everyday_fragment_txt_choiceTag:

                    if (StringUtils.isEmpty(SharePreferenceManager.getInstance().getString
                            (ConstantData.USER_ID, ""))) {

                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    } else {

                        startActivity(new Intent(getActivity(), ChoseTagActivity.class));
                    }

                    break;

                default:
                    break;
            }
        }
    }


    /**
     * viewpager Fragment adapter
     */
    private class ColumnChildFragment extends FragmentPagerAdapter {

        private String[] mMenuTitle;

        public ColumnChildFragment(FragmentManager fm, String[] mMenuTitle) {
            super(fm);
            this.mMenuTitle = mMenuTitle;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mArrayListFragment.get(position);

                case 1:
                    return mArrayListFragment.get(position);


                default:

                    return null;
            }
        }

        @Override
        public int getCount() {
            return mMenuTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMenuTitle[position];
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);

            LogN.d(this, "current pager");

        }
    }

}
