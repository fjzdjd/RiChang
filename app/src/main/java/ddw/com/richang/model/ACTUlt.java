package ddw.com.richang.model;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ddw.com.richang.controller.Config;
import ddw.com.richang.controller.data.oldversion.WebHTTP;
import ddw.com.richang.model.common.IdNameMap;

/**
 * Created by dingdewen on 16/1/17.
 */
public class ACTUlt {
    public long pl_id;
    public boolean hint1;
    public boolean hint2;
    public boolean hint3;
    public String pubpic;
    public String pubname;
    public long pubId;
    private long id;
    private String poster;
    private String posterTop;
    private String title;
    private String time;
    private String place;
    private long collectSum;
    private long praiseSum;
    private long readSum;
    private String pay;
    private String size;
    private String speaker;
    private String sustain;
    private IdNameMap theme;
    private ArrayList<IdNameMap> tags;
    private String desc;
    private String html;
    private String phone;
    private boolean collected;
    private boolean planed;
    private int status;
    private String msg;

    private ACTUlt() {
    }

    public ACTUlt(final long id) {
        this.id = id;
        if (id < 1) {
            this.status = 404;
            return;
        }
        try {
            WebHTTP w = new WebHTTP(Config.getInterface().getActivityContent);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ac_id", id);
            map.put("usr_id", Config.getUSR().getID());
            w.setPost(map);
            String res = w.getStr();
            Log.e("outact", res);
            w.close();
            JSONObject obj = new JSONObject(res);
            final JSONObject data = obj.getJSONObject("data");
            this.status = obj.getInt("code");
            if (200 == this.status) {//
                this.poster = data.getString("ac_poster");
                this.posterTop = data.getString("ac_poster_top");
                this.title = data.getString("ac_title");
                this.time = data.getString("ac_time");
                this.place = data.getString("ac_place");
                this.collectSum = data.getLong("ac_collect_num");
                //this.praiseSum=data.getLong("ac_praise_num");
                this.readSum = data.getLong("ac_read_num");
                this.pay = data.getString("ac_pay");
                this.size = data.getString("ac_size");
                this.speaker = data.getString("ac_speaker");
                this.sustain = data.getString("ac_sustain_time");
                JSONObject thm = data.getJSONObject("ac_theme");
                this.theme = new IdNameMap(thm.getLong("theme_id"), thm.getString("theme_name"));
                this.tags = new ArrayList<IdNameMap>();
                JSONArray arrTags = data.getJSONArray("ac_tags");
                for (int i = 0; i < arrTags.length(); i++) {
                    JSONObject tg = arrTags.getJSONObject(i);
                    this.tags.add(new IdNameMap(tg.getLong("tag_id"), tg.getString("tag_name")));
                }
                this.desc = data.getString("ac_desc");
                this.html = data.getString("ac_html");
                this.phone = data.getString("ac_phone");
                this.collected = (1 == data.getInt("ac_collect"));
                this.pubId = data.getLong("usr_id");
                this.pubname = data.getString("usr_name");
                this.pubpic = data.getString("usr_pic");
                this.planed = (1 == data.getInt("plan"));
                if (this.planed) {
                    this.pl_id = data.getLong("pl_id");
                    this.hint1 = (1 == data.getInt("pl_alarm_one"));
                    this.hint2 = (1 == data.getInt("pl_alarm_two"));
                    this.hint3 = (1 == data.getInt("pl_alarm_three"));
                } else {
                    this.pl_id = -1;
                    this.hint1 = false;
                    this.hint2 = false;
                    this.hint3 = false;
                }
            } else
                this.msg = obj.getString("msg");
        } catch (Exception e) {
            Log.e("outerr", e.getMessage());
        }
    }

    public long getId() {
        return this.id;
    }

    public String getPoster() {
        return this.poster;
    }

    public String getPosterTop() {
        return this.posterTop;
    }

    public String getTime() {
        return this.time;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPlace() {
        return this.place;
    }

    public long getCollectSum() {
        return this.collectSum;
    }

    public long getPraiseSum() {
        return this.praiseSum;
    }

    public long getReadSum() {
        return this.readSum;
    }

    public String getPay() {
        return this.pay;
    }

    public String getSize() {
        return this.size;
    }

    public String getSpeaker() {
        return this.speaker;
    }

    public String getSustain() {
        return this.sustain;
    }

    public IdNameMap getTheme() {
        return this.theme;
    }

    public ArrayList<IdNameMap> getTags() {
        return this.tags;
    }

    public String getTagsStr() {
        if (null == this.tags || 0 == this.tags.size())
            return "";
        String res = "";
        for (int i = 0; i < this.tags.size(); i++) {
            res += (" " + this.tags.get(i).name);
        }
        return res;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getHtml() {
        return this.html;
    }

    public String getPhone() {
        return this.phone;
    }

    public boolean isCollected() {
        return this.collected;
    }

    public boolean isPlaned() {
        return this.planed;
    }

    public void changeCollected(final Activity activity, final Runnable succ, final Runnable
            error) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebHTTP webHTTP = new WebHTTP(Config.getInterface().setCollection);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("usr_id", Config.getUSR().getID());
                    map.put("ac_id", getId());
                    map.put("op_type", collected ? 2 : 1);
                    webHTTP.setPost(map);
                    String res = webHTTP.getStr();
                    JSONObject obj = new JSONObject(res);
                    webHTTP.close();
                    if (activity != null) {
                        if (200 == obj.getInt("code")) {
                            collected = !collected;
                            if (succ != null)
                                activity.runOnUiThread(succ);
                        } else if (error != null)
                            activity.runOnUiThread(error);
                    }
                } catch (Exception e) {
                    if (activity != null && error != null) {
                        activity.runOnUiThread(error);
                    }
                }
            }
        }).start();
    }

    public void addPlan(final PLAN plan, final Activity activity, final Runnable succ, final
    Runnable error) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebHTTP webHTTP = new WebHTTP(Config.getInterface().joinPlan);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("usr_id", Config.getUSR().getID());
                    map.put("op_type", 1);
                    map.put("ac_id", id);
                    webHTTP.setPost(map);
                    String res = webHTTP.getStr();
                    Log.e("outjoinplan", res);
                    JSONObject obj = new JSONObject(res);
                    webHTTP.close();
                    if (activity != null) {
                        if (200 == obj.getInt("code")) {
                            planed = !planed;
                            plan.id = obj.getLong("pl_id");
                            //向列表中添加
                            if (0 == PLAN.list.size()) {
                                PLAN.list.add(plan);
                            } else if (plan.getDate4scroll().id > PLAN.list.get(PLAN.list.size()
                                    - 1).getDate4scroll().id) {
                                PLAN.list.add(plan);
                            } else if (plan.getDate4scroll().id < PLAN.list.get(0).getDate4scroll
                                    ().id) {
                                PLAN.list.add(0, plan);
                            } else if (1 == PLAN.list.size()) {
                                if (PLAN.list.get(0).getDate4scroll().id > plan.getDate4scroll()
                                        .id) {
                                    PLAN.list.add(0, plan);
                                } else {
                                    PLAN.list.add(plan);
                                }
                            } else
                                for (int i = 0; i < PLAN.list.size() - 1; i++) {
                                    if (plan.getDate4scroll().id >= PLAN.list.get(i)
                                            .getDate4scroll().id && plan.getDate4scroll().id <=
                                            PLAN.list.get(i + 1).getDate4scroll().id) {
                                        PLAN.list.add(i + 1, plan);
                                        break;
                                    }
                                }//
                            PLAN.date = plan.getDate4scroll().id;
                            if (succ != null)
                                activity.runOnUiThread(succ);
                        } else if (error != null)
                            activity.runOnUiThread(error);
                    }
                } catch (Exception e) {
                    Log.e("outerror", e.getMessage());
                    if (activity != null && error != null) {
                        activity.runOnUiThread(error);
                    }
                }
            }
        }).start();
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.msg;
    }
}
