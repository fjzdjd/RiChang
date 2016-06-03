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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.controller.view.layout.scrollview.ObScrollView;
import ddw.com.richang.controller.view.layout.scrollview.ScrollViewListener;
import ddw.com.richang.custom.CustomUi.CustomCircleImageView;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.model.RiGetActivityDetail;
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
    private CustomCircleImageView mPublisherPic;

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

    @Override
    protected void onCreate(Bundle outState) {
        super.onCreate(outState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.everyday_detail_activity_layout);
        String ac_id = getIntent().getStringExtra("ac_id");
        getActicityDetail(ac_id,
                SharePreferenceManager.getInstance().getString(ConstantData.USER_ID, ""));
        initWidgets();

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
        mActivityLocation = (TextView) findViewById(R.id.detail_txt_location);
        mActivitySize = (TextView) findViewById(R.id.detail_txt_size);
        mActivityFare = (TextView) findViewById(R.id.detail_txt_fare);
        mHeaderTitle = (TextView) findViewById(R.id.detail_txt_header_title);
        mPublisherName = (TextView) findViewById(R.id.detail_txt_publisher_name);
        mHeaderBackground = (RelativeLayout) findViewById(R.id.detail_rlt_header);
        mActivityContentHtml = (WebView) findViewById(R.id.detail_txt_activity_content);
        mPublisherPic = (CustomCircleImageView) findViewById(R.id.detail_img_publisher_pic);
        mScrollViewContent = (ObScrollView) findViewById(R.id.detail_slv_content);
        mViewMoreInformation = (TextView) findViewById(R.id.detail_txt_more_information);
        TextView mViewMoreComment = (TextView) findViewById(R.id.detail_txt_comment_more);
        mViewMoreComment.setText(Html.fromHtml("<u>" + "查看更多" + "</u>"));
        mViewMoreInformation.setText(Html.fromHtml("<u>" + "查看更多" + "</u>"));
        mViewMoreInformation.setOnClickListener(new DetailOnClick());

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

                                    RiGetActivityDetail mContentDetail = JSON.parseObject(result,
                                            RiGetActivityDetail.class);
                                    setWidgetDatas(mContentDetail);

                                    changePageHeaderTitleAndColor(mContentDetail);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

        for (int i = 0; i < data.getData().getAc_tags().size(); i++) {
            if (!StringUtils.isEmpty(data.getData().getAc_tags().get(i).getTag_name()))
                buildTags += data.getData().getAc_tags().get(i).getTag_name() + " ";
        }

        mContentTags.setText(buildTags);

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

        x.image().bind(mPosterImage, data.getData().getAc_poster());

        //对背景图片进行裁剪
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(this, 2), DensityUtil.dip2px(this, 2))
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();

        x.image().bind(mGlassImage, data.getData().getAc_poster(), imageOptions);

        mActivityTime.setText(data.getData().getAc_time());

        mActivityLocation.setText(data.getData().getAc_place().trim());

        mActivitySize.setText(data.getData().getAc_size().trim());

        mActivityFare.setText(data.getData().getAc_pay().trim());

        x.image().bind(mPublisherPic, data.getData().getUsr_pic());

        mPublisherName.setText(data.getData().getUsr_name());


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
     * 详情点击事件
     */
    private class DetailOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
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

    /**
     * 热门评论
     */
    private class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
