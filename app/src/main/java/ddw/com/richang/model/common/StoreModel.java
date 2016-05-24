package ddw.com.richang.model.common;

import android.content.Context;

/**
 * Created by dingdewen on 16/1/6.
 */
public abstract class StoreModel {
    protected String mJson;
    protected abstract void store(Context context);
    protected abstract void read(Context context);
}
