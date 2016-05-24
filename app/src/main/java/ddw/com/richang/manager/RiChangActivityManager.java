package ddw.com.richang.manager;

import android.content.Intent;

import java.util.Stack;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseActivity;

/**
 * Created by zzp on 2016/5/10.
 */
public class RiChangActivityManager {

    private static volatile RiChangActivityManager instance;

    private volatile Stack<BaseActivity> activityStack = new Stack<>();

    public RiChangActivityManager() {
        instance = this;
    }

    /**
     * 创建单例类，提供静态方法调用
     *
     * @return ActivityManager
     */
    public static RiChangActivityManager getInstance() {
        if (instance == null) {
            instance = new RiChangActivityManager();
        }
        return instance;
    }


    /**
     * 退出Activity
     *
     * @param activity BaseActivity
     */
    public void popActivity(BaseActivity activity) {
        if (activity != null) {

            activityStack.remove(activity);
        }
    }

    /**
     * 获得当前栈顶的Activity
     *
     * @return BaseActivity BaseActivity
     */
    public BaseActivity currentActivity() {
        BaseActivity activity = null;
        if (!activityStack.empty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 将当前Activity推入栈中
     *
     * @param activity BaseActivity
     */
    public void pushActivity(BaseActivity activity) {

        activityStack.add(activity);
    }

    /**
     * 退出栈中其他所有Activity
     *
     * @param cls Class 类名
     */
    public void popOtherActivity(Class<? extends BaseActivity> cls) {
        if (null == cls) {
            return;
        }

        for (BaseActivity activity : activityStack) {
            if (null == activity || activity.getClass().equals(cls)) {
                continue;
            }
            activity.finish();
        }

    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivity() {
        while (true) {
            BaseActivity activity = currentActivity();
            if (activity == null) {
                break;
            }
            activity.finish();
            popActivity(activity);
        }

    }

    /**
     * 添加统一跳转接口，统一界面跳转动画
     *
     * @param intent      意图
     * @param requestCode
     */
    public void startActivityForResult(Intent intent, int requestCode, Class<? extends
            BaseActivity> activityClass) {
        BaseActivity curActivity = currentActivity();

        if (null == intent) {
            intent = new Intent();
        }

        intent.setClass(curActivity, activityClass);
        curActivity.startActivityForResult(intent, requestCode);
        curActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    /**
     * 添加统一跳转接口，统一界面跳转动画
     *
     * @param activityClass 要跳转的activity的calss
     */
    public void startNextActivity(Class<? extends BaseActivity> activityClass) {
        Intent intent = new Intent();
        startNextActivity(intent, activityClass);
    }

    /**
     * 添加统一跳转接口，统一界面跳转动画
     *
     * @param intent        页面定义的Intent
     * @param activityClass 要跳转的activity的calss
     */
    public void startNextActivity(Intent intent, Class<? extends BaseActivity> activityClass) {
        BaseActivity curActivity = currentActivity();
        intent.setClass(curActivity, activityClass);
        curActivity.startActivity(intent);
        curActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }


}
