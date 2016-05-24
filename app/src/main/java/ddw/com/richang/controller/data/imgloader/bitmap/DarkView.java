package ddw.com.richang.controller.data.imgloader.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import ddw.com.richang.controller.data.imgloader.core.DisplayImageOptions;
import ddw.com.richang.controller.data.imgloader.core.assist.LoadedFrom;
import ddw.com.richang.controller.data.imgloader.core.display.BitmapDisplayer;
import ddw.com.richang.controller.data.imgloader.core.imageaware.ImageAware;
import ddw.com.richang.controller.data.imgloader.core.imageaware.ImageViewAware;
import ddw.com.richang.controller.data.internet.cache.bitmap.BitmapPattern;

/**
 * Created by dingdewen on 16/5/5.
 */
public class DarkView  implements BitmapDisplayer {

    public static DisplayImageOptions BITMAPDISPLAYER=new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new DarkView()).build();

    protected  final int margin ;

    public DarkView() {
        this(0);
    }

    public DarkView(int margin) {
        this.margin = margin;
    }

    @Override
    public void display(Bitmap bmpOriginal, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        imageAware.setImageBitmap(bmpGrayscale);
    }
}
