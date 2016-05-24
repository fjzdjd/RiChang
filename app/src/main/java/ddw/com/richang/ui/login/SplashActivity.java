package ddw.com.richang.ui.login;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ddw.com.richang.Activity.pre.Login;
import ddw.com.richang.MainActivity;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.internet.cache.bitmap.BitmapCacher;
import ddw.com.richang.controller.data.oldversion.Storage;

/**
 * 程序入口
 * 1.判断是不是该版本的第一次运行，如果是，进行引导教程，否则，直接进行展示。
 * 2.判断是否已登录，如果是则进入主界面，否则进入登录页。
 */
public class SplashActivity extends AppCompatActivity {
    //幻灯片数据类
    ArrayList<Integer> flashArrayList = new ArrayList<>();
    ArrayList<Bitmap> c = new ArrayList<>();
    private Storage storage;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        if (getIntent().getBooleanExtra("login_splash_layout", false)) {//由主界面调出用于展示
            setContentView(R.layout.login_splash_layout);
            ((ImageView) findViewById(R.id.splashimg)).setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    } finally {
                        finish();
                    }
                }
            }).start();
            return;
        }
        BitmapCacher.setEVN(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/richang/cache/");
        if (getIntent().getBooleanExtra("tutor", false)) {
            setContentView(R.layout.tutor);
            exeTutor(false);
            return;
        }

        storage = new Storage(SplashActivity.this.getApplicationContext());
        storage.setShareId("usr");
        Config.HINT = (Boolean) storage.getSharedPreference("notice", true);
        Config.SHAKE = (Boolean) storage.getSharedPreference("shake", true);
        //尺寸
        Config.SCALE = (int) getResources().getDisplayMetrics().density;
        //判断是否该版本第一次运行
        int firstrun = firstRin();
        //判断是否已登录,如果用户已登录则获取其信息
        Config.getUSR(SplashActivity.this.getApplicationContext());
        intent = goMain();
        if (null == intent) {//异常，我们强行忽略
            intent = new Intent();
            //去登录
            intent.setClass(SplashActivity.this, Login.class);
        }
        //载入本页面
        setContentView(firstrun);
        if (R.layout.login_splash_layout == firstrun) {//Splash
            exeSplash();
        } else if (R.layout.tutor == firstrun) {//tutor
            exeTutor();
        } else {//这里是个奇怪的异常
            //exeError();
            //但是我们强行假装它不会发生
            exeSplash();//就酱吧
        }
    }

    private int firstRin() {//判断是否该版本第一次运行
        storage.setShareId("version");
        String ves = (String) storage.getSharedPreference("version");
        storage.setSharedPreference("version", Config.VERSION);
        if (null == ves || ves.equals("") || !ves.equals(Config.VERSION)) {//第一次
            return R.layout.tutor;
        } else
            return R.layout.login_splash_layout;
    }

    private void exeSplash() {
        startActivity(intent);
        finish();
    }

    private void exeTutor() {
        exeTutor(true);
    }

    private void exeTutor(final boolean start) {
        final int img[] = {
                R.mipmap.tutor1, R.mipmap.tutor2, R.mipmap.tutor3, R.mipmap.tutor4
        };
        final ViewPager tutor = (ViewPager) findViewById(R.id.tutorpage);
        final List<View> viewList = new ArrayList<View>();
        final LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < img.length; i++) {
            View v = layoutInflater.inflate(R.layout.tutoritem, null);
            ImageView imageView = (ImageView) v.findViewById(R.id.tutorimg);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), img[i]);
            imageView.setImageBitmap(bitmap);
            c.add(bitmap);
            if (i == img.length - 1)
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (start) {
                            intent.putExtra("fromsplash", true);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
            viewList.add(v);
        }
        tutor.setAdapter(new pgAdapter(viewList));
    }

    private void exeError() {
    }

    private Intent goMain() {//判断是否已登录
        intent = new Intent();
        if (Config.getUSR().getID() > 0)
            intent.setClass(SplashActivity.this, MainActivity.class);
        else
            intent.setClass(SplashActivity.this, Login.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
        //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Bitmap bitmap : c) {
            if (bitmap != null && !bitmap.isRecycled())
                bitmap.recycle();
        }
    }

    //幻灯片适配器
    class pgAdapter extends PagerAdapter {
        private List<View> list;

        public pgAdapter(List<View> list) {
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
}
