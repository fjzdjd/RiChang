package ddw.com.richang.model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ddw.com.richang.model.common.IdNameMap;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.service.AlarmPlan;

/**
 * Created by dingdewen on 15/12/15.
 */
public class PLAN{
    public static ArrayList<PLAN> list=new ArrayList<PLAN>();
    public static long date=-1;

    public static IdNameMap[] themeList=new IdNameMap[]{
            new IdNameMap(1,"会议"),
            new IdNameMap(2,"约会"),
            new IdNameMap(3,"出差"),
            new IdNameMap(4,"运动"),
            new IdNameMap(5,"购物"),
            new IdNameMap(6,"娱乐"),
            new IdNameMap(7,"聚会"),
            new IdNameMap(8,"其他"),
    };

    public static int[][] imgs= {
            {R.drawable.meeting_select,
                    R.drawable.dating_select,
                    R.drawable.outbusiness_select,
                    R.drawable.sport_select,
                    R.drawable.shopping_select,
                    R.drawable.entertain_select,
                    R.drawable.party_select,
                    R.drawable.otherplan_select},
            {R.drawable.meeting,
                    R.drawable.dating,
                    R.drawable.outbusiness,
                    R.drawable.sport,
                    R.drawable.shopping,
                    R.drawable.entertain,
                    R.drawable.party,
                    R.drawable.otherplan}
    };

    public long id;
    public long act_id;
    public ACT act;

    public IdNameMap theme;
    public int year;
    public int month;
    public int day;
    public int weekday;
    public int hour;
    public int minute;

    public int hintHour;

    public String content;

    public boolean hint1;
    public boolean hint2;
    public boolean hint3;

    public PLAN(){
        this.id=-1;
        this.act_id=-1;

        this.theme=themeList[0];

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        this.year=calendar.get(Calendar.YEAR);
        this.month=calendar.get(Calendar.MONTH);
        this.day=calendar.get(Calendar.DAY_OF_MONTH);
        this.hour=calendar.get(Calendar.HOUR_OF_DAY);
        this.minute=calendar.get(Calendar.MINUTE);
        this.weekday=calendar.get(Calendar.DAY_OF_WEEK)-1;

        this.hintHour=8;

        this.content="";

        this.hint1=false;
        this.hint2=false;
        this.hint3=false;
    }
    public PLAN(Intent intent){
        this();
        try{
            this.id=intent.getLongExtra("id",this.id);
            this.act_id=intent.getLongExtra("act_id", this.act_id);
            this.theme=new IdNameMap(
                    intent.getLongExtra("theme_id",this.theme.id),
                    intent.getStringExtra("theme_name")
            );
            this.year=intent.getIntExtra("year",this.year);
            this.month=intent.getIntExtra("month",this.month);
            this.day=intent.getIntExtra("day",this.day);
            this.hour=intent.getIntExtra("hour",this.hour);
            this.minute=intent.getIntExtra("minute",this.minute);
            this.content=intent.getStringExtra("content");
            this.hint1=intent.getBooleanExtra("hint1", false);
            this.hint2=intent.getBooleanExtra("hint2",false);
            this.hint3=intent.getBooleanExtra("hint3",false);
        }catch (Exception e){}
    }
    public PLAN(ACTUlt act){
        this();
        this.id=act.pl_id;
        this.act_id=act.getId();
        this.act=new ACT(act);
        this.year=Integer.parseInt(act.getTime().substring(0, 4));
        this.month=Integer.parseInt(act.getTime().substring(5, 7))-1;
        this.day=Integer.parseInt(act.getTime().substring(8, 10));
        this.hour=Integer.parseInt(act.getTime().substring(11,13));
        this.minute=Integer.parseInt(act.getTime().substring(14,16));
        this.hint1=act.hint1;
        this.hint2=act.hint2;
        this.hint3=act.hint3;
        this.content=act.getTitle();


    }
    public Intent toIntent(){
        Intent intent=new Intent();
        intent.putExtra("id",this.id);
        intent.putExtra("act_id",this.act_id);
        intent.putExtra("theme_id",this.theme.id);
        intent.putExtra("theme_name",this.theme.name);
        intent.putExtra("year",this.year);
        intent.putExtra("month",this.month);
        intent.putExtra("day",this.day);
        intent.putExtra("hour",this.hour);
        intent.putExtra("minute",this.minute);
        intent.putExtra("hint1",this.hint1);
        intent.putExtra("hint2",this.hint2);
        intent.putExtra("hint3",this.hint3);
        intent.putExtra("content",this.content);
        return intent;
    }

    public String getDatetime4tv(){
        return String.format("%d年%02d月%02d日 %02d:%02d",this.year,(this.month+1),this.day,this.hour,this.minute);
    }public String getDatetime(){
        return String.format("%d-%02d-%02d %02d:%02d:00",this.year,(this.month+1),this.day,this.hour,this.minute);
    }
    public int getDayTime(){
        return this.hour*100+this.minute;
    }
    public boolean isPast(){
        if(this.getDate4scroll().id < AlarmPlan.now.getDate4scroll().id) return true;
        if(this.getDate4scroll().id > AlarmPlan.now.getDate4scroll().id) return false;
        return this.getDayTime() < AlarmPlan.now.getDayTime();
    }
    public IdNameMap getDate4scroll(){
        Calendar c=Calendar.getInstance();
        c.set(this.year,this.month,this.day);
        this.weekday=c.get(Calendar.DAY_OF_WEEK)-1;
        String[] week=new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        return new IdNameMap(
                this.year*12*35+this.month*35+this.day,
                (this.month+1)+"."+this.day+"<br />"+week[this.weekday]
        );
    }


    public void submit(final Activity activity, final Runnable succ, final Runnable error){
        final PLAN thisplan=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHTTP w;
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("usr_id",Config.getUSR().getID());
                try {
                    if(id>0){//修改
                        w=new WebHTTP(Config.getInterface().setPlan);
                        map.put("op_type", 2);
                        map.put("pl_id",id);
                    }else {
                        if(act_id>0){
                            w = new WebHTTP(Config.getInterface().joinPlan);
                            map.put("ac_id", act_id);
                        }else {
                            w = new WebHTTP(Config.getInterface().addPlan);
                        }
                        map.put("op_type", 1);
                    }
                    map.put("theme_id", theme.id);
                    map.put("pl_time", getDatetime());
                    map.put("pl_alarm_one", hint1 ? 1 : 0);
                    map.put("pl_alarm_two", hint2 ? 1 : 0);
                    map.put("pl_alarm_three", hint3 ? 1 : 0);
                    map.put("pl_content", content);
                    map.put("ac_place", "");

                    w.setPost(map);
                    String res=w.getStr();
                    Log.e("outsubmit",res);
                    //
                    JSONObject obj=new JSONObject(res);
                    if(200==obj.getInt("code")){
                        if(id>=0){//修改
                            //从列表中删除
                            for(int i=0;i<PLAN.list.size();i++){
                                if(id==PLAN.list.get(i).id){
                                    PLAN.list.remove(i);
                                    break;
                                }
                            }
                        }else{//新添加
                            id=obj.getJSONObject("data").getLong("pl_id");
                        }
                        //向列表中添加
                        if(0==PLAN.list.size()){
                            PLAN.list.add(thisplan);
                        }else if(thisplan.getDate4scroll().id>PLAN.list.get(PLAN.list.size()-1).getDate4scroll().id){
                            PLAN.list.add(thisplan);
                        }else if(thisplan.getDate4scroll().id<PLAN.list.get(0).getDate4scroll().id){
                            PLAN.list.add(0,thisplan);
                        }else if(1==PLAN.list.size()){
                            if(PLAN.list.get(0).getDate4scroll().id>thisplan.getDate4scroll().id){
                                PLAN.list.add(0, thisplan);
                            }else{
                                PLAN.list.add(thisplan);
                            }
                        }else for(int i=0;i<PLAN.list.size()-1;i++){
                            if(thisplan.getDate4scroll().id>=PLAN.list.get(i).getDate4scroll().id && thisplan.getDate4scroll().id<=PLAN.list.get(i+1).getDate4scroll().id){
                                PLAN.list.add((i + 1), thisplan);
                                break;
                            }
                        }
                        //
                        PLAN.date=thisplan.getDate4scroll().id;
                        if(activity!=null && succ!=null) activity.runOnUiThread(succ);
                    }else{
                        if(activity!=null && error!=null) activity.runOnUiThread(error);
                    }
                }catch(Exception e){
                    Log.e("oute",e.getMessage());
                    if(activity!=null && error!=null) activity.runOnUiThread(error);
                }
            }
        }).start();
    }
    public void delete(final Activity activity, final Runnable succ, final Runnable error){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    WebHTTP w=new WebHTTP(Config.getInterface().deletePlan);
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("usr_id",Config.getUSR().getID());
                    map.put("pl_id", id);
                    w.setPost(map);
                    String res=w.getStr();
                    JSONObject obj=new JSONObject(res);
                    if(activity!=null) {
                        if (200 == obj.getInt("code")) {//删除成功
                            for(int i=0;i<PLAN.list.size();i++){
                                if(id==PLAN.list.get(i).id){
                                    PLAN.list.remove(i);
                                    break;
                                }
                            }
                            if(succ!=null) activity.runOnUiThread(succ);
                        }else if(error!=null)  activity.runOnUiThread(error);
                    }
                }catch (Exception e){}
            }
        }).start();
    }

    public static ArrayList<PLAN> getPLAN(){
        ArrayList<PLAN> list=new ArrayList<PLAN>();
        try {
            WebHTTP w = new WebHTTP(Config.getInterface().getUsrPlan);
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("usr_id",Config.getUSR().getID());
            map.put("begin_date", "2015-05-01");
            map.put("end_date", "2999-05-01");
            w.setPost(map);
            String res=w.getStr();
            Log.e("outplan",res);
            JSONObject obj=new JSONObject(res);
            if(200==obj.getInt("code")){
                JSONArray arr=obj.getJSONArray("data");
                for(int i=0;i<arr.length();i++){
                    JSONObject p=arr.getJSONObject(i);
                    PLAN pn=new PLAN();
                    pn.id=p.getLong("pl_id");
                    pn.act_id=p.getLong("ac_id");
                    pn.theme=new IdNameMap(p.getLong("theme_id"),p.getString("theme_name"));
                    pn.year=Integer.parseInt(p.getString("pl_time").substring(0, 4));
                    pn.month=Integer.parseInt(p.getString("pl_time").substring(5, 7))-1;
                    pn.day=Integer.parseInt(p.getString("pl_time").substring(8, 10));
                    pn.hour=Integer.parseInt(p.getString("pl_time").substring(11,13));
                    pn.minute=Integer.parseInt(p.getString("pl_time").substring(14,16));

                    pn.content=p.getString("pl_content");
                    pn.hint1=p.getString("pl_alarm_one").equals("1");
                    pn.hint2=p.getString("pl_alarm_two").equals("1");
                    pn.hint3=p.getString("pl_alarm_three").equals("1");
                    list.add(pn);
                }
            }
        }catch (Exception e){
            Log.e("outerror",e.getMessage());
        }finally {
            return list;
        }
    }
}
