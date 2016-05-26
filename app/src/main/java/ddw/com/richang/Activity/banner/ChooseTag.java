package ddw.com.richang.Activity.banner;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ddw.com.richang.model.common.IdNameMap;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.controller.view.layout.HLView;

public class ChooseTag extends AppCompatActivity {
    public static int REQCODE = ++Config.REQ;
    boolean changed=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosetag);

        alltagslist=(HLView)findViewById(R.id.alltagsview);
        mytaglist=(HLView)findViewById(R.id.mytagsview);

        getTags();//从网络获取标签并绘制

        //cancel
        ((TextView)findViewById(R.id.tagcancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //submit
        ((TextView)findViewById(R.id.tagsubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("change",changed);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    private void getTags(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //all
                    String at= WebHTTP.getStr(Config.getInterface().getAllTags);
                    JSONObject aobj = new JSONObject(at);
                    ArrayList<IdNameMap> atags=null;
                    if (200 == aobj.getLong("code")) {
                        JSONArray allar=aobj.getJSONArray("data");
                        atags=new ArrayList<IdNameMap>();
                        for(int i=0;i<allar.length();i++){
                            JSONObject onetag=allar.getJSONObject(i);
                            atags.add(new IdNameMap(onetag.getLong("tag_id"),onetag.getString("tag_name")));
                        }
                    }else{}

                    //my
                    WebHTTP webHTTP=new WebHTTP(Config.getInterface().getMyTags);
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("usr_id",Config.getUSR().getID());
                    webHTTP.setPost(map);
                    String mt=webHTTP.getStr();
                    webHTTP.close();
                    JSONObject mobj=new JSONObject(mt);
                    ArrayList<IdNameMap> mtags=null;
                    if(200==mobj.getLong("code")){
                        JSONArray myar=mobj.getJSONArray("data");
                        mtags=new ArrayList<IdNameMap>();
                        for(int i=0;i<myar.length();i++){
                            JSONObject onetag=myar.getJSONObject(i);
                            mtags.add(new IdNameMap(onetag.getLong("tag_id"),onetag.getString("tag_name")));
                        }
                    }else{}

                    //回调
                    final ArrayList<IdNameMap> alltags=atags;
                    final ArrayList<IdNameMap> mytags=mtags;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showTags(alltags,mytags);
                        }
                    });
                }catch (Exception e){
                    showTags(null,null);
                }

            }
        }).start();
    }

    HLView mytaglist;
    HLView alltagslist;
    private void showTags(ArrayList<IdNameMap> alltags,ArrayList<IdNameMap> mytags){
        Config.getUSR().getTags().clear();
        if(null!=mytags && mytags.size()>0){
            for(int i=0;i<mytags.size();i++){
                createTagView(mytags.get(i), mytaglist);
                Config.getUSR().getTags().add(mytags.get(i));
            }
        }
        if(null!=alltags && alltags.size()>0){
            for(int i=0;i<alltags.size();i++){
                //不在我的标签中
                boolean in=false;
                if(null!=mytags && mytags.size()>0) for(int j=0;j<mytags.size();j++) {
                    if(alltags.get(i).id==mytags.get(j).id){
                        in=true;
                        break;
                    }
                }
                if(!in)  createTagView(alltags.get(i), alltagslist);
            }
        }
    }

    private void createTagView(final IdNameMap tag, final HLView hlView){
        final LinearLayout ly=new LinearLayout(ChooseTag.this.getApplicationContext());
        ly.setPadding(8*Config.SCALE, 7*Config.SCALE,8*Config.SCALE,7*Config.SCALE);

        final TextView tv=new TextView(ChooseTag.this.getApplicationContext());
        tv.setText(tag.name);

        if(hlView.equals(mytaglist)){
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundResource(R.drawable.tagme);
        }else {
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setBackgroundResource(R.drawable.tagall);
        }
        tv.setWidth(72 * Config.SCALE);//140
        tv.setHeight(35 * Config.SCALE);//60
        tv.setTextSize(15);//13
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setGravity(Gravity.CENTER);

        ly.addView(tv);
        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changed = true;//说明标签已有变动
                hlView.removeView(ly);//
                //add
                HLView other = mytaglist;
                if (hlView.equals(mytaglist)) {
                    other = alltagslist;
                    //找到下标
                    int index = -1;
                    for (int i = 0; i < Config.getUSR().getTags().size(); i++) {
                        if (Config.getUSR().getTags().get(i).id == tag.id) {
                            index = i;
                            break;
                        }
                    }
                    //从我的标签中移除
                    if (-1 != index) Config.getUSR().getTags().remove(index);
                } else {
                    //添加到我的标签
                    Config.getUSR().getTags().add(tag);
                }
                createTagView(tag, other);
            }
        });
        hlView.addView(ly);
    }
}
