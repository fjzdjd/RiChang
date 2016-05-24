package ddw.com.richang.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.List;

import ddw.com.richang.Activity.egg.OnShake;
import ddw.com.richang.controller.Config;

public class Vibration extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private static boolean vibrate=false;
    public static boolean vable=true;

    final int INTERVAL=100;
    long lastTime=System.currentTimeMillis();
    float lastX=0,lastY=0,lastZ=0;
    @Override
    public void onCreate() {
        super.onCreate();
        if(vibrate) return;
        vibrate=true;//监听状态
        SensorManager sensorManager=(SensorManager)getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if(!Config.SHAKE) return;

                // 现在检测时间
                long currentUpdateTime = System.currentTimeMillis();
                // 两次检测的时间间隔
                long timeInterval = currentUpdateTime - lastTime;
                // 判断是否达到了检测时间间隔
                if (timeInterval < INTERVAL) return;
                // 现在的时间变成last时间
                lastTime = currentUpdateTime;
                // 获得x,y,z坐标
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                // 获得x,y,z的变化值
                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float deltaZ = z - lastZ;

                double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                        * deltaZ)
                        / timeInterval * 10000;
                // 达到速度阀值，发出提示
                if (vable && lastX * lastY * lastZ != 0 && speed >= 2500) {
                    //
                    ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> runningTaskInfos=activityManager.getRunningTasks(1);
                    String act=runningTaskInfos.get(0).topActivity.getPackageName();
                    Log.e("outac",act);
                    if(!act.equals("ddw.com.richang")) return;
                    //
                    onShake();
                    Log.e("outshake", speed + "");
                    vable = false;
                }

                // 将现在的坐标变成last坐标
                lastX = x;
                lastY = y;
                lastZ = z;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void onShake(){
        startActivity(new Intent(this, OnShake.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }



}
