package ddw.com.richang.ui.agenda;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;

/**
 * 行程
 * Created by zzp on 2016/5/13.
 */
public class AgendaFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.agenda_fragment_layout, container, false);

        x.view().inject(getActivity(), view);

        return view;
    }
}
