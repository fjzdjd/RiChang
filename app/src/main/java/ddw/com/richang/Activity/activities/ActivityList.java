package ddw.com.richang.Activity.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ddw.com.richang.model.ACT;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.controller.view.list.ColumAdapter;

public class ActivityList extends AppCompatActivity {
    public static int REQCODE=++Config.REQ;
    //标记来源
    public static int myActivity=1;
    public static int myCollection=2;
    public static int myPublish=3;
    public static int searchResult=4;

    private ListView listView;
    private ColumAdapter adapter;
    private String url;

    boolean freshing=false;
    int type;
    int ac_catalog=1;

    protected ArrayList<ACT> actlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        type=getIntent().getIntExtra("name",0);
        final String keywords=getIntent().getStringExtra("keywords");
        final TextView hint=(TextView)findViewById(R.id.actlistmore);

        listView=(ListView)findViewById(R.id.aclistview);
        if(actlist==null) actlist=new ArrayList<ACT>();
        actlist.clear();
        adapter=new ColumAdapter(ActivityList.this, actlist,null);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ActivityList.this, Content.class);
                long ind = actlist.get(position).getId();
                intent.putExtra("act_id", ind);
                startActivityForResult(intent, Content.REQCODE);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !freshing && hint.getText().toString().equals("加载中")) {//到头
                    request(keywords, hint);
                }
            }
        });


        final String[] title=new String[]{"未知错误","我的活动","我的收藏","我的发布","搜索结果"};
        ((TextView)findViewById(R.id.activitylisttitle)).setText(title[type]);
        ((ImageView)findViewById(R.id.cancelactivitylist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final LinearLayout catalog=(LinearLayout)findViewById(R.id.catalog);
        catalog.setVisibility(3 == type ? View.VISIBLE : View.GONE);
        final TextView catalog1=(TextView) findViewById(R.id.catalog1);
        final TextView catalog2=(TextView) findViewById(R.id.catalog2);
        final TextView catalog3=(TextView) findViewById(R.id.catalog3);

        catalog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ac_catalog == 1 || freshing) return;
                catalog1.setTextColor(getResources().getColor(R.color.rorange));
                catalog2.setTextColor(getResources().getColor(R.color.black));
                catalog3.setTextColor(getResources().getColor(R.color.black));
                actlist.clear();
                ac_catalog = 1;
                request(keywords, hint);
            }
        });
        catalog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ac_catalog == 2 || freshing) return;
                catalog1.setTextColor(getResources().getColor(R.color.black));
                catalog2.setTextColor(getResources().getColor(R.color.rorange));
                catalog3.setTextColor(getResources().getColor(R.color.black));
                actlist.clear();
                ac_catalog = 2;
                request(keywords, hint);
            }
        });
        catalog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ac_catalog == 3 || freshing) return;
                catalog1.setTextColor(getResources().getColor(R.color.black));
                catalog2.setTextColor(getResources().getColor(R.color.black));
                catalog3.setTextColor(getResources().getColor(R.color.rorange));
                actlist.clear();
                ac_catalog = 3;
                request(keywords, hint);
            }
        });


        //request:
        actlist.clear();
        if(type>0) request(keywords,hint);
        else finish();
    }




    //请求
    private void request(final String keywords,final TextView hint){
        if(freshing) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //网络获取
                freshing=true;
                try {
                    if(4==type) url=Config.getInterface().getSearch;//搜索
                    else url=Config.getInterface().getUsrActivity;
                    WebHTTP webHTTP = new WebHTTP(url);
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("usr_id",Config.getUSR().getID());
                    if(4>type){//我的活动、我的发布、我的收藏
                        map.put("op_type",type);
                        if(3==type) map.put("ac_catalog",ac_catalog);
                    }
                    if(4==type){//搜索
                        map.put("ct_id",Config.getUSR().getCity().id);
                        map.put("keywords",keywords);
                    }
                    if(actlist.size()>0){
                        map.put("start_id",actlist.get(actlist.size()-1).getId());
                    }
                    webHTTP.setPost(map);
                    String res=webHTTP.getStr();
                    webHTTP.close();
                    JSONObject obj=new JSONObject(res);
                    if(200==obj.getInt("code")){
                        JSONArray arr=obj.getJSONArray("data");
                        final int sum=arr.length();
                        //解析
                        final ArrayList<ACT> list=new ArrayList<ACT>();
                        for(int i=0;i<sum;i++){
                            ACT act=new ACT(arr.getJSONObject(i));
                            if(act.getId()>0) list.add(act);
                        }
                        //回调
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(sum>0) {
                                    hint.setText("加载中");
                                }else{
                                    hint.setText("已加载完");
                                }
                                listView.setPadding(0, 0, 0, 0);
                                actlist.addAll(list);
                                adapter.notifyDataSetChanged();
                                freshing=false;
                            }
                        });
                    }
                }catch (Exception e){
                    Log.e("outr",e.getMessage());
                    freshing=false;
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;
        if(Content.REQCODE==requestCode && type==myCollection && data.getBooleanExtra("uncollect",false)){
            final long _id=data.getLongExtra("act_id",-1);
            for(int i=0;i<actlist.size();i++){
                if(actlist.get(i).getId()==_id){
                    actlist.remove(i);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}
