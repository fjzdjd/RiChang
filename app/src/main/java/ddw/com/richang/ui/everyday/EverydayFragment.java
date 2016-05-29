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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.components.ui.CustomUi.FilterView;
import ddw.com.richang.components.ui.SmoothListView.SmoothListView;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiActivityRecommend;
import ddw.com.richang.model.filter.FilterData;
import ddw.com.richang.ui.login.NetworkImageHolderView;
import ddw.com.richang.util.CommonUtils;
import ddw.com.richang.util.DensityUtil;
import ddw.com.richang.util.LogN;
import ddw.com.richang.util.ModelUtil;
import ddw.com.richang.util.StringUtils;

/**
 * 日常
 * Created by zzp on 2016/5/13.
 */
public class EverydayFragment extends BaseFragment {

    private int mScreenHeight;
    private FilterData filterData;
    private SmoothListView mSoothListView;

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
    private int ct_id = 4;
    private int start_id = 0;
    private int num = 0;
    private TextView mChoiceCity;
    /**
     * 应用广播
     */
    private GetLocalBroadcastToRefresh mGetLocalBroadcastToRefresh;
    private TextView mChoiceTag;

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
        FilterView mFvTopFilter = (FilterView) view.findViewById(R.id.everyday_fragment_fv_filter);

        mFvTopFilter.setFilterData(getActivity(), filterData);

        mSoothListView = (SmoothListView) view.findViewById(R.id.everyday_fragment_listView);

        mChoiceCity = (TextView) view.findViewById(R.id.everyday_fragment_txt_choiceCity);

        mChoiceTag = (TextView) view.findViewById(R.id.everyday_fragment_txt_choiceTag);

        //获取当前用户的城市名称
        if (!StringUtils.isEmpty(SharePreferenceManager.getInstance().getString(ConstantData
                .USER_CITY_NAME, ""))) {
            mChoiceCity.setText(SharePreferenceManager.getInstance().getString(ConstantData
                    .USER_CITY_NAME, ""));

        }

        mChoiceCity.setOnClickListener(new EverydayOnClickListener());
        mChoiceTag.setOnClickListener(new EverydayOnClickListener());

        View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout
                .everyday_header_layout, null);

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

        mSoothListView.addHeaderView(mHeaderView);

        //分类，下一个版本
//        HeaderFilterViewView headerFilterViewView = new HeaderFilterViewView(getActivity());
//        headerFilterViewView.fillView(new Object(), mSoothListView);

        mAdapterEveryday = new EverydayAdapter(getActivity(), mJsonDatas);

        mSoothListView.setAdapter(mAdapterEveryday);

        //上下拉刷新
        mSoothListView.setRefreshEnable(true);
        mSoothListView.setLoadMoreEnable(true);

        mSoothListView.setSmoothListViewListener(new SmoothListView.ISmoothListViewListener() {
            @Override
            public void onRefresh() {

                mJsonDatas.clear();
                getInitDatas();
                //加入刷新时间
                String time = CommonUtils.getCurrentTime("yyyy-MM-dd  hh:mm:ss");
                mSoothListView.setRefreshTime(time);

            }

            @Override
            public void onLoadMore() {

                getActivityRecommendDatas(String.valueOf(user_id), String.valueOf(ct_id), "", "");
            }
        });


        mSoothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ContentDetailActivity.class);
                intent.putExtra("ac_id",mJsonDatas.get(position).getAc_id());
                startActivity(intent);
            }
        });



    }

    /**
     * 初始化数据
     */
    private void initData() {
        mScreenHeight = DensityUtil.getWindowHeight(getActivity());

        //分类数据
//        filterData = new FilterData();
//        filterData.setCategory(ModelUtil.getCategoryData());
//        filterData.setSorts(ModelUtil.getSortData());
//        filterData.setFilters(ModelUtil.getFilterData());

        getInitDatas();

    }

    /**
     * 获取初始化数据
     */
    private void getInitDatas() {
        if (!StringUtils.isEmpty(SharePreferenceManager.getInstance().getString(ConstantData
                .USER_CITY_ID, ""))) {
            user_id = Integer.parseInt(SharePreferenceManager.getInstance().getString(ConstantData
                    .USER_ID, ""));
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
                        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinished() {
                        if (!hasError && result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.optString("code");
                                if (code.equals("200")) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("data");

                                    LogN.d(this, "555555" + jsonArray.toString());

                                    List<RiActivityRecommend> mJsonRecommends = JSON.parseArray
                                            (jsonArray.toString(), RiActivityRecommend
                                                    .class);

                                    networkImages.clear();
                                    for (int i = 0; i < mJsonRecommends.size(); i++) {
                                        networkImages.add(mJsonRecommends.get(i).getAc_poster());
                                    }


                                    mJsonDatas.addAll(mJsonRecommends);

                                    mAdapterEveryday.setListData(mJsonDatas);

                                    initImageLoader();
                                    mSoothListView.stopRefresh();
                                    mSoothListView.stopLoadMore();
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
     * 初始化网络图片缓存库
     */
    private void initImageLoader() {
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, networkImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.dot_white, R.mipmap
                        .dot_orange});
    }

    /**
     * 注册广播
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
     * 适配器布局
     */
    static class ViewHolder {

        private final ImageView acPoster;
        private final TextView acTitle;
        private final TextView acTime;
        private final TextView acPlace;

        /**
         * 字段中暂无此值
         */
        private final TextView acCostTags;
        private final TextView acReadNum;

        ViewHolder(View view) {

            acPoster = (ImageView) view.findViewById(R.id.everyday_adapter_img_item);
            acTitle = (TextView) view.findViewById(R.id.everyday_adapter_txt_title);
            acTime = (TextView) view.findViewById(R.id.everyday_adapter_txt_time);
            acPlace = (TextView) view.findViewById(R.id.everyday_adapter_txt_place);
            acCostTags = (TextView) view.findViewById(R.id.everyday_adapter_txt_tag);
            acReadNum = (TextView) view.findViewById(R.id.everyday_adapter_txt_num);

        }

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

                    startActivity(new Intent(getActivity(), ChoseCityActivity.class));

                    break;
                //选择标签
                case R.id.everyday_fragment_txt_choiceTag:

                    startActivity(new Intent(getActivity(), ChoseTagActivity.class));

                    break;

                default:
                    break;
            }
        }
    }

    /**
     * adapter适配器
     */
    private class EverydayAdapter extends BaseAdapter {

        private List<RiActivityRecommend> mList;

        private HashMap<Integer, View> viewChache = new HashMap<>();

        public EverydayAdapter(Context context, List<RiActivityRecommend> list) {

            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 设置数据
         *
         * @param list
         */
        public void setListData(List<RiActivityRecommend> list) {

            notifyDataSetChanged();


        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //暂无数据的布局
//            if (isNoData) {
//                convertView = LayoutInflater.from(getActivity()).inflate(R.layout
// .item_no_data_layout, null);
//                AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup
//                        .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                RelativeLayout rootView = (RelativeLayout) convertView.findViewById(R.id
//                        .rl_root_view);
//                rootView.setLayoutParams(params);
//
//                return convertView;
//            }

            ViewHolder viewHolder;

            if (viewChache.get(position) == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout
                        .item_everyday_list_layout, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
                viewChache.put(position, convertView);
            } else {
                convertView = viewChache.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }

            x.image().bind(viewHolder.acPoster, mList.get(position).getAc_poster());
            viewHolder.acTitle.setText(mList.get(position).getAc_title());
            viewHolder.acTime.setText(mList.get(position).getAc_time());
            viewHolder.acPlace.setText(mList.get(position).getAc_place());
            viewHolder.acReadNum.setText(" " + mList.get(position).getAc_read_num());
            viewHolder.acCostTags.setText(" " + "免费活动");


            return convertView;
        }


    }
}
