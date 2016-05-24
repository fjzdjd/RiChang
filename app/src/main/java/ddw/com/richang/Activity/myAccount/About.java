package ddw.com.richang.Activity.myAccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ddw.com.richang.ui.login.SplashActivity;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.imgloader.core.DisplayImageOptions;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.controller.data.imgloader.core.display.FadeInBitmapDisplayer;

public class About extends AppCompatActivity {

    ImageView logo;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        tv=(TextView)findViewById(R.id.versiontv);
        tv.setText("日常 v" + Config.VERSION);

        ((ImageView) findViewById(R.id.cancelabout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((RelativeLayout)findViewById(R.id.showwechat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this,"敬请期待",Toast.LENGTH_SHORT).show();
            }
        });
        ((RelativeLayout)findViewById(R.id.checkVersion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.cheVersion(About.this, false);
            }
        });
        ((RelativeLayout)findViewById(R.id.intro)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(About.this, SplashActivity.class).putExtra("tutor", true));
            }
        });
        logo=(ImageView)findViewById(R.id.logo);
        logo.setOnClickListener(new LogoCliclk(logo,tv));
    }

    class LogoCliclk implements View.OnClickListener{
        private ImageView logo;
        TextView tv;
        String[] pro;
        String[] wor;
        int time=0;
        int downcount=100;//10s
        int tmpindex=-1;
        public LogoCliclk(ImageView logov,TextView tvv){
            this.logo=logov;
            this.tv=tvv;
            logo=(ImageView)findViewById(R.id.logo);
            pro="lm,ddw,hwj,yd,zdy,yyk,sch,yhh,lwy".split(",");
            wor="很高兴遇见你|代码筑城|越努力 越幸福||生活不只眼前的苟且|但行好事 莫问前程|天道酬勤|不因为害怕而不去拥有|随心而动".split("\\|");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(downcount<=-1) return;
                    downcount--;
                    if(downcount==0) runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            logo.setImageResource(R.drawable.icon_cir);
                            tv.setText("日常 v" + Config.VERSION);
                        }
                    });
                }
            },100,100);
        }

        @Override
        public void onClick(View v) {
            int index=(int)(System.currentTimeMillis()%pro.length);
            if(tmpindex==index) index--;
            if(index<0) index=pro.length-1;
            tmpindex=index;
            String str="assets://profile/" + pro[index]+".png";
            ImageLoader.getInstance().displayImage(str, logo,new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(800)).build());
            tv.setText(wor[index]);
            downcount=100;
            //
            if(0==time){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(2500);
                            time=0;
                        }catch (Exception e){}
                    }
                }).start();
            }
            time++;
            if(time>=5){//2.5秒内连按三次
                //startActivity(new Intent(About.this, LogoEgg.class));
                return;
            }
        }
    }
}