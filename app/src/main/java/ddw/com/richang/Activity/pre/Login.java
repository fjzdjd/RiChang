package ddw.com.richang.Activity.pre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import ddw.com.richang.MainActivity;
import ddw.com.richang.model.USR;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.SMSsender;

public class Login extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;
    int downcout= Config.CHECKTIME+1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final RelativeLayout loginbox=(RelativeLayout)findViewById(R.id.loginlayout);
        final RelativeLayout signbox =(RelativeLayout)findViewById(R.id.signlayout);
        final TextView loginbtn=(TextView) findViewById(R.id.gotologin);
        final TextView signbtn=(TextView)findViewById(R.id.gotosign);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signbox.setVisibility(View.GONE);
                loginbox.setVisibility(View.VISIBLE);
                loginbtn.setVisibility(View.GONE);
                signbtn.setVisibility(View.VISIBLE);
            }
        });
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbox.setVisibility(View.GONE);
                signbox.setVisibility(View.VISIBLE);
                loginbtn.setVisibility(View.VISIBLE);
                signbtn.setVisibility(View.GONE);
            }
        });
        login();
        sign();

        ((TextView)findViewById(R.id.nolog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getIntent().getBooleanExtra("afterlog", false)) {
                    startActivity(new Intent(Login.this, MainActivity.class).putExtra("fromlog", true));
                }
                finish();
            }
        });
    }

    private void login(){
        final EditText phone=(EditText)findViewById(R.id.inputloginphone);
        final EditText passwd=(EditText)findViewById(R.id.inputloginpassword);
        final Button submit=(Button)findViewById(R.id.submitlogin);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                //检验手机号
                if (!Pattern.compile("1[0-9]{10}").matcher(phone.getText().toString()).matches()) {
                    Toast.makeText(Login.this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }
                if (passwd.getText().toString().length() < 6) {
                    Toast.makeText(Login.this, "密码过短", Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }
                //登录
                Config.getUSR().setPHONE(phone.getText().toString());
                Config.getUSR().setPASSWD(passwd.getText().toString());
                Config.getUSR().login(Login.this, new Runnable() {
                    @Override
                    public void run() {
                        if (Config.getUSR().getID() > 0) {
                            setResult(RESULT_OK, new Intent().putExtra("afterlog", true));
                            finish();
                            if (!getIntent().getBooleanExtra("afterlog", false))
                                startActivity(new Intent(Login.this, MainActivity.class).putExtra("fromlog", true));
                        } else {
                            String hint = Config.getUSR().getError();
                            if (null == hint) hint = "未知错误";
                            Toast.makeText(Login.this, hint, Toast.LENGTH_LONG).show();
                        }
                        submit.setEnabled(true);
                    }
                });
            }
        });
        final ImageView imgphone=(ImageView)findViewById(R.id.imgloginphone);
        final ImageView linephone=(ImageView)findViewById(R.id.lineloginphone);
        phone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //检验手机号
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
        final ImageView imgpasswd=(ImageView)findViewById(R.id.imgloginpasswd);
        final ImageView linepasswd=(ImageView)findViewById(R.id.lineloginpasswd);
        passwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!(passwd.getText().toString().length() >= 6)) {
                    imgpasswd.setImageResource(R.drawable.password2);
                    linepasswd.setBackgroundColor(getResources().getColor(R.color.rorange));
                } else {
                    imgpasswd.setImageResource(R.drawable.password);
                    linepasswd.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });

        final TextView resetpasswd=(TextView)findViewById(R.id.resetpasswd);
        resetpasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Login.this,Resetpasswd.class).putExtra("mobile",phone.getText().toString()),Resetpasswd.REQCODE);
            }
        });
    }

    private void sign(){
        final EditText phone=(EditText)findViewById(R.id.inputphone);
        final EditText passwd1=(EditText)findViewById(R.id.inputpassword1);
        final EditText passwd2=(EditText)findViewById(R.id.inputpassword2);
        final EditText check=(EditText)findViewById(R.id.inputcheckphone);

        final Button send=(Button)findViewById(R.id.sendcheckphone);
        send.setText("发送验证码");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检验手机号
                if (!Pattern.compile("1[0-9]{10}").matcher(phone.getText().toString()).matches()) {
                    Toast.makeText(Login.this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                send.setEnabled(false);
                getChkNum(send);
            }
        });


        final ImageView imgcheck=(ImageView)findViewById(R.id.imginputcheck);
        final ImageView linecheck=(ImageView)findViewById(R.id.lineinputcheck);
        check.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String ch=check.getText().toString();
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

        final ImageView imgphone=(ImageView)findViewById(R.id.imginputphone);
        final ImageView linephone=(ImageView)findViewById(R.id.lineinputphone);
        phone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String tmp=phone.getText().toString();
                if(!tmp.equals(mobile)){
                    mobile=tmp;
                    chkStr="";
                    check.setText("");
                    imgcheck.setImageResource(R.drawable.pencil2);
                    linecheck.setBackgroundColor(getResources().getColor(R.color.rorange));
                }
                if (!Pattern.compile("1[0-9]{10}").matcher(mobile).matches()) {
                    imgphone.setImageResource(R.drawable.phone2);
                    linephone.setBackgroundColor(getResources().getColor(R.color.rorange));
                } else {
                    imgphone.setImageResource(R.drawable.phone);
                    linephone.setBackgroundColor(getResources().getColor(R.color.black));
                }
                return false;
            }
        });

        final ImageView imgpasswd=(ImageView)findViewById(R.id.imgpasswd);
        final ImageView linepasswd=(ImageView)findViewById(R.id.linepasswd);
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

        final ImageView imgpasswd2=(ImageView)findViewById(R.id.imgpasswd2);
        final ImageView linepasswd2=(ImageView)findViewById(R.id.linepasswd2);
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


        final Button btnSign=(Button)findViewById(R.id.submitsign);
        btnSign.setEnabled(true);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSign.setEnabled(false);
                //检验手机号
                if (!Pattern.compile("1[0-9]{10}").matcher(phone.getText().toString()).matches()) {
                    Toast.makeText(Login.this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                //检验密码
                if (!passwd1.getText().toString().equals(passwd2.getText().toString())) {
                    Toast.makeText(Login.this, "再次输入的密码不匹配！", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                if (passwd1.getText().toString().length() < 6) {
                    Toast.makeText(Login.this, "再次输入的密码过短", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                if(check.getText().toString().trim().length()<6 || ! check.getText().toString().equals(chkStr)){
                    Toast.makeText(Login.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    btnSign.setEnabled(true);
                    return;
                }
                Config.getUSR().setPHONE(phone.getText().toString());
                Config.getUSR().setPASSWD(passwd1.getText().toString());
                //注册
                Config.getUSR().signin(Login.this, new Runnable() {
                    @Override
                    public void run() {
                        if (Config.getUSR().getID() > 0) {
                            setResult(RESULT_OK, new Intent().putExtra("afterlog", true));
                            finish();
                            if (!getIntent().getBooleanExtra("afterlog", false))
                                startActivity(new Intent(Login.this, MainActivity.class).putExtra("fromlog", true));
                        } else {
                            String hint = Config.getUSR().getError();
                            if (null == hint) hint = "未知错误";
                            Toast.makeText(Login.this, hint, Toast.LENGTH_LONG).show();
                        }
                        btnSign.setEnabled(true);
                    }
                });
            }
        });
    }

    String chkStr="";
    String mobile="";
    private void getChkNum(final Button send){
        Random r=new Random(new Date().getTime());
        this.chkStr="";
        for(int i=0;i<6;i++){
            this.chkStr+=(Math.abs(r.nextInt()%10)+"");
        }
        Thread sendSMS=new Thread(new Runnable() {
            @Override
            public void run() {
                if(USR.isExist(mobile)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this,"用户已注册",Toast.LENGTH_SHORT).show();
                            send.setEnabled(true);
                        }
                    });
                    return;
                }
                //发送验证码
                final Boolean res=SMSsender.getInstance().sendLog(
                        ((EditText)findViewById(R.id.inputphone)).getText().toString(),
                        chkStr
                );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((Button)findViewById(R.id.submitlogin)).setEnabled(true);
                    }
                });
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

    @Override
    public void onBackPressed() {

    }
}
