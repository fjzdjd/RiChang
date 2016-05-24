package ddw.com.richang.manager;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import org.xutils.x;

/**
 * Created by sunfusheng on 16/4/6.
 */
public class ImageManager {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";
    private Context mContext;

    public ImageManager(Context context) {
        this.mContext = context;
    }

    // 将资源ID转为Uri
    public Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + FOREWARD_SLASH +
                resourceId);
    }

    // 加载网络图片
    public void loadUrlImage(String url, ImageView imageView) {
        x.image().bind(imageView, url);
    }

    // 加载drawable图片
    public void loadResImage(int resId, ImageView imageView) {
//        x.image().bind(imageView, resId);
    }

    // 加载本地图片
    public void loadLocalImage(String path, ImageView imageView) {

    }

    // 加载网络圆型图片
    public void loadCircleImage(String url, ImageView imageView) {
        x.image().bind(imageView, url);
    }

    // 加载drawable圆型图片
    public void loadCircleResImage(int resId, ImageView imageView) {

    }

    // 加载本地圆型图片
    public void loadCircleLocalImage(String path, ImageView imageView) {

    }

}
