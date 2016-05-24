package ddw.com.richang.manager;

import android.content.Context;
import android.content.SharedPreferences;

import ddw.com.richang.util.LogN;

/**
 *
 * Created by zzp on 2016/5/10.
 */
public class SharePreferenceManager extends BaseManager {

    private static String CONFIG_FILE_NAME = "android_riChang";
    private volatile static SharePreferenceManager instance = null;
    private SharedPreferences sharePreference = null;

    public SharePreferenceManager() {

    }

    public static SharePreferenceManager getInstance() {
        if (instance == null) {
            instance = new SharePreferenceManager();
        }

        return instance;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        sharePreference = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        String ret = defaultValue;
        if (isInit && sharePreference != null) {
            ret = sharePreference.getString(key, defaultValue);
        } else {
            LogN.e("", "init() method should call first or sp is null");
        }
        return ret;
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        int ret = defaultValue;

        if (isInit && sharePreference != null) {
            ret = sharePreference.getInt(key, defaultValue);
        } else {
            LogN.e("", "init() method should call first or sp is null");
        }

        return ret;
    }

    /**
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        if (isInit && sharePreference != null) {
            SharedPreferences.Editor ed = sharePreference.edit();
            ed.putString(key, value);
            ed.commit();
        } else {
            LogN.e("", "init() method should call first or sp is null");
        }
    }

    /**
     * 清空数据
     */
    public void clearAll() {
        if (isInit && sharePreference != null) {
            SharedPreferences.Editor ed = sharePreference.edit();
            ed.clear();
            ed.commit();
            LogN.d(this, "clear data!");
        } else {
            LogN.e(this, "清理数据失败!");
        }
    }

}
