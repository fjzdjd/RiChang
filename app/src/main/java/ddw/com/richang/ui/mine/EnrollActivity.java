package ddw.com.richang.ui.mine;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.custom.CustomUi.WarmAlertDialog;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiGetUserActivity;

/**
 * 我的报名
 * Created by zzp on 2016/6/7.
 */
@ContentView(R.layout.mine_enroll_activity_layout)
public class EnrollActivity extends BaseActivity {

    @ViewInject(R.id.enroll_slv_list)
    ListView mSwipeListView;

    @ViewInject(R.id.enroll_txt_setting)
    TextView mDelete;

    private List<RiGetUserActivity> mRiGetUserActivityData = new ArrayList<>();
    private MineEnrollAdapter mAdapter;

    /**
     * 初始化编辑参数
     */
    private boolean delete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        x.view().inject(this);

        getUserEnrollActivity(SharePreferenceManager.getInstance().getString(ConstantData
                .USER_ID, ""), "1");
        mAdapter = new MineEnrollAdapter(mRiGetUserActivityData);
        mSwipeListView.setAdapter(mAdapter);

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
     * 编辑当前活动，删除
     *
     * @param v 布局中的点击事件
     */
    public void editMyEnroll(View v) {

        if (delete) {
            delete = false;
            mDelete.setText("编辑");
            mDelete.setTextColor(getResources().getColor(R.color.mainGrayTxtColor));
        } else {
            delete = true;
            mDelete.setText("完成");
            mDelete.setTextColor(getResources().getColor(R.color.red));
        }

        mAdapter.notifyDataSetChanged();
    }


    /**
     * 获取用户参与的活动
     *
     * @param usr_id  用户id
     * @param op_type 1参加的，2收藏的，3发布的、4报名的
     */
    private void getUserEnrollActivity(String usr_id, String op_type) {
        RequestParams params = new RequestParams(InterFace.getInstance().getUsrActivity);
        params.addBodyParameter("usr_id", usr_id);
        params.addBodyParameter("op_type", op_type);
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

                                    List<RiGetUserActivity> riGetUserActivities = JSON.parseArray
                                            (jsonArray.toString(), RiGetUserActivity.class);

                                    mRiGetUserActivityData.addAll(riGetUserActivities);

                                    mAdapter.setListDatas(mRiGetUserActivityData);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delete = false;
    }

    private TextView getTextView(String str) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txt = new TextView(EnrollActivity.this);
        txt.setLayoutParams(params);
        txt.setLines(1);
        txt.setTextSize(16);
        txt.setPadding(36, 15, 36, 0);
        txt.setText(str);
        return txt;
    }

    /**
     * 适配器
     */
    private class MineEnrollAdapter extends BaseAdapter {

        private List<RiGetUserActivity> mList;


        public MineEnrollAdapter(List<RiGetUserActivity> List) {
            this.mList = List;

        }


        private void setListDatas(List<RiGetUserActivity> List) {
            this.mList = List;
            notifyDataSetChanged();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            convertView = LayoutInflater.from(EnrollActivity.this).inflate(R.layout
                    .item_mine_enroll_layout, parent, false);

            viewHolder = new ViewHolder();
            x.view().inject(viewHolder, convertView);

            RiGetUserActivity dataBean = mList.get(position);

            viewHolder.mTitle.setText(dataBean.getAc_title());
            viewHolder.mPlace.setText(dataBean.getAc_place());
            viewHolder.mTime.setText(dataBean.getAc_time());
            x.image().bind(viewHolder.mPoster, dataBean.getAc_poster());

            //加入行程
            viewHolder.mAddOrRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewHolder.mAddOrRemove.getText().toString().equals("加入行程")) {

                        Toast.makeText(EnrollActivity.this, "ddd", Toast.LENGTH_SHORT).show();

                    } else {

                        TextView view = getTextView("确定删除此活动吗?");
                        WarmAlertDialog.getInstance().initDialog(EnrollActivity.this, "提示",
                                view, new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        mList.remove(position);
                                        notifyDataSetChanged();

                                    }
                                });

                    }
                }
            });

            if (delete) {

                viewHolder.mAddOrRemove.setText("删除行程");

            } else {

                viewHolder.mAddOrRemove.setText("加入行程");

            }

            return convertView;
        }


        class ViewHolder {

            @ViewInject(R.id.enroll_item_img_poster)
            ImageView mPoster;

            @ViewInject(R.id.enroll_item_txt_title)
            TextView mTitle;

            @ViewInject(R.id.enroll_item_txt_time)
            TextView mTime;

            @ViewInject(R.id.enroll_item_txt_place)
            TextView mPlace;

            @ViewInject(R.id.enroll_item_txt_add)
            TextView mAddOrRemove;

        }
    }


}
