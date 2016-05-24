package ddw.com.richang.ui.everyday;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import ddw.com.richang.R;
import ddw.com.richang.components.ui.CustomUi.FilterView;

/**
 * 筛选器
 * Created by zzp on 2016/5/17.
 */
public class HeaderFilterViewView extends HeaderViewInterface<Object> implements FilterView
        .OnFilterClickListener {

    private FilterView fvFilter;
    private OnFilterClickListener onFilterClickListener;

    public HeaderFilterViewView(Activity context) {
        super(context);
    }

    @Override
    protected void getView(Object o, ListView listView) {

        View view = mInflate.inflate(R.layout.everyday_header_filter_layout, listView, false);

        fvFilter = (FilterView) view.findViewById(R.id.everyday_filter);

        dealWithTheView(o);
        listView.addHeaderView(view);
    }

    // 获得筛选View
    public FilterView getFilterView() {
        return fvFilter;
    }

    private void dealWithTheView(Object obj) {
        fvFilter.setOnFilterClickListener(this);
    }

    @Override
    public void onFilterClick(int position) {
        if (onFilterClickListener != null) {
            onFilterClickListener.onFilterClick(position);
        }
    }

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }

}
