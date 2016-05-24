package ddw.com.richang.Activity.myAccount;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ddw.com.richang.Activity.activities.Photo;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.controller.data.internet.cache.Encription;
import ddw.com.richang.controller.data.internet.cache.bitmap.BitmapPattern;
import ddw.com.richang.controller.data.oldversion.Storage;
import ddw.com.richang.controller.data.oldversion.WebHTTP;

public class ChangeSomething extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changesomething);

        final TextView name=(TextView)findViewById(R.id.changetvname);
        final TextView gender=(TextView)findViewById(R.id.changetvgender);
        final TextView phone=(TextView)findViewById(R.id.changetvphone);
        final TextView email=(TextView)findViewById(R.id.changetvemail);
        final TextView sign=(TextView)findViewById(R.id.changetvsign);

        ((ImageView)findViewById(R.id.cancelchangesomething)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra("profile",profile));
                finish();
            }
        });

        ((RelativeLayout)findViewById(R.id.changeprofile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent,STEP1);
            }
        });
        final ImageView profile=(ImageView) findViewById(R.id.changeimgprofile);
        final String profileUrl=Config.getUSR().getPROFILE() + "@!profile";
        if(Config.getUSR().getID()>0){
            ImageLoader.getInstance().displayImage(profileUrl,profile);
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ChangeSomething.this, Photo.class).putExtra("photo", Config.getUSR().getPROFILE()));
                }
            });
        }else profile.setImageResource(R.drawable.icon_cir);

        name.setText(Config.getUSR().getName());
        gender.setText(Config.getUSR().getGENDER());
        phone.setText(Config.getUSR().getPHONE());
        email.setText(Config.getUSR().getMAIL());
        sign.setText(Config.getUSR().getSIGN());

        ((RelativeLayout)findViewById(R.id.changename)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et=getEditText("输入新昵称",Config.getUSR().getName());
                pop("修改昵称", et, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s=et.getText().toString();
                        Config.getUSR().setNAME(s, new Storage(ChangeSomething.this.getApplicationContext()));
                        name.setText(s);
                    }
                });
            }
        });
        ((RelativeLayout)findViewById(R.id.changegender)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ly=new LinearLayout(ChangeSomething.this);
                ly.setOrientation(LinearLayout.VERTICAL);
                ly.setPadding(36,12,36,12);

                final RadioButton r1=new RadioButton(ChangeSomething.this);
                final RadioButton r2=new RadioButton(ChangeSomething.this);
                r1.setChecked(Config.getUSR().getGENDER().equals("男"));
                r2.setChecked(!r1.isChecked());
                r1.setText("男");
                r2.setText("女");
                r1.setTextColor(getResources().getColor(R.color.rorange));
                r2.setTextColor(getResources().getColor(R.color.rorange));
                r1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        r2.setChecked(false);
                    }
                });
                r2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        r1.setChecked(false);
                    }
                });
                ly.addView(r1);
                ly.addView(r2);
                pop("修改性别", ly, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Config.getUSR().setGENDER(
                                r1.isChecked()?"男":"女",
                                new Storage(ChangeSomething.this.getApplicationContext())
                        );
                        gender.setText(r1.isChecked()?"男":"女");
                    }
                });
            }
        });
        ((RelativeLayout)findViewById(R.id.changemail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et=getEditText("输入新邮件",Config.getUSR().getMAIL());
                pop("修改邮件", et, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s=et.getText().toString();
                        Config.getUSR().setMAIL(s, new Storage(ChangeSomething.this.getApplicationContext()));
                        email.setText(s);
                    }
                });
            }
        });
        ((RelativeLayout)findViewById(R.id.changesign)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et=getEditText("输入新签名",Config.getUSR().getSIGN());
                pop("修改签名", et, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = et.getText().toString();
                        Config.getUSR().setSIGN(s, new Storage(ChangeSomething.this.getApplicationContext()));
                        sign.setText(s);
                    }
                });
            }
        });

    }

    private void pop(String title,View view, final View.OnClickListener click){
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout=layoutInflater.inflate(R.layout.dialog, null);
        ((RelativeLayout)layout.findViewById(R.id.dialogcontainer)).addView(view);
        ((TextView)layout.findViewById(R.id.dialogtitle)).setText(title);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        final AlertDialog alertDialog=builder.show();
        ((TextView) layout.findViewById(R.id.dialogcancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ((TextView)layout.findViewById(R.id.dialogsure)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onClick(v);
                alertDialog.dismiss();
            }
        });
    }

    private EditText getEditText(String hint,String str){
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText et=new EditText(ChangeSomething.this);
        et.setLayoutParams(params);
        et.setBackgroundResource(R.drawable.edit);
        et.setLines(1);
        et.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        et.setPadding(36,12,36,0);
        et.setHint(hint);
        et.setText(str);
        return et;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,new Intent().putExtra("profile",profile));
        finish();
    }

    Bitmap profileBitmap=null;
    String profile="";
    Uri tmp=null;
    private final static int STEP1=++Config.REQ;
    private final static int STEP2=++Config.REQ;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            profile="";
            return;
        }
        try {
            if (STEP1 == requestCode) {//换头像
                Uri uri=data.getData();
                Log.e("out[",uri.toString());
                Intent intent = new Intent("com.android.camera.action.CROP");
                if (null != intent && data != null) {
                    profile=Environment.getExternalStorageDirectory().toString()+"/Richang/cache/"+Encription.MD5(System.currentTimeMillis()+""+Config.getUSR().getID());
                    intent.setDataAndType(uri, "image/*");
                    intent.putExtra("crop", "true");
                    // aspectX aspectY 是宽高的比例
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    // outputX outputY 是裁剪图片宽高
                    intent.putExtra("outputX",150);
                    intent.putExtra("outputY", 150);
                    //存文件
                    File dir=new File(Environment.getExternalStorageDirectory().toString()+"/Richang/cache/");
                    if(!dir.isDirectory()) dir.mkdirs();
                    tmp=Uri.parse("file://"+profile);
                    Log.e("outf",tmp.toString());
                    //
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, tmp);
                    intent.putExtra("outputFormat",Bitmap.CompressFormat.PNG.toString());
                    startActivityForResult(intent, STEP2);
                }
            }else if (STEP2 == requestCode) {//换头像缩放选择
                if(!profile.equals("")) profileBitmap=BitmapFactory.decodeFile(profile);
                if (null == profileBitmap) profile="";
                else{
                    //显示
                    ((ImageView) findViewById(R.id.changeimgprofile)).setImageBitmap(profileBitmap);
                    new Thread(new Runnable() {   //上传图片
                        @Override
                        public void run() {
                            uploadPic(profile);
                        }
                    }).start();
                }
            }
        }catch (Exception e){
            Log.e("outr",e.toString());
        }
    }





    private void uploadPic(String path){
        try {
            //上传
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "******";
            URL url = new URL("http://myrichang.com/Publish/Public/Uploads/android/upload_android.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                    + path.substring(path.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(path);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            Log.e("outup", result);
            dos.close();
            is.close();

            JSONObject obj=new JSONObject(result);
            if(200==obj.getInt("code")){
                String p=obj.getString("msg");
                //
                Config.getUSR().setPROFILE(p,new Storage(ChangeSomething.this.getApplicationContext()));
            }else profile="";
        }catch (Exception e){}
    }

}
