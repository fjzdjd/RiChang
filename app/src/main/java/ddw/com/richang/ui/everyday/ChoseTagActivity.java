package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
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
import ddw.com.richang.model.RiGetTagList;
import ddw.com.richang.util.LogN;

/**
 * 选择标签
 * Created by zzp on 2016/5/26.
 */
public class ChoseTagActivity extends BaseActivity {

    /**
     * 我的标签GridView
     */
    private GridView mGridViewMineTags;

    /**
     * 所有标签GridView
     */
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
    private ChoseAdapter mAllChoseAdapter;
    private ChoseAdapter mMineChoseAdapter;
    private String mUserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.everyday_tag_activity_layout_copy);
        initWidgets();
        mUserID = SharePreferenceManager.getInstance().getString(ConstantData.USER_ID, "");

        mAllChoseAdapter = new ChoseAdapter(mAllTagList, 1);
        mGridViewAllTags.setAdapter(mAllChoseAdapter);

        mMineChoseAdapter = new ChoseAdapter(mMineTagList, 0);
        mGridViewMineTags.setAdapter(mMineChoseAdapter);

        //获取我的标签
        getMineTags(mUserID);


    }


    /**
     * 获取需要提交的标签 并转化为数组
     *
     * @param list 我的标签
     * @return String[]
     */
    private String getMineTags(List<RiGetTagList> list) {

        String postTagId = "[";
        for (int i = 0; i < list.size(); i++) {
            if (i > 0)
                postTagId += ",";
            postTagId += list.get(i).getTag_id();
        }
        postTagId += "]";
        return postTagId;
    }

    /**
     * 初始化界面
     */

    private void initWidgets() {

        mGridViewAllTags = (GridView) findViewById(R.id
                .chose_tag_gdw_all);

        mGridViewMineTags = (GridView) findViewById(R.id
                .chose_tag_gdw_mine);

        mPreserve = (TextView) findViewById(R.id.chose_tag_txt_preserve);

        //保存我的标签
        mPreserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTags(mUserID, getMineTags(mMineTagList));
            }
        });

        mGridViewAllTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMineTagList.add(mAllTagList.get(position));
                mAllTagList.remove(position);
                mAllChoseAdapter.setListData(mAllTagList);
                mMineChoseAdapter.setListData(mMineTagList);

            }
        });

        mGridViewMineTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAllTagList.add(mMineTagList.get(position));
                mMineTagList.remove(position);
                mMineChoseAdapter.setListData(mMineTagList);
                mAllChoseAdapter.setListData(mAllTagList);
            }
        });

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

                            List<RiGetTagList> getTagLists = JSON.parseArray(jsonArray.toString()
                                    , RiGetTagList.class);


                            if (getTagLists.size() == mMineTagList.size()) {
                                mAllTagList.clear();
                            } else {
                                //移除我的标签
                                for (RiGetTagList s2 : getTagLists) {
                                    boolean flag = false;
                                    for (RiGetTagList s1 : mMineTagList) {
                                        if (s2.equals(s1)) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (!flag) {
                                        mAllTagList.add(s2);

                                    }
                                }

                            }
                            mAllChoseAdapter.setListData(mAllTagList);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 提交选择的标签值
     *
     * @param usr_id   用户id
     * @param postTags tags[]
     */
    private void setTags(String usr_id, String postTags) {
        showWaitDialog(R.color.white);
        RequestParams params = new RequestParams(InterFace.getInstance().setTags);
        params.addBodyParameter("usr_id", usr_id);
        params.addBodyParameter("tags[]", postTags);
        LogN.d(this, params.toString());
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
                                String msg = jsonObject.optString("msg");
                                if (code.equals(ConstantData.CODE)) {

                                    finish();

                                } else {

                                    Toast.makeText(ChoseTagActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                                    mMineChoseAdapter.setListData(mMineTagList);
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

        /**
         * 判断所属 1为所有标签
         */
        private int mType;

        /**
         * 适配器
         *
         * @param mList 数据
         * @param type  1,所有；0，我的
         */
        public ChoseAdapter(List<RiGetTagList> mList, int type) {
            this.mList = mList;
            this.mType = type;
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

            if (mType == 1) {
                viewHolder.tagName.setBackground(getDrawable(R.drawable.tagall));
                viewHolder.tagName.setTextColor(getResources().getColor(R.color.font_black_1));
            } else {
                viewHolder.tagName.setBackground(getDrawable(R.drawable.tagme));
                viewHolder.tagName.setTextColor(getResources().getColor(R.color.white));
            }

            viewHolder.tagName.setText(mList.get(position).getTag_name());

            return convertView;
        }
    }

}
