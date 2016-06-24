package ddw.com.richang.ui.column;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import ddw.com.richang.adapter.ChoicenessAdapter;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.custom.refresh.PtrClassicFrameLayout;
import ddw.com.richang.custom.refresh.PtrDefaultHandler;
import ddw.com.richang.custom.refresh.PtrFrameLayout;
import ddw.com.richang.custom.refresh.PtrHandler;
import ddw.com.richang.model.RiGetAlbums;

/**
 * 精选
 * Created by zzp on 2016/6/20.
 */
public class ChoicenessFragment extends BaseFragment {

    @ViewInject(R.id.choiceness_recyclerView)
    RecyclerView mRecyclerView;

    @ViewInject(R.id.choiceness_ptr_refresh)
    PtrClassicFrameLayout mRefreshLayout;

    /**
     * 列表数据
     */
    private ArrayList<RiGetAlbums> mListGetAlbums = new ArrayList<>();
    private ChoicenessAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAlbumSData("3", "2");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.column_choiceness_layout, container, false);
        x.view().inject(this, view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        refreshHandler();

        mAdapter = new ChoicenessAdapter(getActivity(),
                mListGetAlbums);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

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

                        getAlbumSData("3", "2");

                        mRefreshLayout.refreshComplete();
                    }
                }, 0);
            }
        });


    }


    /**
     * 获取当前界面数据
     * http://appv2.myrichang.com/Home/Industry/getAlbums?ct_id=3&start_id=2
     *
     * @param ct_id    起始专辑编号
     * @param start_id 城市编号
     */
    private void getAlbumSData(String ct_id, String start_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getAlbums);
        params.addBodyParameter("ct_id", ct_id);
        params.addBodyParameter("start_id", start_id);
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
                                    List<RiGetAlbums> riGetAlbumses = JSON.parseArray
                                            (jsonArray.toString(), RiGetAlbums.class);
                                    mListGetAlbums.addAll(riGetAlbumses);

                                    mAdapter.notifyDataSetChanged();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                });
    }


}
