package ddw.com.richang.controller.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import ddw.com.richang.controller.Config;

/**
 * Created by dingdewen on 16/1/7.
 */
public class HLView extends ViewGroup{
    public HLView(Context context){
        super(context);
    }
    public HLView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public HLView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mViewGroupWidth  = getMeasuredWidth();  //当前ViewGroup的总宽度

        int mPainterPosX = left;  //当前绘图光标横坐标位置
        int mPainterPosY = top;  //当前绘图光标纵坐标位置

        int childCount = getChildCount();
        for ( int i = 0; i < childCount; i++ ) {

            View childView = getChildAt(i);
            int width  = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();

            //如果剩余的空间不够，则移到下一行开始位置
            if( mPainterPosX + width > mViewGroupWidth ) {
                mPainterPosX = left;
                mPainterPosY += height;
            }

            //执行ChildView的绘制
            childView.layout(mPainterPosX,mPainterPosY,mPainterPosX+width, mPainterPosY+height);

            //记录当前已经绘制到的横坐标位置
            mPainterPosX += width;
        }
    }
    private int left=0,top=0;
}
