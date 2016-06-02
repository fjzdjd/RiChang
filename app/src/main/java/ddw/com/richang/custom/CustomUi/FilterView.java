package ddw.com.richang.custom.CustomUi;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ddw.com.richang.R;
import ddw.com.richang.adapter.FilterLeftAdapter;
import ddw.com.richang.adapter.FilterOneAdapter;
import ddw.com.richang.adapter.FilterRightAdapter;
import ddw.com.richang.model.filter.FilterData;
import ddw.com.richang.model.filter.FilterEntity;
import ddw.com.richang.model.filter.FilterTwoEntity;

/**
 * @author zzp
 */
public class FilterView extends LinearLayout implements View.OnClickListener {

    TextView tvCategory;

    ImageView ivCategoryArrow;

    TextView tvSort;

    ImageView ivSortArrow;

    TextView tvFilter;

    ImageView ivFilterArrow;

    LinearLayout llCategory;

    LinearLayout llSort;

    LinearLayout llFilter;

    ListView lvLeft;

    ListView lvRight;

    LinearLayout llHeadLayout;

    LinearLayout llContentListView;

    View viewMaskBg;

    private Context mContext;
    private Activity mActivity;
    private boolean isStickyTop = false; // 是否吸附在顶部
    private boolean isShowing = false;
    private int filterPosition = -1;
    private int panelHeight;
    private FilterData filterData;

    private FilterTwoEntity selectedCategoryEntity; // 被选择的分类项
    private FilterEntity selectedSortEntity; // 被选择的排序项
    private FilterEntity selectedFilterEntity; // 被选择的筛选项

    private FilterLeftAdapter leftAdapter;
    private FilterRightAdapter rightAdapter;
    private FilterOneAdapter sortAdapter;
    private FilterOneAdapter filterAdapter;
    // 筛选视图点击
    private OnFilterClickListener onFilterClickListener;
    // 分类Item点击
    private OnItemCategoryClickListener onItemCategoryClickListener;
    // 排序Item点击
    private OnItemSortClickListener onItemSortClickListener;
    // 筛选Item点击
    private OnItemFilterClickListener onItemFilterClickListener;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_layout, this);

        initData();
        initView(view);
        initListener();
    }

    private void initData() {

    }

    private void initView(View view) {

        tvCategory = (TextView) view.findViewById(R.id.tv_category);
        tvSort = (TextView) view.findViewById(R.id.tv_sort);
        ivCategoryArrow = (ImageView) view.findViewById(R.id.iv_category_arrow);
        ivSortArrow = (ImageView) view.findViewById(R.id.iv_sort_arrow);
        tvFilter = (TextView) view.findViewById(R.id.tv_filter);
        ivFilterArrow = (ImageView) view.findViewById(R.id.iv_filter_arrow);
        llCategory = (LinearLayout) view.findViewById(R.id.ll_category);
        llSort = (LinearLayout) view.findViewById(R.id.ll_sort);
        llFilter = (LinearLayout) view.findViewById(R.id.ll_filter);
        lvLeft = (ListView) view.findViewById(R.id.lv_left);
        lvRight = (ListView) view.findViewById(R.id.lv_right);
        llHeadLayout = (LinearLayout) view.findViewById(R.id.ll_head_layout);
        llContentListView = (LinearLayout) view.findViewById(R.id.ll_content_list_view);
        viewMaskBg = view.findViewById(R.id.view_mask_bg);


        viewMaskBg.setVisibility(GONE);
        llContentListView.setVisibility(GONE);
    }

    private void initListener() {
        llCategory.setOnClickListener(this);
        llSort.setOnClickListener(this);
        llFilter.setOnClickListener(this);
        viewMaskBg.setOnClickListener(this);
        llContentListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_category:
                filterPosition = 0;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_sort:
                filterPosition = 1;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_filter:
                filterPosition = 2;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.view_mask_bg:
                hide();
                break;
        }

    }

    // 复位筛选的显示状态
    public void resetFilterStatus() {
        tvCategory.setTextColor(mContext.getResources().getColor(R.color.font_black_2));
        ivCategoryArrow.setImageResource(R.mipmap.home_down_arrow);
        tvSort.setTextColor(mContext.getResources().getColor(R.color.font_black_2));
        ivSortArrow.setImageResource(R.mipmap.home_down_arrow);

        tvFilter.setTextColor(mContext.getResources().getColor(R.color.font_black_2));
        ivFilterArrow.setImageResource(R.mipmap.home_down_arrow);
    }

    // 复位所有的状态
    public void resetAllStatus() {
        resetFilterStatus();
        hide();
    }

    // 显示筛选布局
    public void showFilterLayout(int position) {
        resetFilterStatus();
        switch (position) {
            case 0:
                setCategoryAdapter();
                break;
            case 1:
                setSortAdapter();
                break;
            case 2:
                setFilterAdapter();
                break;
        }

        if (isShowing)
            return;
        show();
    }

    // 设置分类数据
    private void setCategoryAdapter() {
        tvCategory.setTextColor(mActivity.getResources().getColor(R.color.orange));
        ivCategoryArrow.setImageResource(R.mipmap.home_up_arrow);
        lvLeft.setVisibility(VISIBLE);
        lvRight.setVisibility(VISIBLE);

        if (selectedCategoryEntity == null) {
            selectedCategoryEntity = filterData.getCategory().get(0);
        }

        // 左边列表视图
        leftAdapter = new FilterLeftAdapter(mContext, filterData.getCategory());
        lvLeft.setAdapter(leftAdapter);
        leftAdapter.setSelectedEntity(selectedCategoryEntity);

        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryEntity = filterData.getCategory().get(position);
                leftAdapter.setSelectedEntity(selectedCategoryEntity);

                // 右边列表视图
                rightAdapter = new FilterRightAdapter(mContext, selectedCategoryEntity.getList());
                lvRight.setAdapter(rightAdapter);
                lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long
                            id) {
                        selectedCategoryEntity.setSelectedFilterEntity(selectedCategoryEntity
                                .getList().get(position));
                        rightAdapter.setSelectedEntity(selectedCategoryEntity
                                .getSelectedFilterEntity());
                        hide();
                        if (onItemCategoryClickListener != null) {
                            onItemCategoryClickListener.onItemCategoryClick(selectedCategoryEntity);
                        }
                    }
                });
            }
        });

        // 如果右边有选中的数据，设置
        if (selectedCategoryEntity.getSelectedFilterEntity() != null) {
            rightAdapter = new FilterRightAdapter(mContext, selectedCategoryEntity.getList());
        } else {
            rightAdapter = new FilterRightAdapter(mContext, filterData.getCategory().get(0)
                    .getList());
        }
        lvRight.setAdapter(rightAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryEntity.setSelectedFilterEntity(selectedCategoryEntity.getList()
                        .get(position));
                rightAdapter.setSelectedEntity(selectedCategoryEntity.getSelectedFilterEntity());
                hide();
                if (onItemCategoryClickListener != null) {
                    onItemCategoryClickListener.onItemCategoryClick(selectedCategoryEntity);
                }
            }
        });
    }

    // 设置排序数据
    private void setSortAdapter() {
        tvSort.setTextColor(mActivity.getResources().getColor(R.color.orange));
        ivSortArrow.setImageResource(R.mipmap.home_up_arrow);
        lvLeft.setVisibility(GONE);
        lvRight.setVisibility(VISIBLE);
        sortAdapter = new FilterOneAdapter(mContext, filterData.getSorts());
        lvRight.setAdapter(sortAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSortEntity = filterData.getSorts().get(position);
                sortAdapter.setSelectedEntity(selectedSortEntity);
                hide();
                if (onItemSortClickListener != null) {
                    onItemSortClickListener.onItemSortClick(selectedSortEntity);
                }
            }
        });
    }

    // 设置筛选数据
    private void setFilterAdapter() {
        tvFilter.setTextColor(mActivity.getResources().getColor(R.color.orange));
        ivFilterArrow.setImageResource(R.mipmap.home_up_arrow);
        lvLeft.setVisibility(GONE);
        lvRight.setVisibility(VISIBLE);
        filterAdapter = new FilterOneAdapter(mContext, filterData.getFilters());
        lvRight.setAdapter(filterAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFilterEntity = filterData.getFilters().get(position);
                filterAdapter.setSelectedEntity(selectedFilterEntity);
                hide();
                if (onItemFilterClickListener != null) {
                    onItemFilterClickListener.onItemFilterClick(selectedFilterEntity);
                }
            }
        });
    }

    // 动画显示
    private void show() {
        isShowing = true;
        viewMaskBg.setVisibility(VISIBLE);
        llContentListView.setVisibility(VISIBLE);
        llContentListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llContentListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                panelHeight = llContentListView.getHeight();
                ObjectAnimator.ofFloat(llContentListView, "translationY", -panelHeight, 0)
                        .setDuration(200).start();
            }
        });
    }

    // 隐藏动画
    public void hide() {
        isShowing = false;
        resetFilterStatus();
        viewMaskBg.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(llContentListView, "translationY", 0, -panelHeight).setDuration
                (200).start();
    }

    // 是否吸附在顶部
    public void setStickyTop(boolean stickyTop) {
        isStickyTop = stickyTop;
    }

    // 设置筛选数据
    public void setFilterData(Activity activity, FilterData filterData) {
        this.mActivity = activity;
        this.filterData = filterData;
    }

    // 是否显示
    public boolean isShowing() {
        return isShowing;
    }

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public void setOnItemCategoryClickListener(OnItemCategoryClickListener
                                                       onItemCategoryClickListener) {
        this.onItemCategoryClickListener = onItemCategoryClickListener;
    }

    public void setOnItemSortClickListener(OnItemSortClickListener onItemSortClickListener) {
        this.onItemSortClickListener = onItemSortClickListener;
    }

    public void setOnItemFilterClickListener(OnItemFilterClickListener onItemFilterClickListener) {
        this.onItemFilterClickListener = onItemFilterClickListener;
    }

    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }

    public interface OnItemCategoryClickListener {
        void onItemCategoryClick(FilterTwoEntity entity);
    }

    public interface OnItemSortClickListener {
        void onItemSortClick(FilterEntity entity);
    }

    public interface OnItemFilterClickListener {
        void onItemFilterClick(FilterEntity entity);
    }

}
