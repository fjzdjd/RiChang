package ddw.com.richang.ui.login;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.util.DensityUtil;

/**
 * Created by zzp
 * 网络图片加载例子,也可用作页面加载
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageResource(R.mipmap.imggrey);//空白图片
        //对背景图片进行裁剪
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();
        x.image().bind(imageView, data,imageOptions);



    }
}
