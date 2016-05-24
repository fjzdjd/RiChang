package ddw.com.richang.Activity.banner;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.Storage;

public class Setting extends AppCompatActivity {
    public static int REQCODE = ++Config.REQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        //back
        final ImageView back=(ImageView)findViewById(R.id.cancelsetting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //notice
        final RelativeLayout notice=(RelativeLayout)findViewById(R.id.changenotice);
        final TextView noticeTv=(TextView)findViewById(R.id.changenoticetv);
        final Storage storage=new Storage(Setting.this);
        storage.setShareId("usr");
        noticeTv.setText(Config.HINT ? "提醒" : "不提醒");
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                notice.setEnabled(false);
                Config.HINT=!Config.HINT;
                storage.setSharedPreference("notice",Config.HINT);
                noticeTv.setText(Config.HINT ? "提醒" : "不提醒");
                notice.setEnabled(true);
            }
        });

        //shake
        final RelativeLayout shake=(RelativeLayout)findViewById(R.id.changeshake);
        final TextView shakeTv=(TextView)findViewById(R.id.changeshaketv);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                shakeTv.post(new Runnable() {
                    @Override
                    public void run() {
                        shakeTv.setText(Config.SHAKE ? "打开" : "关闭");
                    }
                });
            }
        }, 1000, 1000);
        shakeTv.setText(Config.SHAKE ? "打开" : "关闭");
        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shake.setEnabled(false);
                Config.SHAKE=!Config.SHAKE;
                storage.setSharedPreference("shake", Config.SHAKE);
                shakeTv.setText(Config.SHAKE ? "打开" : "关闭");
                shake.setEnabled(true);
            }
        });

        //cache
        final String evDir=Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Richang/cache/";
        final TextView cacheTv=(TextView)findViewById(R.id.clearsum);
        cacheTv.setText(calcDir(evDir));
        final RelativeLayout clearCache=(RelativeLayout)findViewById(R.id.clear);
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                clearDir(evDir);
                cacheTv.setText(calcDir(evDir));
                clearCache.setEnabled(true);
            }
        });
        //exit
        ((RelativeLayout)findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Storage storage=new Storage(Setting.this);
                storage.setShareId("usr");
                long m1=-1;
                storage.setSharedPreference("id",m1);
                Config.getUSR().logout();
                setResult(RESULT_OK,new Intent().putExtra("exit",true));
                finish();
            }
        });
    }

    //calc
    private String calcDir(String dirStr){
        File dir=new File(dirStr);
        if(!dir.exists()) return "0 B";
        double res=calcDirSp(dir);
        clearAble=res>0;
        if(res>1024*1024) return ((int)(res/1024/102.4))/10.0+" MB";
        else if(res>1024) return ((int)(res/102.4))/10.0+" KB";
        else return res+" B";
    }
    private double calcDirSp(File file){
        if(file.isDirectory()){
            File[] children = file.listFiles();
            double res=0;
            for(File f:children){
                res+=calcDirSp(f);
            }
            return res;
        }else{
            return file.length();
        }
    }

    //clear
    private boolean clearAble=false;//是否有必要清理
    private void clearDir(String dirStr){
        if(!clearAble) return;
        clearAble=false;
        File dir=new File(dirStr);
        if(!dir.exists())return;
        clearDirSp(dir);
    }
    private void clearDirSp(File file){
        if(file.isDirectory()){
            File[] children = file.listFiles();
            for(File f:children){
                clearDirSp(f);
            }
        }else{
            file.delete();
        }
    }
}
