package ddw.com.richang.ui.column;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
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
import ddw.com.richang.adapter.ChoicenessDetailAdapter;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.model.RiGetAlbumnAcs;

/**
 * Created by zzp on 2016/6/22.
 */
@ContentView(R.layout.column_choiceness_detail_layout)
public class ChoicenessDetailActivity extends BaseActivity {

    @ViewInject(R.id.choiceness_detail_recyclerView)
    RecyclerView mRecyclerView;

    @ViewInject(R.id.choiceness_detail_txt_title)
    TextView mTitle;
    /**
     * 当前界面数据
     */
    private ArrayList<RiGetAlbumnAcs> mArrayListData = new ArrayList<>();
    private ChoicenessDetailAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);

        String album_id = getIntent().getStringExtra("album_id");
        getAlbumAcs(album_id);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new ChoicenessDetailAdapter(this,mArrayListData);
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
     * http://appv2.myrichang.com/Home/Industry/getAlbumAcs?album_id=1
     * 获取精选 详细信息
     *
     * @param album_id id
     */
    private void getAlbumAcs(String album_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getAlbumAcs);
        params.addBodyParameter("album_id", album_id);
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

                                    List<RiGetAlbumnAcs> riGetAlbumnAcses = JSON.parseArray
                                            (jsonArray.toString(), RiGetAlbumnAcs.class);
                                    mArrayListData.addAll(riGetAlbumnAcses);

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
