package ddw.com.richang.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ddw.com.richang.custom.CustomUi.WaitingAlertDialog;
import ddw.com.richang.util.LogN;

/**
 * 新建fragment继承的基类
 * Created by zzp on 2016/5/10.
 */
public abstract class BaseFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogN.d(this, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        LogN.d(this, "onCreateView");

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        LogN.d(this, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogN.d(this, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogN.d(this, "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogN.d(this, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissWaitDialog();
        LogN.d(this, "onDestroy");
    }


    private WaitingAlertDialog waitDialog;

    public void showWaitDialog(int textRes) {
        if (null == waitDialog) {
            waitDialog = new WaitingAlertDialog(getActivity(), textRes);
        } else {
            waitDialog.setShowText(textRes);
            if (!waitDialog.isShown()) {
                waitDialog.showUp();
            }
        }
    }

    public void dismissWaitDialog() {
        if (null != waitDialog) {
            waitDialog.dismiss();
        }
    }


}
