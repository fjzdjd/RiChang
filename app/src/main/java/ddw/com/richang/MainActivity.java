package ddw.com.richang;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ddw.com.richang.Activity.activities.ActivityList;
import ddw.com.richang.Activity.activities.Content;
import ddw.com.richang.Activity.banner.ChooseCity;
import ddw.com.richang.Activity.banner.ChooseTag;
import ddw.com.richang.Activity.banner.PlanInfo;
import ddw.com.richang.Activity.banner.Search;
import ddw.com.richang.Activity.banner.SetPlan;
import ddw.com.richang.Activity.banner.Setting;
import ddw.com.richang.Activity.myAccount.About;
import ddw.com.richang.Activity.myAccount.ChangeSomething;
import ddw.com.richang.Activity.myAccount.FeedBack;
import ddw.com.richang.Activity.pre.Login;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.ui.login.SplashActivity;
import ddw.com.richang.model.ACT;
import ddw.com.richang.model.PLAN;
import ddw.com.richang.model.common.IdNameMap;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.imgloader.bitmap.CircleView;
import ddw.com.richang.controller.data.imgloader.bitmap.DarkView;
import ddw.com.richang.controller.data.imgloader.bitmap.NormalView;
import ddw.com.richang.controller.data.imgloader.cache.disc.impl.UnlimitedDiskCache;
import ddw.com.richang.controller.data.imgloader.cache.disc.naming.Md5FileNameGenerator;
import ddw.com.richang.controller.data.imgloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import ddw.com.richang.controller.data.imgloader.core.DisplayImageOptions;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.controller.data.imgloader.core.ImageLoaderConfiguration;
import ddw.com.richang.controller.data.imgloader.core.assist.QueueProcessingType;
import ddw.com.richang.controller.data.imgloader.core.download.BaseImageDownloader;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.controller.view.layout.scrollview.ObScrollView;
import ddw.com.richang.controller.view.layout.scrollview.ScrollViewListener;
import ddw.com.richang.controller.view.list.ColumAdapter;
import ddw.com.richang.service.AlarmPlan;
import ddw.com.richang.service.Vibration;

public class MainActivity extends BaseActivity {
    boolean onFreshing = false;
    //城市变化观察者列表
    List<Runnable> onCityChangeListener = new ArrayList<Runnable>();
    //标签变化观察者列表
    List<Runnable> onTagChangeLister = new ArrayList<Runnable>();
    //首页下拉刷新观察者列表
    List<Runnable> onTab1PullDownListener = new ArrayList<Runnable>();
    ArrayList<Flash> flashArrayList = new ArrayList<Flash>();
    Timer flashRolling;
    //ListView视图组件类
    ArrayList<Bitmap> tab1ListViewBitmapCachePool = new ArrayList<Bitmap>();
    ArrayList<ACT> mList = new ArrayList<ACT>();
    ColumAdapter columAdapter;
    int scrollY = 0;
    int tab1PullstartY = -100;
    //List
    ArrayList<Bitmap>[] tab2ListViewBitmapPool;
    ArrayList<ACT>[] iList;
    Runnable[] columnListener;
    long[] columntime;
    int[] scrollColumn;
    //下拉刷新
    int[] touchY;
    boolean[] freshing;
    //[3]
    ArrayList<IdNameMap> datelist = new ArrayList<>();
    Bitmap profileBitmap;
    //主显界面
    private RelativeLayout maintab0;
    private LinearLayout maintab1;
    private RelativeLayout maintab2;
    private LinearLayout maintab3;
    private boolean exitable = false;//按再次返回键退出程序
    private int itemWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        if (!getIntent().getBooleanExtra("fromlog", false) && !getIntent().getBooleanExtra
                ("fromsplash", false)) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class).putExtra("login_splash_layout", true));
        }
        //配置图片缓存
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath().toString() +
                "/Richang/cache/";
        Log.e("outch", dir);

        ImageLoader.getInstance().init(new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPoolSize(10)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheExtraOptions(500, 500)
                .memoryCache(new UsingFreqLimitedMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(new File(dir)))
                .diskCacheSize(64 * 1024 * 1024)//64M
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(8192)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 30 * 1000, 30 *
                        1000))
                .build()
        );

        //
        initTabViews();
        Config.cheVersion(MainActivity.this, true);
        //运行service
        startService(new Intent(MainActivity.this, AlarmPlan.class));
        startService(new Intent(MainActivity.this, Vibration.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode)
            return;
        if (Login.REQCODE == requestCode && data.getBooleanExtra("afterlog", false)) {//注册
            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (ChooseCity.REQCODE == requestCode) {//选城市
            for (int i = 0; i < columntime.length; i++) {
                columntime[i] = 0;
            }
            long city = data.getLongExtra("city", 0);
            if (city != Config.getUSR().getCity().id) {//城市发生变化的时候运行所有城市变化观察者
                Config.getUSR().setCity(city, MainActivity.this);
                ((TextView) findViewById(R.id.tvcity)).setText(Config.getUSR().getCity().name);
                //更新
                if (null != onCityChangeListener && onCityChangeListener.size() > 0) {
                    //遍历观察者并执行
                    for (Runnable lisn : onCityChangeListener)
                        lisn.run();
                }
            }
        } else if (ChooseTag.REQCODE == requestCode) {//选标签
            boolean change = data.getBooleanExtra("change", false);
            if (change) {//标签发生变化
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //更新
                        Config.getUSR().setTags();
                        //listener:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (null != onTagChangeLister && onTagChangeLister.size() > 0) {
                                    //遍历观察者并执行
                                    for (Runnable lisn : onTagChangeLister)
                                        lisn.run();
                                }
                            }
                        });
                    }
                }).start();
            }
        } else if (SetPlan.REQCODE == requestCode || PlanInfo.REQCODE == requestCode || Search
                .REQCODE == requestCode || Content.REQCODE == requestCode) {//加计划
            //行程变化的四个来源:添加计划、计划详情、搜索、活动详情
            Log.e("outplan", PLAN.list.size() + "");
            showdays();
        }
        if (ChangeSomething.REQCODE == requestCode) {//修改个人信息
            TextView name = (TextView) findViewById(R.id.mename);
            name.setText(Config.getUSR().getName());
            String pro = data.getStringExtra("profile");
            if (!pro.equals("")) {
                final ImageView profile = (ImageView) findViewById(R.id.meprofile);
                ImageLoader.getInstance().displayImage("file://" + pro, profile, CircleView
                        .BITMAPDISPLAYER);
            }
        }
        if (data.getBooleanExtra("exit", false)) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (exitable)
            finish();
        else {
            exitable = true;
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    } finally {
                        exitable = false;
                    }
                }
            }).start();
        }
    }

    //初始化
    private void initTabViews() {
        //主显界面
        maintab0 = (RelativeLayout) findViewById(R.id.maintabview0);
        maintab1 = (LinearLayout) findViewById(R.id.maintabview1);
        maintab2 = (RelativeLayout) findViewById(R.id.maintabview2);
        maintab3 = (LinearLayout) findViewById(R.id.maintabview3);
        //顶部按钮
        final LinearLayout chooseCity = (LinearLayout) findViewById(R.id.chooseCity);
        final LinearLayout plus = (LinearLayout) findViewById(R.id.plus);
        final LinearLayout settings = (LinearLayout) findViewById(R.id.settings);
        final LinearLayout tags = (LinearLayout) findViewById(R.id.tags);
        final TextView tvcity = (TextView) findViewById(R.id.tvcity);
        final TextView tvtitle = (TextView) findViewById(R.id.maintitle);
        final String[] title = {"日常", "专栏", "行程", "我的"};
        tvtitle.setText(title[0]);
        tvcity.setText(Config.getUSR().getCity().name);
        chooseCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击切换城市
                startActivityForResult(new Intent(MainActivity.this, ChooseCity.class),
                        ChooseCity.REQCODE);
            }
        });
        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击增添标签
                if (Config.getUSR().getID() > 0)
                    startActivityForResult(new Intent(MainActivity.this, ChooseTag.class),
                            ChooseTag.REQCODE);
                else
                    startActivityForResult(new Intent(MainActivity.this, Login.class).putExtra
                            ("afterlog", true), Login.REQCODE);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.getUSR().getID() > 0)
                    startActivityForResult(new Intent(MainActivity.this, SetPlan.class), SetPlan
                            .REQCODE);
                else
                    startActivityForResult(new Intent(MainActivity.this, Login.class).putExtra
                            ("afterlog", true), Login.REQCODE);
            }
        });
        //脚控
        final RelativeLayout[] btntabs = {
                (RelativeLayout) findViewById(R.id.maintab0),
                (RelativeLayout) findViewById(R.id.maintab1),
                (RelativeLayout) findViewById(R.id.maintab2),
                (RelativeLayout) findViewById(R.id.maintab3)
        };
        final ImageView imgtabs[] = {
                (ImageView) findViewById(R.id.maintab0img),
                (ImageView) findViewById(R.id.maintab1img),
                (ImageView) findViewById(R.id.maintab2img),
                (ImageView) findViewById(R.id.maintab3img)
        };
        final TextView tvtabs[] = {
                (TextView) findViewById(R.id.maintab0text),
                (TextView) findViewById(R.id.maintab1text),
                (TextView) findViewById(R.id.maintab2text),
                (TextView) findViewById(R.id.maintab3text)
        };
        for (int i = 0; i < 4; i++) {//设置可见
            final int index = i;
            btntabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    maintab0.setVisibility(0 == index ? View.VISIBLE : View.GONE);
                    maintab1.setVisibility(1 == index ? View.VISIBLE : View.INVISIBLE);
                    maintab2.setVisibility(2 == index ? View.VISIBLE : View.GONE);
                    maintab3.setVisibility(3 == index ? View.VISIBLE : View.GONE);
                    chooseCity.setVisibility(0 == index ? View.VISIBLE : View.GONE);
                    tags.setVisibility(0 == index ? View.VISIBLE : View.GONE);
                    plus.setVisibility(2 == index ? View.VISIBLE : View.GONE);
                    settings.setVisibility(3 == index ? View.VISIBLE : View.GONE);
                    if (2 == index)
                        tvtitle.setText(String.format("%d月%d日", (AlarmPlan.now.month + 1),
                                AlarmPlan.now.day));
                    else
                        tvtitle.setText(title[index]);

                    imgtabs[0].setImageResource(0 == index ? R.drawable.icon_richang_selected : R
                            .drawable.icon_richang);
                    imgtabs[1].setImageResource(1 == index ? R.drawable.icon_zhuanlan_selected :
                            R.drawable.icon_zhuanlan);
                    imgtabs[2].setImageResource(2 == index ? R.drawable.icon_xingcheng_selected :
                            R.drawable.icon_xingcheng);
                    imgtabs[3].setImageResource(3 == index ? R.drawable.icon_me_selected : R
                            .drawable.icon_me);
                    tvtabs[0].setTextColor(0 == index ? Color.parseColor("#FD7110") : Color
                            .parseColor("#abadb5"));
                    tvtabs[1].setTextColor(1 == index ? Color.parseColor("#FD7110") : Color
                            .parseColor("#abadb5"));
                    tvtabs[2].setTextColor(2 == index ? Color.parseColor("#FD7110") : Color
                            .parseColor("#abadb5"));
                    tvtabs[3].setTextColor(3 == index ? Color.parseColor("#FD7110") : Color
                            .parseColor("#abadb5"));
                }
            });
            tvtabs[i].setText(title[i]);
        }
        //分别初始化四个主显界面
        loadTab0();
        loadTab1();
        loadTab2();
        loadTab3();
    }

    private void setList(ListView listView) {
        try {
            //listView.deferNotifyDataSetChanged();
            if (columAdapter != null)
                columAdapter.notifyDataSetChanged();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = 0;
            if (mList != null && mList.size() > 0) {
                ListAdapter adapter = listView.getAdapter();
                if (null != adapter) {
                    View item = adapter.getView(0, null, listView);
                    if (item != null) {
                        item.measure(0, 0);//加2是因为上下边框
                        params.height = (item.getMeasuredHeight() + 2) * mList.size();
                    }
                }
            }
            listView.setLayoutParams(params);
        } catch (Exception e) {
        }
    }//添加到listView

    //初始化主显界面【1】
    private void loadTab0() {
        //搜索
        ((RelativeLayout) findViewById(R.id.search_enter)).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, Search.class), Search.REQCODE);
            }
        });
        //幻灯片pagerview
        //viewPager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.flashviewpager);
        //final UnViewpager viewPager=(UnViewpager)findViewById(R.id.flashviewpager);
        final List<View> viewList = new ArrayList<View>();
        final LayoutInflater layoutInflater = getLayoutInflater();
        //更新幻灯片
        final Runnable flashdate = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < flashArrayList.size(); i++) {
                    final View v = layoutInflater.inflate(R.layout.flashlist, null);
                    ((TextView) v.findViewById(R.id.flashtime)).setText(flashArrayList.get(i).time);
                    ((TextView) v.findViewById(R.id.flashtitle)).setText(flashArrayList.get(i)
                            .title);
                    ImageLoader.getInstance().displayImage(flashArrayList.get(i).pic, (ImageView)
                            v.findViewById(R.id.flashpic), NormalView.BITMAPDISPLAYER);
                    final long id = flashArrayList.get(i).id;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, Content.class).putExtra
                                    ("act_id", id));
                        }
                    });
                    viewList.add(v);
                }
                viewPager.setAdapter(new Tab1Adapter(viewList));
                viewPager.setOnPageChangeListener(new Tab1ViewChanger(viewList.size()));
                viewPager.setCurrentItem(0);
                //滚动
                if (null == flashRolling) {
                    flashRolling = new Timer();
                } else {
                    flashRolling.cancel();
                    flashRolling = null;
                    flashRolling = new Timer();
                }
                flashRolling.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int p = viewPager.getCurrentItem() + 1;
                                if (p >= flashArrayList.size())
                                    p = 0;
                                viewPager.setCurrentItem(p);
                            }
                        });
                    }
                }, 5000, 5000);
            }
        };
        //更新幻灯片数据
        final Runnable listenerFlash = new Runnable() {
            @Override
            public void run() {
                flashArrayList.clear();
                //网络更新数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //
                        try {
                            String res = WebHTTP.getStr(Config.getInterface().getFlash);
                            JSONObject obj = new JSONObject(res);
                            if (200 == obj.getInt("code")) {
                                JSONArray arr = obj.getJSONArray("data");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject tmp = arr.getJSONObject(i);
                                    flashArrayList.add(new Flash(
                                            tmp.getLong("act_id"), tmp.getString("img"), tmp
                                            .getString("time"), tmp.getString("title")
                                    ));
                                }
                            }
                            runOnUiThread(flashdate);
                        } catch (Exception e) {
                            Log.e("outrflash", e.getMessage());
                        }
                    }
                }).start();
            }
        };
        listenerFlash.run();


        //列表
        final ListView listView = (ListView) findViewById(R.id.maintab0activitlist);
        columAdapter = new ColumAdapter(MainActivity.this, mList, tab1ListViewBitmapCachePool);
        listView.setAdapter(columAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long ind = mList.get(position).getId();
                Intent intent = new Intent(MainActivity.this, Content.class);
                intent.putExtra("act_id", ind);
                startActivityForResult(intent, Content.REQCODE);
            }
        });
        final ObScrollView sv = (ObScrollView) findViewById(R.id.maintab0scroll);
        //监听下拉刷新
        final WebView freshview = (WebView) findViewById(R.id.tab0fresh);

        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onFreshing)
                    return false;
                if (0 < sv.getScrollY()) {
                    freshview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                            .LayoutParams.MATCH_PARENT, 0));
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //这个地方会被挡住,所以放在MOVE中解决
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (-100 == tab1PullstartY)
                        tab1PullstartY = (int) event.getY();
                    int height = (int) (event.getY() - tab1PullstartY);
                    if (height > 50) {
                        int kh = height / 2;
                        if (kh > 64 * Config.SCALE)
                            kh = 64 * Config.SCALE;
                        freshview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.MATCH_PARENT, kh));
                        String url = "file:///android_asset/pre.html";
                        if (freshview.getUrl() == null || !freshview.getUrl().equals(url)) {
                            freshview.loadUrl(url);
                        }
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() ==
                        MotionEvent.ACTION_CANCEL) {
                    int height = (int) (event.getY() - tab1PullstartY);
                    if (height > 200 * Config.SCALE) {
                        int kh = 64 * Config.SCALE;
                        freshview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.MATCH_PARENT, kh));
                        String url = "file:///android_asset/fresh.html";
                        if (freshview.getUrl() == null || !freshview.getUrl().equals(url)) {
                            freshview.loadUrl(url);
                        }
                        if (!onFreshing) {
                            onFreshing = true;
                            for (Runnable runnable : onTab1PullDownListener) {
                                runnable.run();
                            }
                            Log.e("out", "newfresh");
                        }
                    } else {
                        freshview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.MATCH_PARENT, 0));
                    }
                    tab1PullstartY = -100;
                }
                return false;
            }
        });
        //更新第一屏主显推荐内容
        final Runnable listenerRec = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onFreshing = true;
                        //加载内容
                        try {
                            mList.clear();
                            //get
                            WebHTTP webHTTP = new WebHTTP(Config.getInterface()
                                    .getRecommendActivity);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("ct_id", Config.getUSR().getCity().id);
                            map.put("usr_id", Config.getUSR().getID());
                            webHTTP.setPost(map);
                            final ArrayList<ACT> list = ACT.fresh(webHTTP);
                            //draw
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int sum = list.size();
                                    mList.addAll(list);
                                    //其他
                                    freshview.setLayoutParams(new LinearLayout.LayoutParams
                                            (ViewGroup.LayoutParams.MATCH_PARENT, 0));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setList(listView);
                                            sv.scrollTo(0, 0, 500);
                                            onFreshing = false;
                                        }
                                    });
                                    ((TextView) findViewById(R.id.mainaddmore)).setVisibility(sum
                                            < Config.ACTSumOnce ? View.GONE : View.VISIBLE);
                                    ((TextView) findViewById(R.id.mainallover)).setVisibility(sum
                                            >= Config.ACTSumOnce ? View.GONE : View.VISIBLE);
                                }
                            });
                            Thread.sleep(10000);

                        } catch (Exception e) {
                            onFreshing = false;
                        }
                    }
                }).start();
            }
        };
        listenerRec.run();
        //添加城市变化观察者，当城市变化时更新
        onCityChangeListener.add(listenerRec);
        //添加标签变化观察者，当城市变化时更新
        onTagChangeLister.add(listenerRec);
        //下拉
        onTab1PullDownListener.add(listenerRec);
        //加载更多
        final TextView getMore = (TextView) findViewById(R.id.mainaddmore);
        final Runnable getMoreEx = new Runnable() {
            @Override
            public void run() {
                if (getMore.getText().toString().equals("正在加载") || getMore.getVisibility() ==
                        View.GONE)
                    return;
                getMore.setText("正在加载");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onFreshing = true;
                        //get
                        try {
                            WebHTTP webHTTP = new WebHTTP(Config.getInterface()
                                    .getRecommendActivity);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("ct_id", Config.getUSR().getCity().id);
                            map.put("usr_id", Config.getUSR().getID());
                            long startId = mList.get(mList.size() - 1).getId();
                            map.put("start_id", startId);
                            webHTTP.setPost(map);
                            final ArrayList<ACT> list = ACT.fresh(webHTTP);
                            final int sc = scrollY;//
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int sum = list.size();
                                    mList.addAll(list);
                                    sv.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            sv.scrollTo(0, sc);
                                            setList(listView);
                                            onFreshing = false;
                                        }
                                    });
                                    getMore.setText("点击加载更多");
                                    getMore.setVisibility(sum < Config.ACTSumOnce ? View.GONE :
                                            View.VISIBLE);
                                    ((TextView) findViewById(R.id.mainallover)).setVisibility(sum
                                            >= Config.ACTSumOnce ? View.GONE : View.VISIBLE);
                                }
                            });
                        } catch (Exception e) {
                            onFreshing = false;
                        }
                    }
                }).start();
            }
        };
        getMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMoreEx.run();
            }
        });
        //记录滚动位置scrollY
        sv.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObScrollView scrollView, int x, int y, int oldx, int oldy) {
                scrollY = y;
                if ((scrollView.getScrollY() + scrollView.getHeight()) >= scrollView.getChildAt
                        (0).getMeasuredHeight() - 100) {
                    getMoreEx.run();
                }
            }
        });
    }

    //初始化主显界面【2】
    private void loadTab1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String res = WebHTTP.getStr(Config.getInterface().getAllIndustries);
                    JSONObject obj = new JSONObject(res);
                    if (200 == obj.getInt("code")) {
                        Config.COLUMNS.clear();
                        JSONArray arr = obj.getJSONArray("data");
                        for (int i = 0; i < arr.length(); i++) {
                            long id = arr.getJSONObject(i).getLong("ind_id");
                            String name = arr.getJSONObject(i).getString("ind_name");
                            Config.COLUMNS.add(new IdNameMap(id, name));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadTab1onUI();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    private void loadTab1onUI() {
        //顶部标签用来切换viewPager
        final HorizontalScrollView tabSelect = (HorizontalScrollView) findViewById(R.id.tabselect);
        final ImageView tabSelectBottom = (ImageView) findViewById(R.id.tabselectbottom);
        final TextView[] culoumButton = new TextView[Config.COLUMNS.size() + 1];
        LinearLayout columnList = (LinearLayout) findViewById(R.id.columnlist);//专栏列表初始化
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                72 * Config.SCALE, LinearLayout.LayoutParams.MATCH_PARENT
        );
        //viewPager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.maintab1viewpager);
        final List<View> viewList = new ArrayList<>();
        final View[] pages = new View[Config.COLUMNS.size() + 1];
        LayoutInflater layoutInflater = getLayoutInflater();
        //动态添加顶部标签和点击切换事件
        if (Config.COLUMNS.size() == 0)
            return;
        for (int i = 0; i < culoumButton.length; i++) {
            culoumButton[i] = new TextView(MainActivity.this.getApplicationContext());
            if (0 == i)
                culoumButton[i].setText("综合");
            else {
                String ts = Config.COLUMNS.get(i - 1).name;
                //if(ts.length()==2)ts=ts.substring(0,1)+"  "+ts.substring(1,2);
                culoumButton[i].setText(ts);
            }
            culoumButton[i].setLayoutParams(lp);
            culoumButton[i].setTextColor(getResources().getColor(R.color.dark));
            culoumButton[i].setTextSize(18);
            culoumButton[i].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            columnList.addView(culoumButton[i]);
            View v = layoutInflater.inflate(R.layout.maintab1viewpage, null);
            viewList.add(v);
            pages[i] = v;
            final int index = i;
            culoumButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(index);
                    int scr = (index - 2) * 72 * Config.SCALE;
                    tabSelect.scrollTo(scr, 0);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(72 * Config
                            .SCALE, 3 * Config.SCALE);
                    lp.setMargins(72 * index * Config.SCALE, 0, 0, 0);
                    tabSelectBottom.setLayoutParams(lp);
                    for (int k = 0; k < Config.COLUMNS.size() + 1; k++) {
                        culoumButton[k].setTextColor(k == index ? getResources().getColor(R.color
                                .rorange) : getResources().getColor(R.color.dark));
                    }
                }
            });
        }
        //定义城市变化、专栏变化观察者及相关
        tab2ListViewBitmapPool = new ArrayList[Config.COLUMNS.size() + 1];
        iList = new ArrayList[Config.COLUMNS.size() + 1];
        columnListener = new Runnable[Config.COLUMNS.size() + 1];
        columntime = new long[Config.COLUMNS.size() + 1];
        scrollColumn = new int[Config.COLUMNS.size() + 1];
        touchY = new int[Config.COLUMNS.size() + 1];
        freshing = new boolean[Config.COLUMNS.size() + 1];
        for (int i = 0; i < Config.COLUMNS.size() + 1; i++) {
            //刷新控件
            final WebView fresh = (WebView) pages[i].findViewById(R.id.tab1fresh);
            final ObScrollView sv = (ObScrollView) pages[i].findViewById(R.id.tab1scroll);
            final long id = i == 0 ? -1 : Config.COLUMNS.get(i - 1).id;
            final int index = i;
            //定义
            scrollColumn[index] = 0;
            columntime[index] = 0;
            tab2ListViewBitmapPool[index] = new ArrayList<Bitmap>();
            iList[index] = new ArrayList<ACT>();
            touchY[index] = -100;
            final TextView getMore = (TextView) pages[index].findViewById(R.id.tab1getmore);
            columnListener[i] = new Runnable() {
                @Override
                public void run() {
                    freshing[index] = true;
                    getMore.setText("正在加载");
                    //网络获取
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //copy pool
                            final ArrayList<Bitmap> pool = new ArrayList<Bitmap>();
                            if (tab2ListViewBitmapPool[index].size() > 0) {
                                for (int i = 0; i < tab2ListViewBitmapPool[index].size(); i++) {
                                    pool.add(tab2ListViewBitmapPool[index].get(i));
                                }
                            }
                            //get
                            try {
                                WebHTTP webHTTP = new WebHTTP(Config.getInterface().getIndActivity);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("ct_id", Config.getUSR().getCity().id);
                                map.put("ind_id", id);
                                webHTTP.setPost(map);
                                iList[index].clear();
                                final int sum = ACT.fresh(webHTTP, iList[index]);
                                columntime[index] = System.currentTimeMillis();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getMore.setText(sum < Config.ACTSumOnce ? "已加载完" : "加载更多");
                                        drawList(iList[index], pages[index], index, true);
                                        freshing[index] = false;
                                        fresh.setLayoutParams(new LinearLayout.LayoutParams
                                                (ViewGroup.LayoutParams.MATCH_PARENT, 0));
                                    }
                                });
                            } catch (Exception e) {
                                Log.e("outerror", e.getMessage());
                            }
                        }
                    }).start();
                }
            };
            //添加城市变化观察者
            onCityChangeListener.add(columnListener[i]);
            //继续载入
            sv.setScrollViewListener(new ScrollViewListener() {
                @Override
                public void onScrollChanged(ObScrollView scrollView, int x, int y, int oldx, int
                        oldy) {
                    if (y > sv.getMeasuredHeight() - 50 && !freshing[index] && getMore.getText()
                            .equals("加载更多")) {//
                        freshing[index] = true;
                        getMore.setText("正在加载");
                        //网络获取
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //get
                                try {
                                    WebHTTP webHTTP = new WebHTTP(Config.getInterface()
                                            .getIndActivity);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("ct_id", Config.getUSR().getCity().id);
                                    map.put("ind_id", id);
                                    map.put("start_id", iList[index].get(iList[index].size() - 1)
                                            .getId());
                                    webHTTP.setPost(map);
                                    iList[index].clear();
                                    final int sum = ACT.fresh(webHTTP, iList[index]);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getMore.setText(sum < Config.ACTSumOnce ? "已加载完" :
                                                    "加载更多");
                                            drawList(iList[index], pages[index], index, false);
                                            freshing[index] = false;
                                            fresh.setLayoutParams(new LinearLayout.LayoutParams
                                                    (ViewGroup.LayoutParams.MATCH_PARENT, 0));
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("outerror", e.getMessage());
                                }
                            }
                        }).start();
                    }
                }
            });
            //下拉刷新

            sv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (freshing[index])
                        return false;
                    if (0 < sv.getScrollY()) {
                        fresh.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                                .LayoutParams.MATCH_PARENT, 0));
                        return false;
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //这个地方会被挡住,所以放在MOVE中解决
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (-100 == touchY[index])
                            touchY[index] = (int) event.getY();
                        int height = (int) (event.getY() - touchY[index]);
                        if (height > 50) {
                            int kh = height / 2;
                            if (kh > 64 * Config.SCALE)
                                kh = 64 * Config.SCALE;
                            fresh.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                                    .LayoutParams.MATCH_PARENT, kh));
                            String url = "file:///android_asset/pre.html";
                            if (fresh.getUrl() == null || !fresh.getUrl().equals(url)) {
                                fresh.loadUrl(url);
                            }
                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() ==
                            MotionEvent.ACTION_CANCEL) {
                        int height = (int) (event.getY() - touchY[index]);
                        if (height > 100) {
                            int kh = 64 * Config.SCALE;
                            fresh.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                                    .LayoutParams.MATCH_PARENT, kh));
                            String url = "file:///android_asset/fresh.html";
                            if (fresh.getUrl() == null || !fresh.getUrl().equals(url)) {
                                fresh.loadUrl(url);
                            }
                        }
                        if (!freshing[index]) {
                            columnListener[index].run();
                            freshing[index] = true;
                        }
                        touchY[index] = -100;
                    }
                    return false;
                }
            });
        }

        columnListener[0].run();

        viewPager.setAdapter(new Tab2Adapter(viewList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(72 * Config.SCALE, 3
                        * Config.SCALE);
                lp.setMargins((int) (72 * (position + positionOffset) * Config.SCALE), 0, 0, 0);
                tabSelectBottom.setLayoutParams(lp);
                int scr = (int) ((position + positionOffset - 2) * 72 * Config.SCALE);
                tabSelect.scrollTo(scr, 0);
            }

            @Override
            public void onPageSelected(int position) {
                long t = System.currentTimeMillis();
                if (t - columntime[position] > 1000 * 60 * 5)
                    columnListener[position].run();//5分钟未更新
                int scr = (position - 2) * 72 * Config.SCALE;
                tabSelect.scrollTo(scr, 0);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(72 * Config.SCALE, 3
                        * Config.SCALE);
                lp.setMargins(72 * position * Config.SCALE, 0, 0, 0);
                tabSelectBottom.setLayoutParams(lp);
                for (int k = 0; k < Config.COLUMNS.size() + 1; k++) {
                    culoumButton[k].setTextColor(k == position ? Color.parseColor("#FD7110") :
                            Color.BLACK);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private RelativeLayout drawItem(final ACT act, int index) {
        final boolean past = act.getTime().compareTo(AlarmPlan.now.getDatetime()) < 0;//past
        final int pastColor = Color.parseColor("#acacac");
        LinearLayout item = new LinearLayout(MainActivity.this);
        item.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        item.setOrientation(LinearLayout.VERTICAL);

        final ImageView img = new ImageView(MainActivity.this);
        img.setImageResource(R.drawable.abc_ab_share_pack_mtrl_alpha);
        img.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, 145 * Config.SCALE));
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setBackgroundResource(R.drawable.imggrey);
        if (past)
            ImageLoader.getInstance().displayImage(act.getPicture() + "@!display", img, DarkView
                    .BITMAPDISPLAYER);
        else
            ImageLoader.getInstance().displayImage(act.getPicture() + "@!display", img,
                    NormalView.BITMAPDISPLAYER);

        TextView title = new TextView(MainActivity.this);
        title.setPadding(10 * Config.SCALE, 10 * Config.SCALE, 10 * Config.SCALE, 20 * Config
                .SCALE);
        title.setText(act.getTitle());
        title.setTextSize(15.0f);
        title.setTextColor(past ? pastColor : Color.BLACK);

        TextView time = new TextView(MainActivity.this);
        time.setText(act.getTime().substring(0, 16));
        time.setTextSize(13.0f);
        time.setPadding(10 * Config.SCALE, 0, 10 * Config.SCALE, 0);
        time.setTextColor(past ? pastColor : Color.parseColor("#8C8C8C"));

        TextView place = new TextView(MainActivity.this);
        place.setText("地点 :" + act.getPlace());
        place.setTextSize(13.0f);
        place.setTextColor(past ? pastColor : Color.parseColor("#8C8C8C"));
        place.setPadding(10 * Config.SCALE, 0, 10 * Config.SCALE, 10 * Config.SCALE);

        LinearLayout tag = new LinearLayout(MainActivity.this);
        tag.setOrientation(LinearLayout.HORIZONTAL);
        tag.setGravity(Gravity.CENTER_VERTICAL);
        tag.setPadding(10 * Config.SCALE, 0, 10 * Config.SCALE, 10 * Config.SCALE);

        ImageView imgTag = new ImageView(MainActivity.this);
        imgTag.setImageResource(R.drawable.label_icon);
        imgTag.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                10 * Config.SCALE));

        TextView tvtag = new TextView(MainActivity.this);
        tvtag.setText(act.getTagsString());
        tvtag.setPadding(10 * Config.SCALE, 0, 0, 0);
        tvtag.setTextColor(past ? pastColor : Color.parseColor("#8C8C8C"));

        tag.addView(imgTag);
        tag.addView(tvtag);

//            LinearLayout pub = new LinearLayout(MainActivity.this);
//            pub.setOrientation(LinearLayout.HORIZONTAL);
//            pub.setGravity(Gravity.CENTER_VERTICAL);
//            pub.setPadding(10 * Config.SCALE, 0, 10 * Config.SCALE, 10 * Config.SCALE);
//
//                ImageView imgPub = new ImageView(MainActivity.this);
//                imgPub.setLayoutParams(new LinearLayout.LayoutParams(15 * Config.SCALE, 15 *
// Config.SCALE));
//                ImageLoader.getInstance().displayImage(act.pic, imgPub,CircleView
// .BITMAPDISPLAYER);
//
//                TextView tvPub = new TextView(MainActivity.this);
//                tvPub.setText(act.name);
//                tvPub.setTextSize(13.0f);

//                tvPub.setTextColor(past ? pastColor : Color.parseColor("#8C8C8C"));
//                tvPub.setPadding(5 * Config.SCALE, Config.SCALE, 0, Config.SCALE);
//
//                pub.addView(imgPub);
//                pub.addView(tvPub);

        item.addView(img);
        item.addView(title);
        item.addView(time);
        item.addView(place);
        item.addView(tag);
        //item.addView(pub);

        RelativeLayout itembox = new RelativeLayout(MainActivity.this);
        itembox.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        itembox.setPadding(0, 7 * Config.SCALE, 0, 3 * Config.SCALE);

        item.setBackgroundResource(R.drawable.zl);

        itembox.addView(item);
        return itembox;
    }

    private void drawList(ArrayList<ACT> list, View page, int index, boolean clear) {
        LinearLayout ly1 = (LinearLayout) page.findViewById(R.id.tab1viewlinear1);
        LinearLayout ly2 = (LinearLayout) page.findViewById(R.id.tab1viewlinear2);
        itemWidth = ly1.getWidth();
        if (clear) {
            ly1.removeAllViews();
            ly2.removeAllViews();
        }
        ly1.measure(0, 0);
        ly2.measure(0, 0);
        int h1 = ly1.getMeasuredHeight();
        int h2 = ly2.getMeasuredHeight();

        if (null != list && list.size() > 0)
            for (int i = 0; i < list.size(); i++) {
                RelativeLayout item = drawItem(list.get(i), index);
                if (null == item)
                    continue;
                final long act_id = list.get(i).getId();
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Content.class);
                        intent.putExtra("act_id", act_id);
                        startActivityForResult(intent, Content.REQCODE);
                    }
                });
                if (h1 > h2) {
                    ly2.addView(item);
                    ly2.measure(0, 0);
                    h2 = ly2.getMeasuredHeight();
                } else {
                    ly1.addView(item);
                    ly1.measure(0, 0);
                    h1 = ly1.getMeasuredHeight();
                }
            }
    }

    //初始化主显界面【3】
    private void loadTab2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PLAN.list = PLAN.getPLAN();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showdays();
                    }
                });
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showdays() {
        final int itemHeight = 80 * Config.SCALE;
        final ObScrollView days = (ObScrollView) findViewById(R.id.plandays);
        days.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {//手指离开停止滑动
                    int index = (days.getScrollY() + itemHeight / 2) / itemHeight;
                    days.scrollTo(0, index * itemHeight, 150);
                    if (datelist.size() > index) {
                        showoneday(datelist.get(index).id);
                    }
                    return true;
                }
                return false;
            }
        });
        final LinearLayout dayslist = (LinearLayout) findViewById(R.id.plandayslist);
        dayslist.removeAllViews();
        if (null == PLAN.list || 0 == PLAN.list.size()) {
            showoneday(PLAN.date);
            return;
        }
        datelist.clear();
        ///for(int i=0;i<PLAN.list.size();i++){
        if (PLAN.list.size() > 0)
            for (int i = PLAN.list.size() - 1; i >= 0; i--) {
                //判断时期
                //插入datelist
                int inx = datelist.size();
                IdNameMap dat = PLAN.list.get(i).getDate4scroll();
                for (int k = 0; k < datelist.size(); k++) {
                    ///if(datelist.get(k).id>dat.id){
                    if (datelist.get(k).id < dat.id) {
                        inx = i;
                    }
                }
                if ((inx > 0 && datelist.get(inx - 1).id == dat.id) || (datelist.size() > 0 &&
                        inx == 0 && datelist.get(0).id == dat.id)) {
                    continue;
                } else
                    datelist.add(inx, dat);
                //添加
                final LinearLayout ly = new LinearLayout(MainActivity.this);
                ly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                        .WRAP_CONTENT, itemHeight));
                ly.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv = new TextView(MainActivity.this);
                tv.setText(Html.fromHtml(PLAN.list.get(i).getDate4scroll().name));
                if (dat.id < AlarmPlan.now.getDate4scroll().id)
                    tv.setTextColor(Color.parseColor("#a8a8ad"));
                else
                    tv.setTextColor(getResources().getColor(R.color.rorange));
                tv.setHeight(50 * Config.SCALE);
                tv.setWidth(50 * Config.SCALE);
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ImageView img = new ImageView(MainActivity.this);
                if (dat.id < AlarmPlan.now.getDate4scroll().id)
                    img.setImageResource(R.drawable.circle0);
                else
                    img.setImageResource(R.drawable.circle);
                //35+45-12-50=18---12~6
                img.setLayoutParams(new LinearLayout.LayoutParams(24 * Config.SCALE, 50 * Config
                        .SCALE));
                //50-12=38  38/2=19
                img.setPadding(12 * Config.SCALE, 19 * Config.SCALE, 0, 19 * Config.SCALE);
                ly.addView(tv);
                ly.addView(img);
                final int scrollindex = (int) inx;//日期顺序
                final int index = i;//
                ly.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        days.scrollTo(0, itemHeight * scrollindex, 200);
                        showoneday(datelist.get(scrollindex).id);
                    }
                });
                dayslist.addView(ly);
            }
        //show
        if (datelist != null && datelist.size() > 0) {
            int itm = 0;
            long dt = datelist.get(0).id;
            //datelist从前往后（时间从后往前）检索
            //停留在不晚于PLAN.date最迟的一天
            for (int p = 0; p < datelist.size(); p++) {
                IdNameMap tmp = datelist.get(p);
                Log.e("outp", tmp.id + "/" + tmp.name);
                if (tmp.id >= PLAN.date) {
                    itm = p;
                    dt = tmp.id;
                } else
                    break;
            }
            final int goheight = itemHeight * itm;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (days.getScrollY() != goheight) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        } finally {
                            days.scrollTo(0, goheight);
                        }
                    }
                }
            }).start();
            showoneday(dt);
        }
    }

    private boolean showoneday(long date) {
        final LinearLayout list = (LinearLayout) findViewById(R.id.planoneday);
        list.removeAllViews();

        ArrayList<PLAN> p = new ArrayList<>();
        for (int i = 0; i < PLAN.list.size(); i++) {
            PLAN x = PLAN.list.get(i);
            if (x.getDate4scroll().id == date) {
                //添加入今日行程
                //向列表中添加
                if (0 == p.size()) {
                    p.add(x);
                } else if (x.getDayTime() > p.get(p.size() - 1).getDayTime()) {
                    p.add(x);
                } else if (x.getDayTime() < p.get(0).getDayTime()) {
                    p.add(0, x);
                } else if (1 == p.size()) {
                    if (p.get(0).getDayTime() > x.getDayTime()) {
                        p.add(0, x);
                    } else {
                        p.add(x);
                    }
                } else
                    for (int k = 0; k < p.size() - 1; k++) {
                        if (x.getDayTime() >= p.get(k).getDayTime() && x.getDayTime() <= p.get(k
                                + 1).getDayTime()) {
                            p.add((k + 1), x);
                            break;
                        }
                    }
            }
        }
        if (null == p || 0 == p.size())
            return false;

        for (int i = 0; i < p.size(); i++) {
            final PLAN tmp = p.get(i);

            final LinearLayout ly = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5 * Config.SCALE, 0, 5 * Config.SCALE);

            ly.setBackgroundResource(R.drawable.quote);

            ly.setOrientation(LinearLayout.HORIZONTAL);
            ly.setGravity(Gravity.CENTER_VERTICAL);
            ly.setMinimumHeight(100);
            ly.setLayoutParams(params);
            ly.setPadding(25 * Config.SCALE, 10 * Config.SCALE, 25 * Config.SCALE, 10 * Config
                    .SCALE);

            LinearLayout img = new LinearLayout(MainActivity.this);
            img.setOrientation(LinearLayout.VERTICAL);
            img.setPadding(0, 0, 12 * Config.SCALE, 0);
            img.setGravity(Gravity.CENTER_HORIZONTAL);
            img.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ImageView imgview = new ImageView(MainActivity.this);
            imgview.setLayoutParams(new LinearLayout.LayoutParams(32 * Config.SCALE, 32 * Config
                    .SCALE));
            imgview.setImageResource(PLAN.imgs[tmp.isPast() ? 1 : 0][(int) (tmp.theme.id - 1 > 0
                    ? (tmp.theme.id - 1) : 0)]);

            TextView imgtv = new TextView(MainActivity.this);
            imgtv.setText(tmp.theme.name);
            if (tmp.isPast())
                imgtv.setTextColor(getResources().getColor(R.color.dark));
            else
                imgtv.setTextColor(getResources().getColor(R.color.black));
            imgtv.setPadding(0, 0, 0, 10 * Config.SCALE);
            imgtv.setGravity(Gravity.CENTER_HORIZONTAL);

            img.addView(imgview);
            img.addView(imgtv);

            ly.addView(img);

            LinearLayout content = new LinearLayout(MainActivity.this);
            content.setOrientation(LinearLayout.VERTICAL);
            content.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView time = new TextView(MainActivity.this);
            time.setText(p.get(i).getDatetime4tv());
            if (tmp.isPast())
                time.setTextColor(getResources().getColor(R.color.dark));
            else
                time.setTextColor(getResources().getColor(R.color.black));
            TextView title = new TextView(MainActivity.this);
            title.setPadding(0, 5 * Config.SCALE, 0, 0);
            title.setTextSize(15);
            title.setText(tmp.content);
            if (tmp.isPast())
                title.setTextColor(getResources().getColor(R.color.dark));
            else
                title.setTextColor(getResources().getColor(R.color.black));

            content.addView(time);
            content.addView(title);

            ly.addView(content);

            ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = tmp.toIntent();
                    intent.setClass(MainActivity.this, PlanInfo.class);
                    startActivityForResult(intent, PlanInfo.REQCODE);
                }
            });
            list.addView(ly);
        }
        return true;
    }

    //初始化主显界面【4】
    private void loadTab3() {
        //头像
        final ImageView profile = (ImageView) findViewById(R.id.meprofile);
        final String pic = Config.getUSR().getPROFILE() + "@!profile";
        if (Config.getUSR().getID() > 0)
            ImageLoader.getInstance().displayImage(pic, profile, CircleView.BITMAPDISPLAYER);
        else
            profile.setImageResource(R.drawable.icon_cir);

        TextView name = (TextView) findViewById(R.id.mename);
        name.setText(Config.getUSR().getID() > 0 ? Config.getUSR().getName() : "游客");

        //控制
        RelativeLayout myinfo = (RelativeLayout) findViewById(R.id.meinfo);
        RelativeLayout myact = (RelativeLayout) findViewById(R.id.memyact);
        RelativeLayout mypub = (RelativeLayout) findViewById(R.id.memypub);
        RelativeLayout mycollect = (RelativeLayout) findViewById(R.id.memycollect);
        RelativeLayout about = (RelativeLayout) findViewById(R.id.meaboutus);
        RelativeLayout feedback = (RelativeLayout) findViewById(R.id.mefeedback);
        LinearLayout setting = (LinearLayout) findViewById(R.id.settings);
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.getUSR().getID() > 0)
                    startActivityForResult(new Intent(MainActivity.this, ChangeSomething.class),
                            ChangeSomething.REQCODE);
                else
                    startActivityForResult(new Intent(MainActivity.this, Login.class).putExtra
                            ("afterlog", true), Login.REQCODE);
            }
        });
        myact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", ActivityList.myActivity);
                intent.setClass(MainActivity.this, ActivityList.class);
                startActivity(intent);
            }
        });
        mypub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", ActivityList.myPublish);
                intent.setClass(MainActivity.this, ActivityList.class);
                startActivity(intent);
            }
        });
        mycollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", ActivityList.myCollection);
                intent.setClass(MainActivity.this, ActivityList.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FeedBack.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, Setting.class), Setting.REQCODE);
            }
        });
    }

    //[1]
    //幻灯片数据类
    class Flash {
        public long id;
        public String pic;
        public String time;
        public String title;
        public Flash(long id, String pic, String time, String title) {
            this.id = id;
            this.pic = pic;
            this.time = time;
            this.title = title;
        }
        public Flash(ACT act) {
            this(act.getId(), act.getPicture(), act.getTime(), act.getTitle());
        }
    }

    //幻灯片适配器
    class Tab1Adapter extends PagerAdapter {
        private List<View> list;

        public Tab1Adapter(List<View> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(list.get(position));
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            //super.finishUpdate(container);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(list.get(position), 0);
            return list.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {

        }
    }

    class Tab1ViewChanger implements ViewPager.OnPageChangeListener {
        int selected = R.drawable.dot;
        int notselected = R.drawable.dotun;
        private ImageView[] dots;

        public Tab1ViewChanger(int size) {
            dots = new ImageView[size];
            final LinearLayout dotbox = (LinearLayout) findViewById(R.id.flashdots);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8 * Config.SCALE, 8
                    * Config.SCALE);
            params.setMargins(3 * Config.SCALE, 0, 3 * Config.SCALE, 8 * Config.SCALE);
            for (int i = 0; i < size; i++) {
                dots[i] = new ImageView(MainActivity.this);
                dots[i].setImageResource(notselected);
                dots[i].setLayoutParams(params);
                dotbox.addView(dots[i]);
            }
            dots[0].setImageResource(selected);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dots.length; i++) {
                dots[i].setImageResource(position == i ? selected : notselected);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //[2]
    //自定义ViewPager适配器
    class Tab2Adapter extends PagerAdapter {
        private List<View> list;

        public Tab2Adapter(List<View> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(list.get(position), 0);
            return list.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {

        }
    }

}