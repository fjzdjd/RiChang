package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiActivityRecommend;
import ddw.com.richang.model.RiGetActivityDetail;
import ddw.com.richang.util.LogN;

/**
 * 详情界面
 * Created by zzp on 2016/5/28.
 */
public class ContentDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.everyday_detail_activity_layout);

        String ac_id = getIntent().getStringExtra("ac_id");

        getActicityDetail(ac_id,
                SharePreferenceManager.getInstance().getString(ConstantData.USER_ID, ""));

    }

    /**
     * 获取页面数据
     *
     * @param ac_id   活动id
     * @param user_id 用户id
     */
    private void getActicityDetail(String ac_id, String user_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getActivityContent);
        params.addBodyParameter("ac_id", ac_id);
        params.addBodyParameter("usr_id", user_id);
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
                                if (code.equals(ConstantData.CODE)) {

                                    RiGetActivityDetail mContentDetail = JSON.parseObject(result, RiGetActivityDetail.class);


                                    LogN.d(this, mContentDetail.toString());


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });

    }
}
