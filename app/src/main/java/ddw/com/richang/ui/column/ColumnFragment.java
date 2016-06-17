package ddw.com.richang.ui.column;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;

/**
 * 专栏
 * Created by zzp on 2016/5/13.
 */
public class ColumnFragment extends BaseFragment {

    @ViewInject(R.id.column_slv_menu)
    HorizontalScrollView mHorizontalMenu;

    @ViewInject(R.id.column_llt_menu)
    LinearLayout mLayoutMenu;

    @ViewInject(R.id.column_vpr_pager)
    ViewPager mContentViewPager;

    private SparseArray<String> mSparseArray = new SparseArray<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.column_fragment_layout, container, false);
        x.view().inject(this, view);

        initMenuData();

        createMenuLayout();

        return view;
    }

    /**
     * 创建菜单栏目
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void createMenuLayout() {
        for (int i = 1; i <= mSparseArray.size(); i++) {
            LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout myLinear = new LinearLayout(getActivity());
            linearLp.setMargins(10, 0, 10, 0);
            myLinear.setOrientation(LinearLayout.VERTICAL);
            myLinear.setTag(i);
            myLinear.setPadding(5, 5, 5, 0);
            myLinear.setGravity(Gravity.CENTER);
            mLayoutMenu.addView(myLinear, linearLp);

            LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(
                    125, 100);
            TextView textView = new TextView(getActivity());
            textView.setText(mSparseArray.get(i));
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            myLinear.addView(textView, textViewLp);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    120, 10);
            TextView textView1 = new TextView(getActivity());
            textView1.setBackgroundColor(getResources().getColor(R.color.mainColor));
            myLinear.addView(textView1, lp);


            myLinear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity(), v.getTag().toString(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        mHorizontalMenu.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int
                    oldScrollY) {

            }
        });
    }

    /**
     * 初始化菜单参数
     */
    private void initMenuData() {
        mSparseArray.put(1, "精选");
        mSparseArray.put(2, "综合");
        mSparseArray.put(3, "传媒");
        mSparseArray.put(4, "创业");
        mSparseArray.put(5, "金融");
        mSparseArray.put(6, "设计");
        mSparseArray.put(7, "大学");
    }
}
