package ddw.com.richang.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.Storage;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.model.common.IdNameMap;
import ddw.com.richang.model.common.StoreModel;

/**
 * Created by dingdewen on 15/11/23.
 * 用户模型
 */
public class USR extends StoreModel { //单例
    //实现单例
    public static USR usr;
    //定义
    private long ID;
    private String PASSWD;
    private IdNameMap CITY;
    private ArrayList<IdNameMap> TAGS;
    private String NAME;
    private int GENDER;
    private String PROFILE;
    private String PHONE;
    private String MAIL;
    private String SIGN;
    private String ERROR;
    private USR() {
        //初始化
        this.ID = -1;
        this.PASSWD = "";
        this.CITY = null;
        this.NAME = "游客";
        this.PHONE = "10000000000";
        this.GENDER = 1;
        this.SIGN = "个性签名";
        this.MAIL = "a@b.c";
        this.TAGS = new ArrayList<IdNameMap>();
        this.PROFILE = "";
    }

    public static USR getInstance() {
        return USR.getInstance(null);
    }

    public static USR getInstance(Context context) {
        if (null == usr) {
            usr = new USR();
            if (null != context)
                usr.read(context);
        }
        return usr;
    }

    public static boolean isExist(String mobile) {
        String res = WebHTTP.getStr(Config.getInterface().isExist + "?usr_phone=" + mobile);
        try {
            JSONObject obj = new JSONObject(res);
            return ((200 == obj.getInt("code")) && (1 == obj.getInt("exist")));
        } catch (Exception e) {
            return false;
        }
    }

    //登录、注册时从本地、网络获取用户信息
    public void signin(final Activity activity, final Runnable runnable) {
        this.getInfo(2, activity, runnable);
    }

    public void login(final Activity activity, final Runnable runnable) {
        this.getInfo(1, activity, runnable);
    }

    public void logout() {
        this.ID = -1;
    }

    private void getInfo(final int type, final Activity activity, final Runnable runnable) {
        ERROR = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("op_type", type);
                    map.put("usr_phone", PHONE);
                    map.put("act_passwd", PASSWD);
                    WebHTTP webHTTP = new WebHTTP(Config.getInterface().getUSRInfo);
                    webHTTP.setGet(map);
                    String res_usrinfo = webHTTP.getStr();
                    //
                    JSONObject obj = new JSONObject(res_usrinfo);
                    if (200 == obj.getInt("code")) {
                        JSONObject data = obj.getJSONObject("data");
                        mJson = data.toString();
                        readJSON(data);
                        store(activity.getApplicationContext());
                    } else {
                        ERROR = obj.getString("msg");
                    }
                } catch (Exception e) {
                } finally {
                    if (null != activity && null != runnable)
                        activity.runOnUiThread(runnable);
                }
            }
        }).start();
    }

    //解析用户信息的JSON
    private void readJSON(JSONObject usr) throws Exception {
        mJson = usr.toString();
        Log.e("outtest", "json" + mJson);
        this.ID = usr.getLong("usr_id");
        this.NAME = usr.getString("usr_name");
        this.SIGN = usr.getString("usr_sign");
        this.PROFILE = usr.getString("usr_pic");
        this.GENDER = usr.getInt("usr_sex");
        this.PHONE = usr.getString("usr_phone");
        this.MAIL = usr.getString("usr_mail");
        this.CITY = new IdNameMap(usr.getLong("ct_id"), usr.getString("ct_name"));
    }

    public long getID() {
        return this.ID;
    }

    public void setID(long id) {
        this.ID = id;
    }

    public void setPASSWD(String passwd) {
        this.PASSWD = passwd;
    }

    public String getError() {
        return this.ERROR;
    }

    //标签
    public ArrayList<IdNameMap> getTags() {
        return this.TAGS;
    }

    public void setTags() {
        //上传!!注意！考虑到操作异步，这些代码不是在新线程里执行的。
        try {
            WebHTTP webHTTP = new WebHTTP(Config.getInterface().setTags);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("usr_id", getID());
            String t = "[";
            for (int i = 0; i < getTags().size(); i++) {
                if (i > 0)
                    t += ",";
                t += getTags().get(i).id;
            }
            t += "]";
            map.put("tags[]", t);
            webHTTP.setPost(map);
            String res = webHTTP.getStr();
            webHTTP.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.NAME;
    }

    public void setNAME(String name, Storage storage) {
        this.NAME = name;
        this.updated("usr_name", this.NAME, storage);
    }

    public String getGENDER() {
        return 1 == this.GENDER ? "男" : "女";
    }

    public void setGENDER(String gender, Storage storage) {
        this.GENDER = gender.equals("男") ? 1 : 2;
        updated("usr_sex", gender.equals("男") ? 1 : 2, storage);
    }

    public String getPHONE() {
        return this.PHONE;
    }

    public void setPHONE(String phone) {
        this.PHONE = phone;
    }

    public String getMAIL() {
        return this.MAIL;
    }

    public void setMAIL(String mail, Storage storage) {
        this.MAIL = mail;
        updated("usr_mail", this.MAIL, storage);
    }

    public String getSIGN() {
        return this.SIGN;
    }

    public void setSIGN(String sign, Storage storage) {
        this.SIGN = sign;
        this.updated("usr_sign", this.SIGN, storage);
    }

    public String getPROFILE() {
        return this.PROFILE;
    }

    public void setPROFILE(String profile, Storage storage) {
        this.PROFILE = profile;
        this.updated("usr_pic", this.PROFILE, storage);
    }

    protected void updated(final String key, final Object value, Storage storage) {
        new Thread(new Runnable() {//上传
            @Override
            public void run() {
                try {
                    WebHTTP webHTTP = new WebHTTP(Config.getInterface().setAccount);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("usr_id", ID);
                    map.put("op_type", 2);
                    map.put(key, value);
                    webHTTP.setPost(map);
                    String res = webHTTP.getStr();
                    webHTTP.close();
                    Log.e("outupdate", res);
                } catch (Exception e) {
                }
            }
        }).start();
        //本地
        if (storage != null) {
            storage.setShareId("usr");
            storage.setSharedPreference(key, value);
        }
    }

    //城市
    public IdNameMap getCity() {
        if (null != this.CITY)
            return this.CITY;
        else
            return new IdNameMap(1, "北京");
    }

    public void setCity(long city) {
        String name = IdNameMap.findNameById(city, Config.CITYS);
        this.setCity(city, name);
    }

    public void setCity(long city, String name) {
        this.setCity(new IdNameMap(city, name));
    }

    public void setCity(IdNameMap city) {
        this.CITY = city;
    }

    public void setCity(long city, Activity activity) {
        this.setCity(city);
        //上传
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebHTTP webHTTP = new WebHTTP(Config.getInterface().setCity);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("usr_id", Config.getUSR().getID());
                    map.put("ct_id", Config.getUSR().getCity().id);
                    webHTTP.setPost(map);
                    String res = webHTTP.getStr();
                    webHTTP.close();
                } catch (Exception e) {
                }
            }
        }).start();
        //存储
        Storage storage = new Storage(activity);
        storage.setShareId("usr");
        storage.setSharedPreference("ct_id", city);
        storage.setSharedPreference("ct_name", IdNameMap.findNameById(city, Config.CITYS));
    }

    //存储
    protected void store(Context context) {
        Storage storage = new Storage(context);
        storage.setShareId("usr");
        storage.setSharedPreference("id", this.ID);
        storage.setSharedPreference("ct_id", this.CITY.id);
        storage.setSharedPreference("ct_name", this.CITY.name);
        storage.setSharedPreference("usr_name", this.NAME);
        storage.setSharedPreference("usr_sex", this.GENDER);
        storage.setSharedPreference("usr_pic", this.PROFILE);
        storage.setSharedPreference("phone", this.PHONE);
        storage.setSharedPreference("usr_mail", this.MAIL);
        storage.setSharedPreference("usr_sign", this.SIGN);
    }

    protected void read(Context context) {
        long m1 = -1, z1 = 1;
        Storage storage = new Storage(context);
        storage.setShareId("usr");

        this.ID = (long) storage.getSharedPreference("id", m1);
        this.PASSWD = "";
        this.CITY = new IdNameMap(
                (long) storage.getSharedPreference("ct_id", z1),
                (String) storage.getSharedPreference("ct_name", "北京")
        );
        this.NAME = (String) storage.getSharedPreference("usr_name");
        this.PHONE = (String) storage.getSharedPreference("phone");
        this.GENDER = (int) storage.getSharedPreference("usr_sex", 1);
        this.SIGN = (String) storage.getSharedPreference("usr_sign");
        this.MAIL = (String) storage.getSharedPreference("usr_mail");
        this.PROFILE = (String) storage.getSharedPreference("usr_pic");
    }
}
