package ddw.com.richang.controller.view.modalFrame;

import android.app.Activity;
import android.view.View;

/**
 * Created by dingdewen on 16/1/18.
 */
public class FrameSupport {
    protected Activity activity;
    protected View rootView;
    public FrameSupport(Activity activity,View rootView){
        this.activity=activity;
    }

    protected View model;
    protected void showModel(){

    }
}
