package ddw.com.richang.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ddw.com.richang.R;
import ddw.com.richang.custom.CustomUi.WaitingAlertDialog;
import ddw.com.richang.manager.RiChangActivityManager;
import ddw.com.richang.util.LogN;

/**
 * 新建activity继承的基类
 * Created by zzp on 2016/5/10.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected boolean isActive;

    protected RiChangActivityManager riChangActivityManager = RiChangActivityManager.getInstance();
    private WaitingAlertDialog waitDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        riChangActivityManager.pushActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogN.d(this, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogN.d(this, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogN.d(this, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogN.d(this, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogN.d(this, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissWaitDialog();
        isActive = false;
        riChangActivityManager.popActivity(this);
        LogN.d(this, "onDestroy");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    /**
     * 显示软键盘
     *
     * @param view
     */
    public void showSoftInput(View view) {
        if (null == view) {
            LogN.e(this, "showSoftInput | view is null");
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        if (null == getCurrentFocus()) {
            LogN.w(this, "hideSoftInput currFocus is null");
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager
                .HIDE_NOT_ALWAYS);
    }


    /**
     * 显示菊花
     *
     * @param textRes 菊花的上的字
     * @param color   菊花的颜色
     */
    public void showWaitDialog(String textRes, int color) {
        if (null == waitDialog) {
            waitDialog = new WaitingAlertDialog(this, textRes, color);
        } else {
            waitDialog.setShowText(textRes);
            if (!waitDialog.isShown()) {
                waitDialog.showUp();
            }
        }
    }

    /**
     * 显示progressBar
     *
     * @param textRes 需要提示的字
     */
    public void showWaitDialog(int textRes) {
        if (null == waitDialog) {
            waitDialog = new WaitingAlertDialog(this, textRes);
        } else {
            waitDialog.setShowText(textRes);
            if (!waitDialog.isShown()) {
                waitDialog.showUp();
            }
        }
    }

    /**
     * 销毁progressBar
     */
    public void dismissWaitDialog() {
        if (null != waitDialog) {
            waitDialog.dismiss();
        }
    }


}
