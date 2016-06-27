package ddw.com.richang.ui.everyday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;

/**
 * 发布者推荐
 * Created by zzp on 2016/6/27.
 */
public class RecommendPublisherFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.everyday_publisher_layout, container, false);

        x.view().inject(getActivity(), view);

        return view;
    }
}
