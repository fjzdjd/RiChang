package ddw.com.richang.ui.everyday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.controller.view.layout.scrollview.ObScrollView;
import ddw.com.richang.controller.view.layout.scrollview.ScrollViewListener;
import ddw.com.richang.custom.CustomUi.WarmAlertDialog;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiGetActivityDetail;
import ddw.com.richang.model.RiGetPopularComments;
import ddw.com.richang.util.DensityUtil;
import ddw.com.richang.util.StringUtils;

/**
 * 详情界面
 * Created by zzp on 2016/5/28.
 */
public class ContentDetailActivity extends BaseActivity {

    private ImageView mPosterImage;
    private ImageView mGlassImage;
    private TextView mContentTitle;
    private TextView mContentTags;
    private TextView mActivityHot;
    private TextView mActivityTime;
    private TextView mActivityLocation;
    private TextView mActivitySize;
    private TextView mActivityFare;
    private TextView mHeaderTitle;
    private WebView mActivityContentHtml;
    private TextView mPublisherName;
    private ImageView mPublisherPic;

    /**
     * 组合标签
     */
    private String buildTags = "#";
    private ObScrollView mScrollViewContent;
    private RelativeLayout mHeaderBackground;
    private TextView mViewMoreInformation;
    /**
     * 查看更多信息
     */
    private boolean loadingMoreInformationFlag = true;
    private ImageView mCollection;
    private String mUserId;
    private String ac_id;
    private RiGetActivityDetail mContentDetail;
    private TextView mRemindMe;

    private ImageView mUserPic;
    private TextView mUserName;
    private TextView mPublishTime;
    private TextView mTitle;
    private TextView mLikeNum;
    private ImageOptions options;
    private TextView mAddAgenda;
    private TextView mEnrollMine;

    @Override
    protected void onCreate(Bundle outState) {
        super.onCreate(outState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        showWaitDialog("", R.color.white);
        setContentView(R.layout.everyday_detail_activity_layout);

        options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.imggrey)
                .setLoadingDrawableId(R.mipmap.imggrey).setUseMemCache(true).setCircular(true)
                .build();
        mUserId = SharePreferenceManager.getInstance().getString(ConstantData.USER_ID, "");
        ac_id = getIntent().getStringExtra("ac_id");
        getActicityDetail(ac_id, mUserId);
        initWidgets();
        getPopularComments(ac_id);
    }

    /**
     * 初始化界面
     */
    private void initWidgets() {
        mPosterImage = (ImageView) findViewById(R.id.detail_img_content_poster);
        mGlassImage = (ImageView) findViewById(R.id.detail_img_glass);
        mContentTitle = (TextView) findViewById(R.id.detail_txt_content_title);
        mContentTags = (TextView) findViewById(R.id.detail_txt_tags);
        mActivityHot = (TextView) findViewById(R.id.detail_txt_people_watch);
        mActivityTime = (TextView) findViewById(R.id.detail_txt_time);
        mCollection = (ImageView) findViewById(R.id.detail_img_collect);
        mActivityLocation = (TextView) findViewById(R.id.detail_txt_location);
        mActivitySize = (TextView) findViewById(R.id.detail_txt_size);
        mActivityFare = (TextView) findViewById(R.id.detail_txt_fare);
        mAddAgenda = (TextView) findViewById(R.id.detail_txt_add_agenda);
        mEnrollMine = (TextView) findViewById(R.id.detail_txt_enroll);
        TextView mFocusPublisher = (TextView) findViewById(R.id.detail_txt_focus_publisher);
        mRemindMe = (TextView) findViewById(R.id.detail_txt_reminder);
        mHeaderTitle = (TextView) findViewById(R.id.detail_txt_header_title);
        mPublisherName = (TextView) findViewById(R.id.detail_txt_publisher_name);
        mHeaderBackground = (RelativeLayout) findViewById(R.id.detail_rlt_header);
        mActivityContentHtml = (WebView) findViewById(R.id.detail_txt_activity_content);
        mPublisherPic = (ImageView) findViewById(R.id.detail_img_publisher_pic);
        mScrollViewContent = (ObScrollView) findViewById(R.id.detail_slv_content);
        mViewMoreInformation = (TextView) findViewById(R.id.detail_txt_more_information);
        TextView mViewMoreComment = (TextView) findViewById(R.id.detail_txt_comment_more);
        mViewMoreComment.setText(Html.fromHtml("<u>" + "查看或添加评论" + "</u>"));
        mViewMoreInformation.setText(Html.fromHtml("<u>" + "查看更多" + "</u>"));
        mViewMoreInformation.setOnClickListener(new DetailOnClick());
        mCollection.setOnClickListener(new DetailOnClick());
        mRemindMe.setOnClickListener(new DetailOnClick());
        mFocusPublisher.setOnClickListener(new DetailOnClick());
        mAddAgenda.setOnClickListener(new DetailOnClick());
        mEnrollMine.setOnClickListener(new DetailOnClick());
        mViewMoreComment.setOnClickListener(new DetailOnClick());
        mUserPic = (ImageView) findViewById(R.id.user_img_comment);
        mUserName = (TextView) findViewById(R.id.user_txt_name);
        mPublishTime = (TextView) findViewById(R.id.user_txt_time);
        mTitle = (TextView) findViewById(R.id.user_txt_title);
        mLikeNum = (TextView) findViewById(R.id.user_txt_likeNum);


    }

    /**
     * 头部栏目标题和颜色渐变
     *
     * @param data RiGetActivityDetail
     */
    private void changePageHeaderTitleAndColor(final RiGetActivityDetail data) {
        mContentTitle.measure(0, 0);
        final int mIntTitleHeight = mContentTitle.getMeasuredHeight();
        final String mStringHeaderTitle = mHeaderTitle.getText().toString().trim();
        mScrollViewContent.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > mIntTitleHeight * 0.8) {
                    if (oldy <= mIntTitleHeight * 0.8)
                        mHeaderTitle.setGravity(Gravity.START);
                    mHeaderTitle.setText(data.getData().getAc_title());
                    mHeaderBackground.setBackgroundResource(R.color.mainColor);
                } else {
                    if (oldy > mIntTitleHeight * 0.8)
                        mHeaderTitle.setText(mStringHeaderTitle);
                    mHeaderBackground.setBackgroundResource(R.color.transparent);
                }
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
     * 获取热门评论
     *
     * @param ac_id 活动编号
     */
    private void getPopularComments(String ac_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().getPopularComments);
        params.addBodyParameter("ac_id", ac_id);
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    List<RiGetPopularComments> riGetPopularComments = JSON
                                            .parseArray(jsonArray.toString(),
                                                    RiGetPopularComments.class);
                                    if (StringUtils.isEmpty(riGetPopularComments.get(0)
                                            .getComment_id())) {
                                        findViewById(R.id.comment_txt_visible).setVisibility(View
                                                .VISIBLE);
                                        findViewById(R.id.comment_llt_visible).setVisibility(View
                                                .GONE);
                                        mUserPic.setVisibility(View.GONE);
                                    } else {
                                        x.image().bind(mUserPic, riGetPopularComments.get(0)
                                                .getUsr_pic(), options);

                                        mUserName.setText(riGetPopularComments.get(0).getUsr_name
                                                ());

                                        mPublishTime.setText(riGetPopularComments.get(0)
                                                .getComment_time());

                                        mTitle.setText(riGetPopularComments.get(0)
                                                .getComment_content
                                                        ());

                                        mLikeNum.setText(riGetPopularComments.get(0)
                                                .getComment_praise_num());
                                    }

                                } else {
                                    findViewById(R.id.comment_txt_visible).setVisibility(View
                                            .VISIBLE);
                                    findViewById(R.id.comment_llt_visible).setVisibility(View.GONE);
                                    mUserPic.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                });
    }


    /**
     * 给对话框添加文字
     *
     * @return
     */
    private TextView getTextView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txt = new TextView(ContentDetailActivity.this);
        txt.setLayoutParams(params);
        txt.setTextSize(16);
        txt.setPadding(36, 15, 36, 0);
        txt.setText(Html.fromHtml("恭喜你!报名成功,你可以把该活动" + "<font color='#FF850E'>加入行程</font>" +
                "中哦~"));
        return txt;
    }


    /**
     * 用户报名活动
     *
     * @param usr_id 用户id
     * @param ac_id  活动id
     */
    private void enrollActivity(String usr_id, final String ac_id) {
        RequestParams params = new RequestParams(InterFace.getInstance().enrollActivity);
        params.addBodyParameter("usr_id", usr_id);
        params.addBodyParameter("ac_id", ac_id);
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

                                    TextView view = getTextView();
                                    WarmAlertDialog.getInstance().initDialog
                                            (ContentDetailActivity.this, "提示",
                                                    view, new View.OnClickListener() {

<<<<<<< HEAD
                                                        @Override
                                                        public void onClick(View v) {

                                                            joinTrip(mUserId, ac_id, "1");

                                                        }
                                                    });
=======

                                } else {

                                    Toast.makeText(ContentDetailActivity.this,"报名失败!",Toast.LENGTH_SHORT).show();
>>>>>>> origin/master

                                } else {
                                    Toast.makeText(ContentDetailActivity.this, "报名失败,请查看是否重复报名!",
                                            Toast
                                                    .LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }

                });
    }


    /**
     * 加入行程
     *
     * @param usr_id  用户id
     * @param ac_id   活动id
     * @param op_type 加入行程（1）
     */
    private void joinTrip(String usr_id, String ac_id, String op_type) {
        RequestParams params = new RequestParams(InterFace.getInstance().joinPlan);
        params.addBodyParameter("usr_id", usr_id);
        params.addBodyParameter("ac_id", ac_id);
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
                                if (code.equals(ConstantData.CODE)) {
                                    Toast.makeText(ContentDetailActivity.this, "加入成功", Toast
                                            .LENGTH_SHORT).show();
                                    mAddAgenda.setText("已加入行程");
                                } else {
                                    Toast.makeText(ContentDetailActivity.this, "加入失败", Toast
                                            .LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }

                });


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
                                if (code.equals(ConstantData.CODE)) {

                                    mContentDetail = JSON.parseObject(result,
                                            RiGetActivityDetail.class);

                                    setWidgetDatas(mContentDetail);

                                    changePageHeaderTitleAndColor(mContentDetail);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissWaitDialog();
                        }
                    }
                });


    }

    /**
     * 给组件赋值
     *
     * @param data 数据源
     */
    private void setWidgetDatas(RiGetActivityDetail data) {

        mContentTitle.setText(data.getData().getAc_title());
        if (mContentDetail.getData().getPlan().equals("1"))
            mAddAgenda.setText("已加入行程");
        for (int i = 0; i < data.getData().getAc_tags().size(); i++) {
            if (!StringUtils.isEmpty(data.getData().getAc_tags().get(i).getTag_name()))
                buildTags += data.getData().getAc_tags().get(i).getTag_name() + " ";
        }

        mContentTags.setText(buildTags);

        if (data.getData().getAc_collect().equals("1")) {
            mCollection.setImageResource(R.mipmap.collect_icon_focus);
        } else {
            mCollection.setImageResource(R.mipmap.collect_icon);
        }

        mActivityHot.setText(" " + data.getData().getAc_read_num());

        String headerHtml = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><meta " +
                "name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no, " +
                "width=device-width\"> <style>* {  font-size: 12pt;  max-width: 100%;  " +
                "word-break: break-all;  padding: 0px;  margin: 0px  }  #contentJs { line-height:" +
                " 150%;  color: #646464  }</style></head><body>";

        String JavaScript = "<script>window.onload=function(){content.adjust(document" +
                ".getElementById(\"contentJs\").offsetHeight);}</script>";

        String footerHtml = "</body></html>";

        getHtmlCode(headerHtml + "<div id='contentJs'>" + data.getData().getAc_html() + "</div>" +
                JavaScript + footerHtml);


        //对背景图片进行裁剪
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(this, 2), DensityUtil.dip2px(this, 10))
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();

        x.image().bind(mGlassImage, data.getData().getAc_poster(), imageOptions);

        x.image().bind(mPosterImage, data.getData().getAc_poster());

        mActivityTime.setText(data.getData().getAc_time());

        mActivityLocation.setText(data.getData().getAc_place().trim());

        mActivitySize.setText(data.getData().getAc_size().trim());

        mActivityFare.setText(data.getData().getAc_pay().trim());

        x.image().bind(mPublisherPic, data.getData().getUsr_pic(), options);

        mPublisherName.setText(data.getData().getUsr_name());

        dismissWaitDialog();//关闭加载progressBar
    }

    /**
     * 通过webview展示内容
     *
     * @param data 获取网页源码
     */
    @SuppressLint("JavascriptInterface")
    private void getHtmlCode(String data) {
        WebSettings ws = mActivityContentHtml.getSettings();
        ws.setJavaScriptEnabled(true); // 设置支持javascript脚本
        ws.setAllowFileAccess(true); // 允许访问文件
        ws.setBuiltInZoomControls(false); // 设置显示缩放按钮
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setSupportZoom(true); //
        // * 用WebView显示图片，可使用这个参数
        // * 设置网页布局类型：
        // * 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
        // * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        // */
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setDefaultTextEncodingName("UTF-8"); // 设置文本编码
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置缓存模式

        //调用自定义JavaScript
        mActivityContentHtml.addJavascriptInterface(this, "content");

        mActivityContentHtml.setWebViewClient(new WebViewClientHtml(data));

        mActivityContentHtml.loadData(data, "text/html; charset=UTF-8", null);


    }

    /**
     * 活动收藏
     *
     * @param usr_id  用户id
     * @param ac_id   活动id
     * @param op_type 收藏（1）或取消收藏（2）
     */
    private void setActivityCollect(String usr_id, String ac_id, String op_type) {
        RequestParams params = new RequestParams(InterFace.getInstance().setCollection);
        params.addBodyParameter("usr_id", usr_id);
        params.addBodyParameter("ac_id", ac_id);
        params.addBodyParameter("op_type", op_type);
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
                        Toast.makeText(ContentDetailActivity.this, "操作失败", Toast
                                .LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Toast.makeText(ContentDetailActivity.this, "操作失败", Toast
                                .LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinished() {
                        if (!hasError && result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = jsonObject.optString("code");
                                if (code.equals(ConstantData.CODE)) {

                                    if (mContentDetail.getData().getAc_collect().equals("1")) {
                                        mCollection.setImageResource(R.mipmap.collect_icon);
                                        Toast.makeText(ContentDetailActivity.this, "取消收藏", Toast
                                                .LENGTH_SHORT).show();
                                        mContentDetail.getData().setAc_collect("2");
                                    } else {
                                        mCollection.setImageResource(R.mipmap.collect_icon_focus);
                                        Toast.makeText(ContentDetailActivity.this, "收藏成功", Toast
                                                .LENGTH_SHORT).show();
                                        mContentDetail.getData().setAc_collect("1");
                                    }

                                } else {

                                    Toast.makeText(ContentDetailActivity.this, "操作失败", Toast
                                            .LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ContentDetailActivity.this, "操作失败", Toast
                                    .LENGTH_SHORT).show();
                        }

                    }
                });


    }

    /**
     * 详情点击事件
     */
    private class DetailOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                //显示更多信息
                case R.id.detail_txt_more_information:
                    if (loadingMoreInformationFlag) {
                        mActivityContentHtml.setVisibility(View.VISIBLE);
                        loadingMoreInformationFlag = false;
                    } else {
                        mActivityContentHtml.setVisibility(View.GONE);
                        loadingMoreInformationFlag = true;
                    }
                    break;

                //收藏
                case R.id.detail_img_collect:
                    if (mContentDetail.getData().getAc_collect().equals("1")) {
                        setActivityCollect(mUserId, ac_id, "2");
                    } else {
                        setActivityCollect(mUserId, ac_id, "1");
                    }
                    break;

                //提醒我
                case R.id.detail_txt_reminder:
                    intent.putExtra("remindHour", mContentDetail.getData().getAc_time());
                    riChangActivityManager.startNextActivity(intent, RemindTimeActivity.class);

                    break;

                //关注
                case R.id.detail_txt_focus_publisher:
                    intent.putExtra("publisher_id", mContentDetail.getData().getUsr_id());
                    riChangActivityManager.startNextActivity(intent, PublisherDetailActivity.class);

                    break;

                //加入行程
                case R.id.detail_txt_add_agenda:

                    if (!mContentDetail.getData().getPlan().equals("1"))
                        joinTrip(mUserId, ac_id, "1");

                    break;

                //我要报名
                case R.id.detail_txt_enroll:

                    enrollActivity(mUserId, ac_id);

                    break;

                //查看和添加评论
                case R.id.detail_txt_comment_more:

                    intent.putExtra("ac_id", ac_id);
                    riChangActivityManager.startNextActivity(intent, AllCommentsActivity.class);

                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 自定义webView
     */
    private class WebViewClientHtml extends WebViewClient {

        String data;

        /**
         * 自定义webView
         *
         * @param data 传入数据
         */
        public WebViewClientHtml(String data) {
            this.data = data;
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //文本中包含超链接需要用此方法
            view.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);

            Uri uri = Uri.parse(url.toString().trim());

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            startActivity(intent);

            return true;
        }
    }


}
