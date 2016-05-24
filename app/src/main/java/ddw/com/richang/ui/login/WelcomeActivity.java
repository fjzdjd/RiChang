package ddw.com.richang.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import ddw.com.richang.R;
import ddw.com.richang.app.RiChangApplication;
import ddw.com.richang.base.BaseActivity;
import ddw.com.richang.service.LocationService;
import ddw.com.richang.util.LogN;

/**
 * 2.0欢饮界面
 * Created by zzp on 2016/5/12.
 */
public class WelcomeActivity extends BaseActivity implements Runnable {


    private Button mStartRiChang;
    /**
     * 是否第一次使用
     */
    private boolean isFirstUse;
    /**
     * 版本号
     */
    private TextView mTxtVersion;
    /**
     * 项目包名
     */
    private String mPackName;
    /**
     * 版本
     */
    private String mVersionName;
    /**
     * 预留更改
     */
    private ImageView mStartUI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.login_welcome_activity_layout);

        initWidgets();
        initDatas();

        //开启线程
        new Thread(WelcomeActivity.this).start();
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        //预留后台更改启动画面接口
//        mStartUI.setBackground();
    }

    /**
     * 初始化界面
     */
    private void initWidgets() {

        mStartRiChang = (Button) findViewById(R.id.login_welcome_btn_start);
        mStartUI = (ImageView) findViewById(R.id.login_welcome_img_start);

        mStartRiChang.setOnClickListener(new WelcomeOnClickListener());
    }

    @Override
    public void run() {
        /**
         * 延迟两秒时间
         */
        try {
            Thread.sleep(2000);
            //读取SharedPreferences中需要的数据

            SharedPreferences preferences = getSharedPreferences("isFirstUse", MODE_WORLD_READABLE);

            isFirstUse = preferences.getBoolean("isFirstUse", true);

            /**
             * 如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
             */
            if (isFirstUse) {
                riChangActivityManager.startNextActivity(GuideActivity.class);
            } else {
                //如果有登录就不跳转登录界面
                riChangActivityManager.startNextActivity(LoginActivity.class);
            }

            WelcomeActivity.this.finish();
            // 实例化Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            // 存入数据
            editor.putBoolean("isFirstUse", false);
            // 提交修改
            editor.commit();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((RiChangApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);

        locationService.setLocationOption(locationService.getDefaultLocationClientOption());

        locationService.start();// 定位SDK

    }

    private LocationService locationService;
    @Override
    protected void onStop() {
        super.onStop();
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /**
     * welcome界面点击事件
     */
    private class WelcomeOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //开启日常
                case R.id.login_welcome_btn_start:
//                    riChangActivityManager.startNextActivity(MainActivity.class);
//                    WelcomeActivity.this.finish();

                    break;


                default:
                    break;
            }

        }
    }

    /*****
     * @see copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                LogN.d(this,sb.toString());
            }
        }

    };
}
