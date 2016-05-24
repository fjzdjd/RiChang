package ddw.com.richang.Activity.banner;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ddw.com.richang.Activity.activities.ActivityList;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.controller.view.layout.HLView;

public class Search extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;

    EditText searchContent;
    boolean searchAble=true;
    public static ArrayList<String> history=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchContent=(EditText)findViewById(R.id.search_content);
        searchContent.setFocusable(true);
        InputMethodManager inm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        searchContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (66 == keyCode && searchAble) {
                    doSeach();//搜索
                }
                return false;
            }
        });
        ((TextView)findViewById(R.id.search_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.clearsearchhistory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空搜索记录
                Search.history.clear();
                drawHistory();
                searchContent.setText("");
            }
        });

        hot();//热门搜索
        drawHistory();

    }

    private void hot(){
        final HLView hotsearch=(HLView)findViewById(R.id.hotsearch);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject(WebHTTP.getStr(Config.getInterface().getHotSearch));
                    if(200==obj.getLong("code")) {
                        JSONArray arr=obj.getJSONArray("data");
                        String[] x=new String[arr.length()];
                        for(int i=0;i<arr.length();i++){
                            x[i]=arr.getJSONObject(i).getString("keywords");
                        }
                        final String[] hots=x;
                        //添加按钮
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < hots.length; i++) {
                                    final LinearLayout ly = new LinearLayout(Search.this.getApplicationContext());
                                    ly.setPadding(8 * Config.SCALE, 7 * Config.SCALE, 8 * Config.SCALE, 7 * Config.SCALE);


                                    final TextView tv = new TextView(Search.this.getApplicationContext());
                                    tv.setText(hots[i]);
                                    tv.setTextColor(getResources().getColor(R.color.black));
                                    tv.setBackgroundResource(R.drawable.tagall);
                                    tv.setWidth(72*Config.SCALE);//140
                                    tv.setHeight(35*Config.SCALE);//60
                                    tv.setTextSize(15);//13
                                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    tv.setGravity(Gravity.CENTER);

                                    ly.addView(tv);
                                    ly.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {//点击开始搜索
                                            searchContent.setText(tv.getText().toString());
                                            doSeach();
                                        }
                                    });
                                    hotsearch.addView(ly);
                                }
                            }
                        });
                    }
                }catch(Exception e){}
            }
        }).start();
    }

    private void doSeach(){
        if(!searchAble) return;
        searchAble=false;
        final String content=searchContent.getText().toString();
        Search.history.add(content);//添加搜索记录
        Intent intent = new Intent();
        intent.putExtra("name",ActivityList.searchResult);
        intent.putExtra("keywords",searchContent.getText().toString());
        intent.setClass(Search.this, ActivityList.class);
        startActivityForResult(intent, ActivityList.REQCODE);
        finish();
    }

    private void drawHistory(){
        final LinearLayout historyList=(LinearLayout)findViewById(R.id.searchhistorylist);

        final int sum=8;//绘制记录,最多8条
        historyList.removeAllViews();
        int size=Search.history.size();
        ((LinearLayout)findViewById(R.id.searchhistorylistbox)).setVisibility(size>0?View.VISIBLE:View.GONE);
        if(size>0)for(int i=size-1;i>=0 && i>size-sum;i--) {
            LinearLayout one = new LinearLayout(Search.this.getApplicationContext());
            one.setOrientation(LinearLayout.HORIZONTAL);
            one.setPadding(24, 24, 24, 24);
            one.setGravity(Gravity.CENTER_VERTICAL);

            ImageView img = new ImageView(Search.this.getApplicationContext());
            img.setImageResource(R.drawable.time_icon);
            img.setScaleType(ImageView.ScaleType.FIT_XY);

            one.addView(img, 36, 36);

            TextView tv = new TextView(Search.this.getApplicationContext());
            final String text=Search.history.get(i);
            tv.setText(text);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(14);
            tv.setPadding(16, 0, 0, 0);
            one.addView(tv);

            LinearLayout line=new LinearLayout(Search.this.getApplicationContext());
            line.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 2));
            line.setBackgroundColor(Color.parseColor("#45000000"));

            historyList.addView(one);
            historyList.addView(line);

            one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchContent.setText(text);
                    doSeach();
                }
            });
        }
    }
}
