package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.model.GetCityList;

/**
 * 选择城市
 * Created by zzp on 2016/5/25.
 */
public class ChoseCityActivity extends BaseActivity {

    private GridView mGridView;
    /**
     * 城市名称
     */
    private List<GetCityList> mJsonCityData = new ArrayList<>();
    private ChoseAdapter mChoseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.everyday_city_activity_layout);

        initWidgets();

        mChoseAdapter = new ChoseAdapter(mJsonCityData);

        mGridView.setAdapter(mChoseAdapter);

        initData();
    }

    private void initData() {
        RequestParams params = new RequestParams(InterFace.getInstance().getCities);
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
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    // ...
                } else { // 其他错误
                    // ...
                }

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

                            List<GetCityList> getCityLists = JSON.parseArray(jsonArray.toString()
                                    , GetCityList.class);

                            mJsonCityData.addAll(getCityLists);

                            mChoseAdapter.setListData(mJsonCityData);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initWidgets() {
        ImageView mCloseWin = (ImageView) findViewById(R.id.chose_city_img_close);
        //关闭当前页面
        mCloseWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGridView = (GridView) findViewById(R.id.chose_city_gdv);
    }

    /**
     * 适配器布局
     */
    static class ViewHolder {

        private final ImageView cityPic;
        private final ImageView location;
        private final TextView cityName;

        ViewHolder(View view) {

            cityPic = (ImageView) view.findViewById(R.id.chose_item_cityImg);
            location = (ImageView) view.findViewById(R.id.chose_item_img_location);
            cityName = (TextView) view.findViewById(R.id.chose_item_txt_cityName);


        }

    }

    /**
     * 当前item适配器
     */
    private class ChoseAdapter extends BaseAdapter {

        private HashMap<Integer, View> viewChache = new HashMap<>();

        private List<GetCityList> mList;

        public ChoseAdapter(List<GetCityList> mList) {
            this.mList = mList;
        }

        /**
         * 数据更新
         *
         * @param mList 数据值
         */
        private void setListData(List<GetCityList> mList) {
            this.mList = mList;
            this.notifyDataSetChanged();
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (viewChache.get(position) == null) {
                convertView = LayoutInflater.from(ChoseCityActivity.this).inflate(R.layout
                        .item_chose_city_layout, null);

                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
                viewChache.put(position, convertView);
            } else {
                convertView = viewChache.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }

            x.image().bind(viewHolder.cityPic, InterFace.getInstance().picDomain + "/img/city/" +
                    mList.get(position).getCt_id() + ".png");
            viewHolder.cityName.setText(mList.get(position).getCt_name());

            return convertView;
        }
    }
}
