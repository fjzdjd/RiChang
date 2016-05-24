package ddw.com.richang.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ddw.com.richang.Activity.banner.PlanInfo;
import ddw.com.richang.model.PLAN;
import ddw.com.richang.R;
import ddw.com.richang.controller.Config;

public class AlarmPlan extends Service {
    private static boolean running=false;
    public static PLAN now=new PLAN();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(running) return;
        running=true;
        now();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                now();

                if (!Config.HINT || Config.getUSR().getID() < 1) return;

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());

                if (now.hour == 8 && now.minute < 1) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    long id1 = getId(calendar);
                    calendar.add(Calendar.DAY_OF_MONTH, 2);
                    long id3 = getId(calendar);
                    if (PLAN.list != null && PLAN.list.size() > 0) for (PLAN p : PLAN.list) {
                        if (p.hint2 && p.getDate4scroll().id == id1) alert(p, 1);
                        if (p.hint3 && p.getDate4scroll().id == id3) alert(p, 2);
                    }
                }
                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR_OF_DAY, 1);//后一小时
                if (PLAN.list != null && PLAN.list.size() > 0) for (PLAN p : PLAN.list) {
                    if (p.getDate4scroll().id == getId(calendar)  && p.hour==calendar.get(Calendar.HOUR_OF_DAY) && p.minute==calendar.get(Calendar.MINUTE)) {
                        alert(p, 0);
                    }
                }
            }
        },10*1000,1*60*1000);//1分钟刷新一次
    }

    private long getId(Calendar calendar){
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return year*12*35+month*35+day;
    }


    private static int sum=1;
    private void alert(PLAN p,int day){
        String[] h=new String[]{"前一小时","前一天","前三天"};
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.icon,p.content,System.currentTimeMillis());
//        notification.setLatestEventInfo(getApplicationContext(),"日程提醒-"+h[day],p.content,null);
        notification.flags=Notification.FLAG_ONLY_ALERT_ONCE;
        notification.defaults=Notification.DEFAULT_SOUND;
        if(0==day) notification.vibrate=new long[]{500,800,500,800,500,800,500,800,500,800};

        Intent intent=p.toIntent();
        intent.setClass(AlarmPlan.this, PlanInfo.class);
        PendingIntent pendingIntent=PendingIntent.getActivities(this,0,new Intent[]{intent},0);
        notification.contentIntent=pendingIntent;

        manager.notify(sum++,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running=false;
    }


    private void now(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        now.year = year;
        now.month = month;
        now.day = day;
        now.hour = hour;
        now.minute = minute;

        if (PLAN.date == -1) PLAN.date = now.getDate4scroll().id;
    }
}
