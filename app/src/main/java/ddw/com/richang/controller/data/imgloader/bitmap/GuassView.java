package ddw.com.richang.controller.data.imgloader.bitmap;

import android.graphics.Bitmap;

import ddw.com.richang.controller.data.imgloader.core.DisplayImageOptions;
import ddw.com.richang.controller.data.imgloader.core.assist.LoadedFrom;
import ddw.com.richang.controller.data.imgloader.core.display.BitmapDisplayer;
import ddw.com.richang.controller.data.imgloader.core.imageaware.ImageAware;
import ddw.com.richang.controller.data.imgloader.core.imageaware.ImageViewAware;
import ddw.com.richang.controller.data.internet.cache.bitmap.BitmapPattern;

/**
 * Created by dingdewen on 16/3/22.
 */
public class GuassView implements BitmapDisplayer{

    public static DisplayImageOptions BITMAPDISPLAYER=new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).displayer(new GuassView()).build();

    protected  final int margin ;

    public GuassView() {
        this(0);
    }

    public GuassView(int margin) {
        this.margin = margin;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        bitmap = new BitmapPattern(bitmap).convertHori().scale(0.6f).dark(0.5f).guass(50).getBitmap();
        imageAware.setImageBitmap(bitmap);
    }
}
