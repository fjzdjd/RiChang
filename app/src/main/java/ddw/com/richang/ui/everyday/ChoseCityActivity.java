package ddw.com.richang.ui.everyday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiGetCityList;
import ddw.com.richang.util.StringUtils;

/**
 * 选择城市
 * Created by zzp on 2016/5/25.
 */
public class ChoseCityActivity extends BaseActivity {

    /**
     * 选择城市 发送广播更新主界面数据
     */
    public static String mBroadcastRegistFlag = "choseCityActivity";

    private GridView mGridView;

    /**
     * 城市名称
     */
    private List<RiGetCityList> mJsonCityData = new ArrayList<>();
    private ChoseAdapter mChoseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.everyday_city_activity_layout);

        initWidgets();

        showWaitDialog("", R.color.transparent);

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

                Toast.makeText(x.app(), "网络访问失败!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "网络访问失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        String code = jsonObject.optString("code");

                        JSONArray jsonArray = jsonObject.optJSONArray("data");

                        if (code.equals("200")) {

                            List<RiGetCityList> getCityLists = JSON.parseArray(jsonArray.toString()
                                    , RiGetCityList.class);

                            mJsonCityData.addAll(getCityLists);

                            mChoseAdapter.setListData(mJsonCityData);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dismissWaitDialog();
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

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //更改城市
                SharePreferenceManager.getInstance().setString(ConstantData.USER_CITY_NAME,
                        mJsonCityData.get(position).getCt_name());

                SharePreferenceManager.getInstance().setString(ConstantData.USER_CITY_ID,
                        mJsonCityData.get(position).getCt_id());

                //发送应用内广播更新主界面数据
                Intent intent = new Intent();
                intent.setAction(mBroadcastRegistFlag);
                intent.putExtra(mBroadcastRegistFlag, "choseCity");
                LocalBroadcastManager.getInstance(ChoseCityActivity.this).sendBroadcast(intent);

                finish();
            }
        });
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

        private List<RiGetCityList> mList;

        public ChoseAdapter(List<RiGetCityList> mList) {
            this.mList = mList;
        }

        /**
         * 数据更新
         *
         * @param mList 数据值
         */
        private void setListData(List<RiGetCityList> mList) {
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

            if (!StringUtils.isEmpty(SharePreferenceManager.getInstance()
                    .getString(ConstantData.USER_CITY_NAME, ""))) {
                if (mList.get(position).getCt_name().equals(SharePreferenceManager.getInstance()
                        .getString(ConstantData.USER_CITY_NAME, ""))) {
                    viewHolder.location.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.location.setVisibility(View.INVISIBLE);
                }
            }
            x.image().bind(viewHolder.cityPic, InterFace.getInstance().picDomain +
                    "/img/city/" +
                    mList.get(position).getCt_id() + ".png");
            viewHolder.cityName.setText(mList.get(position).getCt_name());

            return convertView;
        }
    }
}
