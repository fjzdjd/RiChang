package ddw.com.richang.ui.column;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;

/**
 * 设计
 * Created by zzp on 2016/6/20.
 */
public class DesignFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.column_design_layout, container, false);
        x.view().inject(this, view);
        return view;


    }
}
