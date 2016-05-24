package ddw.com.richang.controller.data.internet.cache.bitmap;

import android.graphics.Bitmap;

/**
 * Created by dingdewen on 16/3/8.
 */
public abstract class BitmapExcutor {
    public BitmapManager bitmapManager;
    public void setBitmapManager(BitmapManager bitmapManager){
        this.bitmapManager=bitmapManager;
    }
    public abstract void run();
}
