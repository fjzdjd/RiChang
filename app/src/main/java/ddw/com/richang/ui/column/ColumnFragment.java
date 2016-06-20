package ddw.com.richang.ui.column;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import ddw.com.richang.R;
import ddw.com.richang.base.BaseFragment;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.controller.InterFace;
import ddw.com.richang.util.LogN;

/**
 * 专栏
 * Created by zzp on 2016/5/13.
 */
public class ColumnFragment extends BaseFragment {

    @ViewInject(R.id.column_tlt_menu)
    TabLayout mTabMenu;

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



        return view;
    }

    private void initWidgets() {
        mContentViewPager.setAdapter(new ColumnChildFragment(getFragmentManager(), mSparseArray));

        mTabMenu.setupWithViewPager(mContentViewPager);

        mTabMenu.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mContentViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mContentViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mContentViewPager.setCurrentItem(tab.getPosition());
            }
        });
    }


    /**
     * 初始化菜单参数
     */
    private void initMenuData() {
        RequestParams params = new RequestParams(InterFace.getInstance().getAllIndustries);
        // 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
        params.setCacheMaxAge(1000 * 60);
        Callback.Cancelable cancelable
                = x.http().get(params, new Callback.CacheCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) {
                this.result = result;
                return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.optString("code");
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (code.equals(ConstantData.CODE)) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                String menuName = jsonObject1.optString("ind_name");
                                String ind_id = jsonObject1.optString("ind_id");
                                mSparseArray.put(Integer.parseInt(ind_id), menuName);


                            }

                        }

                        initWidgets();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    /**
     * viewpager Fragment adapter
     */
    private class ColumnChildFragment extends FragmentPagerAdapter {

        private SparseArray<String> mSparseArray;

        public ColumnChildFragment(FragmentManager fm, SparseArray<String>
                sparseArray) {
            super(fm);
            this.mSparseArray = sparseArray;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChoicenessFragment();

                case 1:
                    return new ComprehensiveFragment();

                case 2:
                    return new MediaFragment();

                case 3:
                    return new ChuangYeFragment();

                case 4:
                    return new FinanceFragment();

                case 5:
                    return new DesignFragment();

                case 6:
                    return new UniversityFragment();

                default:

                    return null;
            }
        }

        @Override
        public int getCount() {
            return mSparseArray.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mSparseArray.get(position + 1);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);

            LogN.d(this, "current pager");

        }
    }
}
