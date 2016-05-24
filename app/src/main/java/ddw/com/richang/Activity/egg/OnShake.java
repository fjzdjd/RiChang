package ddw.com.richang.Activity.egg;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ddw.com.richang.Activity.myAccount.FeedBack;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.Storage;
import ddw.com.richang.service.Vibration;

public class OnShake extends Activity {
    public static int REQCODE=++Config.REQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.onshake);

        ((TextView)findViewById(R.id.shakenothing)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.shakefeed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OnShake.this, FeedBack.class), REQCODE);
            }
        });
        ((TextView)findViewById(R.id.shakeclear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                clearDir();
                Toast.makeText(OnShake.this,"已清除缓存",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        ((TextView)findViewById(R.id.shakecancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Storage storage=new Storage(OnShake.this);
                storage.setShareId("usr");
                storage.setSharedPreference("shake", false);
                Config.SHAKE=false;
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Vibration.vable=true;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }


    private void clearDir(){
        File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Richang/cache/");
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
