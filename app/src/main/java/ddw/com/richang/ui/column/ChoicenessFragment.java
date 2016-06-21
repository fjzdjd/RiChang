package ddw.com.richang.ui.column;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.model.RiGetUserActivity;

/**
 * 精选
 * Created by zzp on 2016/6/20.
 */
public class ChoicenessFragment extends BaseFragment {

    @ViewInject(R.id.choiceness_recyclerView)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.column_choiceness_layout, container, false);
        x.view().inject(this, view);


        return view;
    }

    /**
     * http://appv2.myrichang.com/Home/Industry/getAlbums?ct_id=3&start_id=2
     * @param ct_id 起始专辑编号
     * @param start_id  城市编号
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

                                    List<RiGetUserActivity> riGetUserActivities = JSON.parseArray
                                            (jsonArray.toString(), RiGetUserActivity.class);



                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                });

    }


}
