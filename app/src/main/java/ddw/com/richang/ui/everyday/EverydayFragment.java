package ddw.com.richang.ui.everyday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.adapter.EverydayAdapter;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.custom.refresh.PtrClassicFrameLayout;
import ddw.com.richang.custom.refresh.PtrDefaultHandler;
import ddw.com.richang.custom.refresh.PtrFrameLayout;
import ddw.com.richang.custom.refresh.PtrHandler;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiActivityRecommend;
import ddw.com.richang.model.RiBannerData;
import ddw.com.richang.ui.login.LoginActivity;
import ddw.com.richang.ui.login.NetworkImageHolderView;
import ddw.com.richang.util.CommonUtils;
import ddw.com.richang.util.StringUtils;

/**
 * 日常
 * Created by zzp on 2016/5/13.
 */
public class EverydayFragment extends BaseFragment {


    private ListView mListView;

    /**
     * 广告栏 banner图片
     */
    private List<String> networkImages = new ArrayList<>();

    /**
     * 当前界面数据
     */
    private List<RiActivityRecommend> mJsonDatas = new ArrayList<>();

    /**
     * 活动推荐
     */
    private TextView mActivityRecommend1, mActivityRecommend2;

    /**
     * 发布者推荐
     */
    private TextView mPosterRecommend1, mPosterRecommend2;
    private ConvenientBanner mConvenientBanner;

    /**
     * 头部导航栏(热点，大学，附近，精选)
     */
    private TextView mHotActivity, mUniversity, mSelection, mBearby;

    private EverydayAdapter mAdapterEveryday;
    private int user_id = 0;
    private int ct_id = 1;
    private int start_id = 0;
    private int num = 0;
    private TextView mChoiceCity;
    /**
     * 应用广播
     */
    private GetLocalBroadcastToRefresh mGetLocalBroadcastToRefresh;
    private TextView mChoiceTag;
    private PtrClassicFrameLayout mRefreshLayout;
    private TextView mListLoadMore;
    private ProgressBar mListLoadMorePb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.everyday_fragment_layout, container, false);

        registerBroadcastToRefresh();//注册广播

        initWidgets(view);

        initData();

        return view;
    }


    /**
     * 初始化界面
     */
    private void initWidgets(View view) {

        mListView = (ListView) view.findViewById(R.id.everyday_fragment_listView);

        mRefreshLayout = (PtrClassicFrameLayout) view.findViewById(R.id.everyday_refresh_ptr);

        mChoiceCity = (TextView) view.findViewById(R.id.everyday_fragment_txt_choiceCity);

        mChoiceTag = (TextView) view.findViewById(R.id.everyday_fragment_txt_choiceTag);

        //获取当前用户的城市名称
        if (!StringUtils.isEmpty(SharePreferenceManager.getInstance().getString(ConstantData
                .USER_CITY_NAME, ""))) {
            mChoiceCity.setText(SharePreferenceManager.getInstance().getString(ConstantData
                    .USER_CITY_NAME, ""));
        }

        //listView头部
        View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout
                .everyday_header_layout, null);

        //添加listView底部加载更多
        View mListFooterView = LayoutInflater.from(getActivity()).inflate(R.layout
                .listview_footer_loadmore, null);

        mListLoadMore = (TextView) mListFooterView.findViewById(R.id.list_text_loadMore);

        mListLoadMorePb = (ProgressBar) mListFooterView.findViewById(R.id
                .list_progressBar_loadMore);


        //活动推荐
        mActivityRecommend1 = (TextView) mHeaderView.findViewById(R.id
                .everyday_header_txt_acRecommend1);
        mActivityRecommend2 = (TextView) mHeaderView.findViewById(R.id
                .everyday_header_txt_acRecommend2);

        //发布者推荐
        mPosterRecommend1 = (TextView) mHeaderView.findViewById(R.id
                .everyday_header_txt_ptRecommend1);
        mPosterRecommend2 = (TextView) mHeaderView.findViewById(R.id
                .everyday_header_txt_ptRecommend2);

        //广告栏
        mConvenientBanner = (ConvenientBanner) mHeaderView.findViewById(R.id
                .everyday_header_banner);

        mHotActivity = (TextView) mHeaderView.findViewById(R.id.everyday_header_txt_hot);
        mUniversity = (TextView) mHeaderView.findViewById(R.id.everyday_header_txt_university);
        mSelection = (TextView) mHeaderView.findViewById(R.id.everyday_header_txt_selection);
        mBearby = (TextView) mHeaderView.findViewById(R.id.everyday_header_txt_nearby);

        mListView.addHeaderView(mHeaderView);

        mListView.addFooterView(mListFooterView);

        mAdapterEveryday = new EverydayAdapter(getActivity(), mJsonDatas);

        mListView.setAdapter(mAdapterEveryday);

        setClickEvent();


    }

    /**
     * 设置click and refresh事件
     */
    private void setClickEvent() {

        mChoiceCity.setOnClickListener(new EverydayOnClickListener());
        mChoiceTag.setOnClickListener(new EverydayOnClickListener());

        refreshHandler();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CommonUtils.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), ContentDetailActivity.class);
                intent.putExtra("ac_id", mJsonDatas.get(position - 1).getAc_id());
                startActivity(intent);
            }
        });

        /**
         * 监听listView滑动,加载更多
         */
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //The view is not scrolling.
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {

                            mListLoadMore.setVisibility(View.INVISIBLE);
                            mListLoadMorePb.setVisibility(View.VISIBLE);
                            getInitDatas();

                        }
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

            }
        });

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

                        if (!CommonUtils.isNetworkAvailable(getActivity())) {
                            Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mJsonDatas.clear();
                        getInitDatas();

                        mRefreshLayout.refreshComplete();
                    }
                }, 0);
            }
        });

    }


    /**
     * 初始化数据
     */
    private void initData() {

        //banner数据
        getBannerImageData();

        //list数据
        getInitDatas();

    }


    /**
     * 获取banner栏数据
     */
    private void getBannerImageData() {
        RequestParams params = new RequestParams(InterFace.getInstance().getFlash);
        // 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
        params.setCacheMaxAge(1000 * 60);
        Callback.Cancelable cancelable
                = x.http().get(params, new Callback.CacheCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) {
                this.result = result;
                return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
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

                Toast.makeText(x.app(), "网络访问失败!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "网络访问失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        String code = jsonObject.optString("code");

                        JSONArray jsonArray = jsonObject.optJSONArray("data");

                        if (code.equals(ConstantData.CODE)) {

                            List<RiBannerData> mBannerImages = JSON.parseArray(jsonArray.toString
                                    (), RiBannerData.class);

                            for (int i = 0; i < mBannerImages.size(); i++) {
                                networkImages.add(mBannerImages.get(i).getImg());
                            }

                            initBannerImageLoader(networkImages, mBannerImages);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    /**
     * 推荐数据
     */
    private void getInitDatas() {
        if (!StringUtils.isEmpty(SharePreferenceManager.getInstance().getString(ConstantData
                .USER_ID, ""))) {
            user_id = Integer.parseInt(SharePreferenceManager.getInstance().getString(ConstantData
                    .USER_ID, ""));
        }

        if (!StringUtils.isEmpty(SharePreferenceManager.getInstance().getString(ConstantData
                .USER_CITY_ID, ""))) {
            ct_id = Integer.parseInt(SharePreferenceManager.getInstance().getString(ConstantData
                    .USER_CITY_ID, ""));
        }

        getActivityRecommendDatas(String.valueOf(user_id), String.valueOf(ct_id), "", "");
    }

    /**
     * 获取为你推荐
     * <P>测试数据 1,1,2,1</P>
     *
     * @param ct_id    城市id
     * @param start_id 起始活动id
     * @param num      活动数目
     * @param usr_id   用户id
     */
    private void getActivityRecommendDatas(String usr_id, String ct_id, String start_id, String
            num) {
        RequestParams params = new RequestParams(InterFace.getInstance().getRecommendActivity);
        params.addBodyParameter("usr_id", usr_id);
        params.addBodyParameter("ct_id", ct_id);
        params.addBodyParameter("start_id", start_id);
        params.addBodyParameter("num", num);
        // 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
        params.setCacheMaxAge(1000 * 60);
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
                        Toast.makeText(x.app(), "网络访问失败!", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "网络访问失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        if (!hasError && result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.optString("code");
                                if (code.equals("200")) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("data");

                                    List<RiActivityRecommend> mJsonRecommends = JSON.parseArray
                                            (jsonArray.toString(), RiActivityRecommend
                                                    .class);

                                    mJsonDatas.addAll(mJsonRecommends);

                                    mAdapterEveryday.setListData(mJsonDatas);

                                    //更改底部栏加载更多
                                    mListLoadMore.setVisibility(View.VISIBLE);
                                    mListLoadMorePb.setVisibility(View.GONE);

                                } else {
                                    mAdapterEveryday.setListData(mJsonDatas);

                                    //更改底部栏加载更多
                                    mListLoadMore.setVisibility(View.VISIBLE);
                                    mListLoadMorePb.setVisibility(View.GONE);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });

    }

    @Override
    public void onResume() {
        super.onResume();
        //开启广告栏自动切换
        mConvenientBanner.startTurning(3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止广告栏切换
        mConvenientBanner.stopTurning();

        //注销广播
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver
                (mGetLocalBroadcastToRefresh);
    }

    /**
     * banner广告栏数据加载
     */
    private void initBannerImageLoader(final List<String> data, final List<RiBannerData> mObjData) {
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, data)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.dot_white, R.mipmap.dot_orange})
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getActivity(), ContentDetailActivity.class);
                        intent.putExtra("ac_id", mObjData.get(position).getAct_id());
                        startActivity(intent);

                    }
                });
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

                mJsonDatas.clear();
                getInitDatas();
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
            switch (v.getId()) {
                //选择城市
                case R.id.everyday_fragment_txt_choiceCity:
                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    startActivity(new Intent(getActivity(), ChoseCityActivity.class));

                    break;
                //选择标签
                case R.id.everyday_fragment_txt_choiceTag:
                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                        return;
                    }

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


}
