package ddw.com.richang.model;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ddw.com.richang.model.common.IdNameMap;
import ddw.com.richang.model.common.StoreModel;
import ddw.com.richang.controller.data.oldversion.WebHTTP;

/**
 * Created by dingdewen on 15/12/14.
 */
public class ACT extends StoreModel{

    //线程内获取活动。返回活动数
    public static int fresh(WebHTTP webHTTP,ArrayList<ACT> mList) throws Exception{
        String res=webHTTP.getStr();
        webHTTP.close();
        Log.e("outt",res);
        JSONObject obj=new JSONObject(res);
        if(200==obj.getLong("code")){
            JSONArray arr=obj.getJSONArray("data");
            for(int i=0;i<arr.length();i++){
                mList.add(new ACT(arr.getJSONObject(i)));
            }
            return arr.length();
        }
        return 0;
    }
    public static ArrayList<ACT> fresh(WebHTTP webHTTP) throws Exception{
        String res=webHTTP.getStr();
        webHTTP.close();
        ArrayList<ACT> list=new ArrayList<ACT>();
        JSONObject obj=new JSONObject(res);
        if(200==obj.getLong("code")){
            JSONArray arr=obj.getJSONArray("data");
            for(int i=0;i<arr.length();i++){
                list.add(new ACT(arr.getJSONObject(i)));
            }
            return list;
        }
        return list;
    }


    //定义
    private long id;
    private String title;
    private String time;
    private String place;
    private String picture;
    public String name;
    public String pic;
    private ArrayList<IdNameMap> tags;

    public ACT(ACTUlt act){
        this.id=act.getId();
        this.title=act.getTitle();
        this.time=act.getTitle();
        this.place=act.getPlace();
        this.picture=act.getPoster();
        this.tags=act.getTags();
        this.name=act.pubname;
        this.pic=act.pubpic;
    }
    public ACT(String json){//获取活动概要
        mJson=json;
        try{
            this.readJson(new JSONObject(json));
        }catch (Exception e){}
    }
    public ACT(JSONObject json){
        mJson=json.toString();
        try{
            this.readJson(json);
        }catch (Exception e){}
    }
    private void readJson(JSONObject json) throws Exception{
        this.id=json.getLong("ac_id");
        this.title=json.getString("ac_title");
        this.time=json.getString("ac_time");
        this.place=json.getString("ac_place");
        this.picture=json.getString("ac_poster");
        this.tags=new ArrayList<IdNameMap>();
        JSONArray ts=json.getJSONArray("ac_tags");
        for(int i=0;i<ts.length();i++){
            this.tags.add(new IdNameMap(ts.getJSONObject(i).getLong("tag_id"),ts.getJSONObject(i).getString("tag_name")));
        }
        this.pic=json.getString("usr_pic");
        this.name=json.getString("usr_name");

    }

    //get
    public long getId(){return this.id;}
    public String getTitle(){return this.title;}
    public String getTime(){return this.time;}
    public String getPlace(){return this.place;}
    public String getPicture(){return this.picture;}
    public ArrayList<IdNameMap> getTags(){return this.tags;}
    public String getTagsString(){
        String res="";
        if(this.tags!=null && this.tags.size()>0) for(int i=0;i<this.tags.size();i++){
            res+=(this.tags.get(i).name+" ");
        }
        return res;
    }
    //从网络获取activity详细信息

    //存储
    public void store(Context context){
    }
    public void read(Context context){
    }
}