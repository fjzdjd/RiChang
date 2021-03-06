package ddw.com.richang.controller.view.layout.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by dingdewen on 16/1/18.
 */
public class ObScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private boolean scrollable = true;

    public ObScrollView(Context context) {
        super(context);
    }

    public ObScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        scrollTo(x, y, scrollable);
    }

    public void scrollTo(int x, int y, boolean able) {
        if (able)
            super.scrollTo(x, y);
    }

    public void scrollTo(final int x, final int y, int mill) {
        scrollable = false;
        final int time = mill / 25 + 1;
        final int startX = getScrollX();
        final int startY = getScrollY();
        final double stepX = (x - startX) * 1.0 / time;
        final double stepY = (y - startY) * 1.0 / time;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int k = -1;
                    while (++k < time) {
                        scrollTo(
                                (int) (startX + stepX * k),
                                (int) (startY + stepY * k),
                                true
                        );
                        Thread.sleep(25);
                    }
                } catch (Exception e) {
                } finally {
                    scrollTo(x, y, true);
                    scrollable = true;
                }
            }
        }).start();
    }
}


