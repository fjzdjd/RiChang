package ddw.com.richang.controller;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ddw.com.richang.controller.data.internet.cache.Encription;
import ddw.com.richang.controller.data.oldversion.WebHTTP;

/**
 * Created by dingdewen on 16/3/18.
 */
public class SMSsender {
    private static SMSsender instance=new SMSsender();
    private SMSsender(){}
    public static SMSsender getInstance(){return instance;}

    public boolean sendLog(String mobile,String code){
        try{
            WebHTTP webHTTP=new WebHTTP(Config.getInterface().sendSMS);
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("type",0);
            map.put("mobile", mobile);
            map.put("msg", code);
            map.put("token", Encription.MD5(code + "sms"));
            webHTTP.setPost(map);
            Log.e("outsms",code+"setmap"+Encription.MD5(code + "sms"));
            String res=webHTTP.getStr();
            Log.e("outsms",res);
            JSONObject obj=new JSONObject(res);
            return (200==obj.getInt("code"));
        }catch (Exception e){
            Log.e("outr",e.getMessage());
            return false;
        }
    }

    public boolean sendReset(String mobile,String code){
        try{
            WebHTTP webHTTP=new WebHTTP(Config.getInterface().sendSMS);
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("type",1);
            map.put("mobile", mobile);
            map.put("msg", code);
            map.put("token", Encription.MD5(code + "sms"));
            webHTTP.setPost(map);
            Log.e("outsms",code+"setmap"+Encription.MD5(code + "sms"));
            String res=webHTTP.getStr();
            Log.e("outsms",res);
            JSONObject obj=new JSONObject(res);
            return (200==obj.getInt("code"));
        }catch (Exception e){
            Log.e("outr",e.getMessage());
            return false;
        }
    }


}
