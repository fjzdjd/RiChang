package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.adapter.EverydayAdapter;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiActivityRecommend;

/**
 * Created by zzp on 2016/7/12.
 */
@ContentView(R.layout.everyday_search_activity_layout)
public class SearchResultActivity extends BaseActivity {

    @ViewInject(R.id.everyday_search_list)
    ListView mList;

    private List<RiActivityRecommend> mRiGetUserActivityData = new ArrayList<>();
    private EverydayAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);

        getActivitySearch(getIntent().getStringExtra("keywords"), "", "", SharePreferenceManager
                .getInstance().getString(ConstantData.USER_CITY_ID, ""));


        mAdapter = new EverydayAdapter(SearchResultActivity.this, mRiGetUserActivityData);
        mList.setAdapter(mAdapter);
    }


    /**
     * 关闭当前界面
     *
     * @param v 布局中的点击事件
     */
    public void closeCurrentWin(View v) {
        finish();
    }

    /**
     * 活动搜索
     *
     * @param keywords 关键词
     * @param start_id 起始活动
     * @param num      数目
     * @param ct_id    城市id
     */
    private void getActivitySearch(String keywords, String start_id, String num, String ct_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getSearch);
        params.addBodyParameter("keywords", keywords);
        params.addBodyParameter("start_id", start_id);
        params.addBodyParameter("num", num);
        params.addBodyParameter("ct_id", ct_id);
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
                                if (code.equals("200")) {

                                    List<RiActivityRecommend> riActivity = JSON.parseArray
                                            (jsonArray.toString(), RiActivityRecommend.class);

                                    mRiGetUserActivityData.addAll(riActivity);

                                    mAdapter.setListData(mRiGetUserActivityData);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                });

    }
}
