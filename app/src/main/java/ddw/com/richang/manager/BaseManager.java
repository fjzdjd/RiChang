package ddw.com.richang.manager;

import android.content.Context;

/**
 * 初始化sharePreference单例模式
 * Created by zzp on 2016/5/10.
 */
public class BaseManager {
    protected Context appContext;

    protected boolean isInit = false;

    public void init(Context context) {
        isInit = true;
        this.appContext = context;
    }
}
