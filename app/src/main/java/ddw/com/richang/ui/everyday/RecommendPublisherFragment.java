package ddw.com.richang.ui.everyday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.adapter.PublisherAdapter;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.custom.refresh.PtrClassicFrameLayout;
import ddw.com.richang.custom.refresh.PtrDefaultHandler;
import ddw.com.richang.custom.refresh.PtrFrameLayout;
import ddw.com.richang.custom.refresh.PtrHandler;
import ddw.com.richang.model.RiGetPublisherRecommend;
import ddw.com.richang.util.LogN;

/**
 * 发布者推荐
 * Created by zzp on 2016/6/27.
 */
public class RecommendPublisherFragment extends BaseFragment {

    private PtrClassicFrameLayout mRefreshLayout;

    private ArrayList<RiGetPublisherRecommend> mPublisherRecommends = new ArrayList<>();
    private ListView mListView;
    private PublisherAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //测试数据
        getPublisherRecommend("6");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.everyday_publisher_layout, container, false);

        initWidget(view);

        mAdapter = new PublisherAdapter(getActivity(),mPublisherRecommends);

        mListView.setAdapter(mAdapter);

        refreshHandler();

        return view;
    }

    private void initWidget(View view) {

        mRefreshLayout = (PtrClassicFrameLayout) view.findViewById(R.id
                .everyday_publisher_ptr_refresh);


        mListView = (ListView) view.findViewById(R.id.everyday_publisher_recyclerView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),PublisherDetailActivity.class);
                startActivity(intent);
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

                        //测试数据
                        getPublisherRecommend("6");

                        mRefreshLayout.refreshComplete();
                    }
                }, 0);
            }
        });


    }

    /**
     * 获取推荐的发布者列表
     *
     * @param usr_id 用户的id
     */
    private void getPublisherRecommend(String usr_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getPublisherRecommend);
        params.addBodyParameter("usr_id", usr_id);
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
                                    List<RiGetPublisherRecommend> riGetAlbumses = JSON.parseArray
                                            (jsonArray.toString(), RiGetPublisherRecommend.class);

                                    mPublisherRecommends.addAll(riGetAlbumses);

                                    mAdapter.setListData(mPublisherRecommends);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                });

    }
}
