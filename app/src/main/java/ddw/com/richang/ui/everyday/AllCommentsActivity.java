package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import ddw.com.richang.adapter.AllCommentAdapter;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.custom.refresh.PtrClassicFrameLayout;
import ddw.com.richang.model.RiGetPopularComments;

/**
 * Created by zzp on 2016/7/15.
 */
@ContentView(R.layout.everyday_comments_layout)
public class AllCommentsActivity extends BaseActivity {

    @ViewInject(R.id.comment_ptr_refresh)
    PtrClassicFrameLayout mRefreshLayout;

    @ViewInject(R.id.comment_recyclerView)
    RecyclerView mRecyclerView;

    private List<RiGetPopularComments> mCommentses = new ArrayList<>();
    private AllCommentAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);

        String ac_id = getIntent().getStringExtra("ac_id");

        //测试数据
        getAllComments("1320", "2");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(AllCommentsActivity.this);

        mAdapter = new AllCommentAdapter(AllCommentsActivity.this,mCommentses);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);


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
     * 有标题点击事件
     *
     * @param v 布局中的点击事件
     */
    public void navRight(View v) {

    }

    /**
     * 获取活动的所有评论
     *
     * @param ac_id    活动编号
     * @param start_id 起始评论id（0表示第一次获取）
     */
    private void getAllComments(String ac_id, String start_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getAllComments);
        params.addBodyParameter("ac_id", ac_id);
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
                                if (code.equals(ConstantData.CODE)) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    List<RiGetPopularComments> riGetPopularComments = JSON
                                            .parseArray(jsonArray.toString(),
                                                    RiGetPopularComments.class);

                                    mCommentses.addAll(riGetPopularComments);

                                    mAdapter.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(AllCommentsActivity.this, "获取评论失败", Toast
                                            .LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }

                });
    }

}
