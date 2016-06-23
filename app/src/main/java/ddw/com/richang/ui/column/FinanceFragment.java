package ddw.com.richang.ui.column;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.adapter.CheckIndustryAdapter;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.custom.refresh.PtrClassicFrameLayout;
import ddw.com.richang.custom.refresh.PtrDefaultHandler;
import ddw.com.richang.custom.refresh.PtrFrameLayout;
import ddw.com.richang.custom.refresh.PtrHandler;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiCheckIndustry;

/**
 * 金融
 * Created by zzp on 2016/6/20.
 */
public class FinanceFragment extends BaseFragment {

    @ViewInject(R.id.comprehensive_recyclerView)
    RecyclerView mRecyclerView;

    @ViewInject(R.id.comprehensive_ptr_refresh)
    PtrClassicFrameLayout mRefreshLayout;

    /**
     * 当前界面数据
     */
    private ArrayList<RiCheckIndustry> mArrayListData = new ArrayList<>();
    private CheckIndustryAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData(SharePreferenceManager.getInstance().getString(ConstantData.USER_CITY_ID, "1"),
                "0", "4");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.column_viewpager_layout, container, false);
        x.view().inject(this, view);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        refreshHandler();

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));
        mAdapter = new CheckIndustryAdapter(getActivity(), mArrayListData);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        return view;
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

                        initData(SharePreferenceManager.getInstance().getString(ConstantData
                                        .USER_CITY_ID, "1"),
                                "0", "2");

                        mRefreshLayout.refreshComplete();
                    }
                }, 0);
            }
        });


    }

    /**
     * 查看专栏数据
     *
     * @param ct_id    城市ID
     * @param start_id 起始活动 id
     * @param ind_id   行业id
     */
    private void initData(String ct_id, String start_id, String ind_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getIndActivity);
        params.addBodyParameter("ct_id", ct_id);
        params.addBodyParameter("start_id", start_id);
        params.addBodyParameter("ind_id", ind_id);
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
                                JSONArray jsonArray = jsonObject.optJSONArray("data");
                                if (code.equals(ConstantData.CODE)) {
                                    List<RiCheckIndustry> riCheckIndustries = JSON.parseArray
                                            (jsonArray.toString(), RiCheckIndustry.class);

                                    mArrayListData.addAll(riCheckIndustries);

                                    mAdapter.notifyDataSetChanged();


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                });

    }

    /**
     * 分割线
     */
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView
                .State state) {
            //不是第一个的格子都设一个左边和底部的间距
            outRect.left = space;
            outRect.bottom = space;
            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.left = 0;
            }
        }

    }

}
