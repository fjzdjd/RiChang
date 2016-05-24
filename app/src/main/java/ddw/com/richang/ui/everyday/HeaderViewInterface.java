package ddw.com.richang.ui.everyday;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.List;

/**
 * Created by zzp on 2016/5/17.
 */
public abstract class HeaderViewInterface<T> {

    protected Activity mContext;
    protected LayoutInflater mInflate;
    protected T mEntity;

    public HeaderViewInterface(Activity context) {
        this.mContext = context;
        mInflate = LayoutInflater.from(context);
    }

    public boolean fillView(T t, ListView listView) {
        if (t == null) {
            return false;
        }
        if ((t instanceof List) && ((List) t).size() == 0) {
            return false;
        }
        this.mEntity = t;
        getView(t, listView);
        return true;
    }

    protected abstract void getView(T t, ListView listView);

}
