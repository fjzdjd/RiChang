package ddw.com.richang.Activity.myAccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.WebHTTP;

public class FeedBack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        ((ImageView)findViewById(R.id.cancelfeed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText mail=(EditText)findViewById(R.id.feedmail);
        mail.setText(Config.getUSR().getMAIL());

        final Button submit=(Button) findViewById(R.id.btnfeed);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String feedback = ((EditText) findViewById(R.id.etfeed)).getText().toString();
                submit.setText("正在提交");
                //提交
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WebHTTP webHTTP = new WebHTTP(Config.getInterface().feedBack);
                            Map<String,Object> map=new HashMap<String, Object>();
                            map.put("usr_id",Config.getUSR().getID());
                            map.put("fb_mail",mail.getText().toString());
                            map.put("fb_phone",Config.getUSR().getPHONE());
                            map.put("fb_content", feedback);
                            webHTTP.setPost(map);
                            String res=webHTTP.getStr();
                            JSONObject obj=new JSONObject(res);
                            final int code=obj.getInt("code");
                            final String msg=obj.getString("msg");
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    Toast.makeText(FeedBack.this,msg,Toast.LENGTH_SHORT).show();
                                    submit.setText(200==code?"提交成功":"重新提交");
                                    if(200==code){
                                        new AlertDialog.Builder(FeedBack.this).setTitle("提交建议").setMessage("感谢您的建议，点击返回。")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                }).show();
                                    }
                                }
                            });
                        }catch (Exception e){}
                    }
                }).start();
            }
        });



        if(getIntent().getBooleanExtra("feed",false)){

        }
    }
}
