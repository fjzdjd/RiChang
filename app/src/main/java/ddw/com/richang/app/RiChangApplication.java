package ddw.com.richang.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import org.xutils.x;

import java.io.File;

import ddw.com.richang.controller.data.imgloader.cache.disc.impl.UnlimitedDiskCache;
import ddw.com.richang.controller.data.imgloader.cache.disc.naming.Md5FileNameGenerator;
import ddw.com.richang.controller.data.imgloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import ddw.com.richang.controller.data.imgloader.core.DisplayImageOptions;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.controller.data.imgloader.core.ImageLoaderConfiguration;
import ddw.com.richang.controller.data.imgloader.core.assist.QueueProcessingType;
import ddw.com.richang.controller.data.imgloader.core.download.BaseImageDownloader;
import ddw.com.richang.manager.SharePreferenceManager;
import ddw.com.richang.service.LocationService;

/**
 * 程序入口
 * Created by zzp on 2016/5/10.
 */
public class RiChangApplication extends Application {

    public static RiChangApplication instance;
    public LocationService locationService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
        /**
         * 初始化sharePreference
         */
        SharePreferenceManager.getInstance().init(this);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());


    }

}
