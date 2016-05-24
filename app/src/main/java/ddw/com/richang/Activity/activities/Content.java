package ddw.com.richang.Activity.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cn.sharesdk.framework.ShareSDK;
import ddw.com.richang.Activity.banner.Planhint;
import ddw.com.richang.model.ACTUlt;
import ddw.com.richang.model.PLAN;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.imgloader.bitmap.NormalView;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.controller.shareSDK.cn.sharesdk.onekeyshare.OnekeyShare;
import ddw.com.richang.controller.data.imgloader.bitmap.CircleView;
import ddw.com.richang.controller.data.imgloader.bitmap.GuassView;
import ddw.com.richang.controller.view.layout.scrollview.ObScrollView;
import ddw.com.richang.controller.view.layout.scrollview.ScrollViewListener;

public class Content extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;
    private ArrayList<Bitmap> arrBitmap =new ArrayList<Bitmap>();//图片缓冲池
    private ACTUlt act;//活动模型
    private boolean add=false;//标记是否在这个页面上活动被加入到行程
    private boolean uncollect=false;//标记是否在这个页面上活动被取消收藏
    private PLAN plan;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        final long actId=getIntent().getLongExtra("act_id",-1);
        //从网络获取
        if(actId>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    act=new ACTUlt(actId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showActivity();
                        }
                    });
                }
            }).start();
        }else{
            Toast.makeText(Content.this,"活动错误",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=arrBitmap && arrBitmap.size()>0) for(Bitmap bitmap:arrBitmap){
            if(null!=bitmap && ! bitmap.isRecycled()) bitmap.recycle();
        }
    }

    @Override
    public void onBackPressed() {
        if(add){
            Intent intent=plan.toIntent();
            intent.putExtra("joinplan",true);
            setResult(RESULT_OK,intent);
        }
        if(uncollect){
            Intent intent=new Intent();
            intent.putExtra("uncollect",true);
            intent.putExtra("act_id",act.getId());
            setResult(RESULT_OK,intent);
        }
        finish();
        //super.onBackPressed();
    }

    private void showActivity(){
        if(200!=act.getStatus()){
            Toast.makeText(Content.this,act.getMessage(),Toast.LENGTH_SHORT).show();
            if(404==act.getStatus()) finish();
            return;
        }

        //标题
        TextView title=(TextView)findViewById(R.id.activity_title);
        title.setText(act.getTitle());
        ImageView back=(ImageView)findViewById(R.id.activity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //
        ObScrollView scrollView=(ObScrollView)findViewById(R.id.contentscroll);
        title.measure(0,0);
        final int hh=title.getMeasuredHeight();
        final TextView toptitle=(TextView)findViewById(R.id.contenttoptitle);
        final String oldtoptitle=toptitle.getText().toString();
        scrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObScrollView scrollView, int x, int y, int oldx, int oldy) {
                if(y>hh*0.8){
                    if(oldy<=hh*0.8) toptitle.setText(act.getTitle());
                }else{
                    if(oldy>hh*0.8) toptitle.setText(oldtoptitle);
                }
            }
        });
        //图片
        ImageView poster=(ImageView)findViewById(R.id.activity_poster);
        ImageView guass =(ImageView)findViewById(R.id.activity_guass);
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Content.this,Photo.class).putExtra("photo",act.getPoster()));
            }
        });
        String pic=act.getPoster()+"@!display";
        ImageLoader.getInstance().displayImage(pic,poster, NormalView.BITMAPDISPLAYER);
        ImageLoader.getInstance().displayImage(pic,guass,GuassView.BITMAPDISPLAYER);
        //标签
        TextView tags=(TextView)findViewById(R.id.activity_tag);
        tags.setText(act.getTagsStr());
        //发布者
        ImageView pubpic=(ImageView)findViewById(R.id.publisherpic);
        pubpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Content.this, Photo.class).putExtra("photo",act.pubpic));
            }
        });
        ImageLoader.getInstance().displayImage(act.pubpic+"@!profile",pubpic,CircleView.BITMAPDISPLAYER);
        ((TextView)findViewById(R.id.publishername)).setText(act.pubname);
        //时间
        TextView time = (TextView) findViewById(R.id.activity_time);
        if(act.getTime().length()>16) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(
                    Integer.parseInt(act.getTime().substring(0, 4)),
                    Integer.parseInt(act.getTime().substring(5, 7)) - 1,
                    Integer.parseInt(act.getTime().substring(8, 10))
            );
            String[] wd = new String[]{"日", "一", "二", "三", "四", "五", "六"};
            String week = wd[calendar.get(Calendar.DAY_OF_WEEK)-1];
            time.setText(act.getTime().substring(0, 10) + "  周" + week+" "+act.getTime().substring(11,16));
        }else time.setText(act.getTime());
        //详情
        final TextView place=(TextView)findViewById(R.id.activity_place);
        place.setText(act.getPlace());
        TextView size=(TextView)findViewById(R.id.activity_size);
        size.setText(act.getSize());
        TextView fee=(TextView)findViewById(R.id.activity_fee);
        fee.setText(act.getPay());
        fee.setText("免费");//这里1.0统一设置为免费
        //介绍
        TextView introduce=(TextView)findViewById(R.id.activity_introduce);
        introduce.setText(act.getDesc());
        //分享
        ImageView share=(ImageView)findViewById(R.id.sharebtn);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url="http://myrichang.com/activity.php?id="+act.getId();
                ShareSDK.initSDK(Content.this);
                OnekeyShare oks=new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                oks.setTitle(act.getTitle());
                oks.setText(act.getDesc());
                oks.setImageUrl(act.getPoster()+"@!display");
                oks.setComment("");
                oks.setTitleUrl(url);
                oks.setUrl(url);
                oks.setSite(url);
                oks.setSiteUrl(url);
                oks.show(Content.this);
            }
        });
        //详情
//        final TextView getmore=(TextView)findViewById(R.id.actmore);
//        getmore.setText(">>更多内容<<");
//        if(act.getHtml()!=null && ! act.getHtml().equals("")){
//            getmore.setVisibility(View.VISIBLE);
//            getmore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(Content.this,MoreContent.class);
//                    intent.putExtra("html",act.getHtml());
//                    intent.putExtra("title",act.getTitle());
//                    startActivity(intent);
//                }
//            });
//        }
        if(act.getHtml()!=null && ! act.getHtml().equals("")){
            ((LinearLayout)findViewById(R.id.actmore)).setVisibility(View.VISIBLE);
            WebView webView=(WebView)findViewById(R.id.morecontent);
            String css="<head><meta charset='utf-8' /><meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\"/><style>*{font-size:14px;max-width:100%;word-break:break-all;padding:0px;margin:0px}\n#contentsss{line-height:150%;color:#646464}</style></head>";
            String head="";//"<h2>"+act.getTitle()+"</h2><hr />";
            String script="<script>window.onload=function(){content.adjust(document.getElementById(\"contentsss\").offsetHeight);}</script>";
            String showHtml=css+head+"<div id='contentsss'>"+act.getHtml()+"</div>"+script;
            webView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
            webView.loadData(showHtml, "text/html; charset=UTF-8", null);//这种写法可以正确解码
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new JScontent(this),"content");
        }





        //收藏
        LinearLayout lyCollect=(LinearLayout)findViewById(R.id.activity_addcollect);
        final TextView tvCollect=(TextView)findViewById(R.id.tvcollect);
        final ImageView showCollect=(ImageView)findViewById(R.id.activity_showcollect);
        showCollect.setImageResource(act.isCollected() ? R.drawable.collectionicon_selected : R.drawable.collection_icon);
        tvCollect.setText(act.isCollected() ? "已收藏" : "收藏");
        tvCollect.setTextColor(act.isCollected()?getResources().getColor(R.color.rorange):getResources().getColor(R.color.black));
        lyCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.getUSR().getID()>0) {
                    final boolean s = act.isCollected();
                    showCollect.setImageResource(!s ? R.drawable.collectionicon_selected : R.drawable.collection_icon);
                    act.changeCollected(Content.this, new Runnable() {
                        @Override
                        public void run() {
                            uncollect = !act.isCollected();
                            showCollect.setImageResource(act.isCollected() ? R.drawable.collectionicon_selected : R.drawable.collection_icon);
                            tvCollect.setText(act.isCollected() ? "已收藏" : "收藏");
                            tvCollect.setTextColor(act.isCollected()?getResources().getColor(R.color.rorange):getResources().getColor(R.color.black));
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Content.this, "网络错误", Toast.LENGTH_SHORT).show();
                            showCollect.setImageResource(act.isCollected() ? R.drawable.collectionicon_selected : R.drawable.collection_icon);
                            tvCollect.setText(act.isCollected() ? "已收藏" : "收藏");
                            tvCollect.setTextColor(act.isCollected() ? getResources().getColor(R.color.rorange) : getResources().getColor(R.color.black));
                        }
                    });
                }else Toast.makeText(Content.this, "请先登录后再收藏", Toast.LENGTH_SHORT).show();
            }
        });
        //行程+提醒我
        final TextView remind=(TextView)findViewById(R.id.contenthint);
        final LinearLayout planly=(LinearLayout)findViewById(R.id.addtoplan);
        planly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.getUSR().getID()>0) {
                    planly.setEnabled(false);
                    remind.setEnabled(false);
                    if (act.isPlaned()) {
                        Toast.makeText(getApplicationContext(), "已在行程列表中", Toast.LENGTH_SHORT).show();
                        planly.setEnabled(true);
                        remind.setEnabled(true);
                    } else {
                        plan = new PLAN(act);
                        act.addPlan(plan, Content.this, new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "已加入行程", Toast.LENGTH_SHORT).show();
                                add = true;
                                remind.setEnabled(true);
                                planly.setEnabled(true);
                            }
                        }, new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "加入行程失败", Toast.LENGTH_SHORT).show();
                                planly.setEnabled(true);
                                remind.setEnabled(true);
                            }
                        });
                    }
                }else Toast.makeText(Content.this, "请先登录后再加入行程", Toast.LENGTH_SHORT).show();
            }
        });
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.getUSR().getID()>0) {
                    Intent intent=new Intent(Content.this, Planhint.class);
                    intent.putExtra("hint1",act.hint1);
                    intent.putExtra("hint2",act.hint2);
                    intent.putExtra("hint3",act.hint3);
                    startActivityForResult(intent, Planhint.REQCODE);
                }else Toast.makeText(Content.this, "请先登录后再设置提醒", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(resultCode!=RESULT_OK) return;
        if(requestCode== Planhint.REQCODE){
            //行程+提醒我
            final TextView remind=(TextView)findViewById(R.id.contenthint);
            final LinearLayout planly=(LinearLayout)findViewById(R.id.addtoplan);
            remind.setEnabled(false);
            planly.setEnabled(false);
            act.hint1=data.getBooleanExtra("hint1",false);
            act.hint2=data.getBooleanExtra("hint2",false);
            act.hint3=data.getBooleanExtra("hint3",false);
            plan=new PLAN(act);
            //加入行程
            if(!act.isPlaned()){
                act.addPlan(plan, Content.this, new Runnable() {
                    @Override
                    public void run() {
                        //设置提醒
                        plan.submit(Content.this, new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"设置提醒成功",Toast.LENGTH_SHORT).show();
                                planly.setEnabled(true);
                                remind.setEnabled(true);
                                add=true;
                            }
                        }, new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"设置提醒失败",Toast.LENGTH_SHORT).show();
                                add=true;
                                planly.setEnabled(true);
                                remind.setEnabled(true);
                            }
                        });
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"加入行程失败",Toast.LENGTH_SHORT).show();
                        planly.setEnabled(true);
                        remind.setEnabled(true);
                    }
                });
            }else{
                plan.submit(Content.this, new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"设置提醒成功",Toast.LENGTH_SHORT).show();
                        planly.setEnabled(true);
                        remind.setEnabled(true);
                        add=true;
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"设置提醒失败",Toast.LENGTH_SHORT).show();
                        planly.setEnabled(true);
                        remind.setEnabled(true);
                    }
                });
            }
        }
    }





    class JScontent{
        private Context context;
        public JScontent(Context context){
            this.context=context;
        }

        @JavascriptInterface
        public void adjust(final int height){
            final WebView webView=(WebView)findViewById(R.id.morecontent);
            webView.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(height+12)*Config.SCALE);
                    params.setMargins(12*Config.SCALE,7*Config.SCALE,12*Config.SCALE,12*Config.SCALE);
                    webView.setLayoutParams(params);
                }
            });
        }
    }
}
