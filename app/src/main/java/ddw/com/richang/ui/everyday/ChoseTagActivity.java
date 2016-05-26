package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import ddw.com.richang.controller.view.layout.HLView;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiGetCityList;
import ddw.com.richang.model.RiGetTagList;
import ddw.com.richang.util.StringUtils;

/**
 * 选择标签
 * Created by zzp on 2016/5/26.
 */
public class ChoseTagActivity extends BaseActivity {

//    private HLView mMineTags;
//    private HLView mAllTags;

    private GridView mGridViewMineTags;
    private GridView mGridViewAllTags;

    private TextView mPreserve;

    /**
     * 所有标签
     */
    private List<RiGetTagList> mAllTagList = new ArrayList<>();

    /**
     * 我的标签
     */
    private List<RiGetTagList> mMineTagList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
//        setContentView(R.layout.everyday_tag_activity_layout);
        setContentView(R.layout.everyday_tag_activity_layout_copy);

        initWidgets();


        getMineTags(SharePreferenceManager.getInstance().getString(ConstantData.USER_ID, ""));
    }

    /**
     * 初始化界面
     */
    private void initWidgets() {
//        mMineTags = (HLView) findViewById(R.id
//                .chose_tag_cl_mine);
//
//        mAllTags = (HLView) findViewById(R.id
//                .chose_tag_cl_all);

        mGridViewAllTags = (GridView) findViewById(R.id
                .chose_tag_gdw_all);
        mGridViewMineTags = (GridView) findViewById(R.id
                .chose_tag_gdw_mine);

        mPreserve = (TextView) findViewById(R.id.chose_tag_txt_preserve);

    }

    /**
     * 关闭当前页面
     *
     * @param v xml中的点击事件
     */
    public void closeCurrentWin(View v) {
        finish();
    }

    /**
     * 获取所有标签值,在取得我的标签之后执行
     */
    private void initData() {
        RequestParams params = new RequestParams(InterFace.getInstance().getAllTags);
        // 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
        params.setCacheMaxAge(1000 * 60);
        Callback.Cancelable cancelable
                = x.http().get(params, new Callback.CacheCallback<String>() {

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

                            List<RiGetTagList> getTagLists = JSON.parseArray(jsonArray.toString()
                                    , RiGetTagList.class);

                            mAllTagList.addAll(getTagLists);

                            //移除我的标签
                            for (int i = 0; i < mMineTagList.size(); i++) {
                                mAllTagList.remove(i);
                            }

                            ChoseAdapter choseAdapter = new ChoseAdapter(mAllTagList);
                            mGridViewAllTags.setAdapter(choseAdapter);
                            choseAdapter.setListData(mAllTagList);


//                            dynamicInitWidegts(mAllTags, mAllTagList, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    LinearLayout layout = (LinearLayout) v.getParent();
//                                    layout.setVisibility(View.GONE);
//                                }
//                            }, 0);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取用户设置的标签值
     *
     * @param user_id 用户id
     */
    private void getMineTags(String user_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getMyTags);
        params.addBodyParameter("usr_id", user_id);
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
                        dismissWaitDialog();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                        dismissWaitDialog();
                    }

                    @Override
                    public void onFinished() {
                        if (!hasError && result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);

                                String code = jsonObject.optString("code");

                                JSONArray jsonArray = jsonObject.optJSONArray("data");

                                if (code.equals("200")) {

                                    List<RiGetTagList> getTagLists = JSON.parseArray(jsonArray
                                                    .toString()
                                            , RiGetTagList.class);

                                    mMineTagList.addAll(getTagLists);


                                    ChoseAdapter choseAdapter = new ChoseAdapter(mMineTagList);
                                    mGridViewMineTags.setAdapter(choseAdapter);
                                    choseAdapter.setListData(mMineTagList);


//                                    dynamicInitWidegts(mMineTags, mMineTagList, new View
//                                            .OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                        }
//                                    }, 1);

                                }

                                initData();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }

    /**
     * 动态加载组件
     *
     * @param lyt             根布局
     * @param text            数据
     * @param onClickListener 点击事件
     * @param type            所有0,我的1
     */
    private void dynamicInitWidegts(HLView lyt, List<RiGetTagList> text, View
            .OnClickListener
            onClickListener, int type) {
        for (int i = 0; i < text.size(); i++) {
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            LinearLayout llt = new LinearLayout(ChoseTagActivity.this);
            llt.setPadding(16, 14, 16, 14);
            llt.setGravity(Gravity.CENTER);
            llt.setDividerPadding(20);
            TextView textView = new TextView(ChoseTagActivity.this);
            textView.setLayoutParams(llp);
            textView.setTextSize(15);

            if (type == 1) {
                textView.setBackgroundResource(R.drawable.tagme);
                textView.setTextColor(getResources().getColor(R.color.white));
            } else {
                textView.setBackgroundResource(R.drawable.tagall);
                textView.setTextColor(getResources().getColor(R.color.font_black_1));
            }

            textView.setText(text.get(i).getTag_name());

            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER);
            textView.setWidth(200);
            textView.setHeight(100);
            textView.setOnClickListener(onClickListener);
            llt.addView(textView);
            lyt.addView(llt);
        }
    }

    /**
     * 适配器布局
     */
    static class ViewHolder {

        private final TextView tagName;

        ViewHolder(View view) {


            tagName = (TextView) view.findViewById(R.id.chose_item_tag_txt);

        }

    }

    /**
     * 当前item适配器
     */
    private class ChoseAdapter extends BaseAdapter {

        private HashMap<Integer, View> viewChache = new HashMap<>();

        private List<RiGetTagList> mList;

        public ChoseAdapter(List<RiGetTagList> mList) {
            this.mList = mList;
        }

        /**
         * 数据更新
         *
         * @param mList 数据值
         */
        private void setListData(List<RiGetTagList> mList) {
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
                convertView = LayoutInflater.from(ChoseTagActivity.this).inflate(R.layout
                        .item_chose_tag_layout, null);

                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
                viewChache.put(position, convertView);
            } else {
                convertView = viewChache.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tagName.setText(mList.get(position).getTag_name());

            return convertView;
        }
    }

}
