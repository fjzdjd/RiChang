package ddw.com.richang.Activity.pre;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import ddw.com.richang.model.USR;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.SMSsender;
import ddw.com.richang.controller.data.oldversion.WebHTTP;

public class Resetpasswd extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;

    private String mobile="";
    private String chkStr="";

    private int downcout=60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpasswd);

        ((ImageView)findViewById(R.id.cancelresetpasswd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //chk
        final ImageView imgcheck=(ImageView)findViewById(R.id.imgresetcheck);
        final ImageView linecheck=(ImageView)findViewById(R.id.lineresetcheck);
        final EditText check=(EditText)findViewById(R.id.resetcheckphone);
        check.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String ch = check.getText().toString();
                if (!(Pattern.compile("[0-9]{6}").matcher(ch).matches() && ch.equals(chkStr))) {
                    imgcheck.setImageResource(R.drawable.pencil2);
                    linecheck.setBackgroundColor(getResources().getColor(R.color.rorange));
                } else {
                    imgcheck.setImageResource(R.drawable.pencil);
                    linecheck.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });


        //phone
        final EditText phone=(EditText)findViewById(R.id.resetphone);
        final ImageView imgphone=(ImageView)findViewById(R.id.imgresetphone);
        final ImageView linephone=(ImageView)findViewById(R.id.lineresetphone);
        mobile=getIntent().getStringExtra("mobile");
        phone.setText(mobile);
        if (!Pattern.compile("1[0-9]{10}").matcher(mobile).matches()) {
            imgphone.setImageResource(R.drawable.phone2);
            linephone.setBackgroundColor(getResources().getColor(R.color.rorange));
        }
        phone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String tmp=phone.getText().toString();
                if(!tmp.equals(mobile)){
                    mobile=tmp;
                    chkStr="";
                    check.setText("");
                    imgphone.setImageResource(R.drawable.pencil2);
                    linephone.setBackgroundColor(getResources().getColor(R.color.rorange));
                }
                if (!Pattern.compile("1[0-9]{10}").matcher(phone.getText().toString()).matches()) {
                    imgphone.setImageResource(R.drawable.phone2);
                    linephone.setBackgroundColor(getResources().getColor(R.color.rorange));
                } else {
                    imgphone.setImageResource(R.drawable.phone);
                    linephone.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });


        final ImageView imgpasswd=(ImageView)findViewById(R.id.imgresetpasswd);
        final ImageView linepasswd=(ImageView)findViewById(R.id.lineresetpasswd);
        final EditText passwd1=(EditText)findViewById(R.id.resetpassword1);
        passwd1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!(passwd1.getText().toString().length()>=6)){
                    imgpasswd.setImageResource(R.drawable.password2);
                    linepasswd.setBackgroundColor(getResources().getColor(R.color.rorange));
                } else {
                    imgpasswd.setImageResource(R.drawable.password);
                    linepasswd.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });



        final ImageView imgpasswd2=(ImageView)findViewById(R.id.imgresetpasswd2);
        final ImageView linepasswd2=(ImageView)findViewById(R.id.lineresetpasswd2);
        final EditText passwd2=(EditText)findViewById(R.id.resetpassword2);
        passwd2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!(passwd2.getText().toString().length()>=6 && passwd2.getText().toString().equals(passwd1.getText().toString()))){
                    imgpasswd2.setImageResource(R.drawable.password2);
                    linepasswd2.setBackgroundColor(getResources().getColor(R.color.rorange));
                } else {
                    imgpasswd2.setImageResource(R.drawable.password);
                    linepasswd2.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });

        final Button send=(Button)findViewById(R.id.resetsendcheckphone);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setEnabled(false);
                //检验手机号
                if (!Pattern.compile("1[0-9]{10}").matcher(phone.getText().toString()).matches()) {
                    Toast.makeText(Resetpasswd.this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    send.setEnabled(true);
                    return;
                }
                getChkNum(send);
            }
        });









        final Button btnSign=(Button)findViewById(R.id.submitreset);
        btnSign.setEnabled(true);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSign.setEnabled(false);
                //检验手机号
                if (!Pattern.compile("1[0-9]{10}").matcher(phone.getText().toString()).matches()) {
                    Toast.makeText(Resetpasswd.this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                //检验密码
                if (!passwd1.getText().toString().equals(passwd2.getText().toString())) {
                    Toast.makeText(Resetpasswd.this, "再次输入的密码不匹配！", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                if (passwd1.getText().toString().length() < 6) {
                    Toast.makeText(Resetpasswd.this, "再次输入的密码过短", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                if(check.getText().toString().trim().length()<6 || ! check.getText().toString().equals(chkStr)){
                    Toast.makeText(Resetpasswd.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                //重置密码
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WebHTTP w = new WebHTTP(Config.getInterface().resetPasswd);
                            Map<String,Object> map=new HashMap<String, Object>();
                            map.put("mobile",mobile);
                            map.put("passwd",passwd1.getText().toString());

                            w.setPost(map);

                            String res=w.getStr();
                            //handler
                            JSONObject obj=new JSONObject(res);
                            if(200==obj.getInt("code")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Resetpasswd.this,"重置密码成功！",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }
                        }catch (Exception e){}finally{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnSign.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }






    private void getChkNum(final Button send){
        Random r=new Random(new Date().getTime());
        this.chkStr="";
        for(int i=0;i<6;i++){
            this.chkStr+=(Math.abs(r.nextInt()%10)+"");
        }
        Thread sendSMS=new Thread(new Runnable() {
            @Override
            public void run() {
                if(!USR.isExist(mobile)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Resetpasswd.this, "用户未注册", Toast.LENGTH_SHORT).show();
                            send.setEnabled(true);
                        }
                    });
                    return;
                }
                //发送验证码
                final Boolean res=SMSsender.getInstance().sendReset(mobile, chkStr);
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        downcout--;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                send.setText("发送(" + downcout + "秒)");
                                if (0 == downcout) {
                                    send.setEnabled(true);
                                    timer.cancel();
                                    downcout = Config.CHECKTIME + 1;
                                    send.setText("发送验证码");
                                }
                            }
                        });
                    }
                }, 0, 1000);
            }
        });
        sendSMS.start();

    }
}
