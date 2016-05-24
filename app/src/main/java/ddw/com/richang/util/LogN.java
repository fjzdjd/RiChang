package ddw.com.richang.util;


import android.util.Log;

/**
 * 统一日志打印
 *
 * @author zzp
 */
public class LogN {

    /**
     * 默认true ，false将关闭所有DEBUG打印
     */
    private static final boolean DEBUG = true;

    /**
     * 默认true ，false将关闭所有WARNING打印
     */
    private static final boolean WARNING = true;

    /**
     * 默认true ，false将关闭所有INFO打印
     */
    private static final boolean INFO = true;

    /**
     * 默认true ，false将关闭所有ERROR打印
     */
    private static final boolean ERROR = true;

    private static final String TAG = "CheHui.DuoBao";

    public static void i(Object nvsObject, String msg) {
        if (null == nvsObject) {
            return;
        }
        i(nvsObject.getClass(), msg);
    }

    public static void w(Object nvsObject, String msg) {
        if (null == nvsObject) {
            return;
        }
        w(nvsObject.getClass(), msg);
    }

    public static void d(Object nvsObject, String msg) {
        if (null == nvsObject) {
            return;
        }
        d(nvsObject.getClass(), msg);
    }

    public static void e(Object nvsObject, String msg) {
        if (null == nvsObject) {
            return;
        }
        e(nvsObject.getClass(), msg);
    }

    public static void i(Class<? extends Object> nvsObject, String msg) {
        if (INFO && null != nvsObject) {
            Log.i(TAG, nvsObject.getSimpleName() + " | " + msg);
        }
    }

    public static void w(Class<? extends Object> nvsObject, String msg) {
        if (WARNING && null != nvsObject) {
            Log.w(TAG, nvsObject.getSimpleName() + " | " + msg);
        }
    }

    public static void d(Class<? extends Object> nvsObject, String msg) {
        if (DEBUG && null != nvsObject) {
            Log.d(TAG, nvsObject.getSimpleName() + " | " + msg);
        }
    }

    public static void e(Class<? extends Object> nvsObject, String msg) {
        if (ERROR && null != nvsObject) {
            Log.e(TAG, nvsObject.getSimpleName() + " | " + msg);
        }
    }

}
