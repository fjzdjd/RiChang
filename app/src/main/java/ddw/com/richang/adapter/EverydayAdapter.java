package ddw.com.richang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.model.RiActivityRecommend;
import ddw.com.richang.util.StringUtils;

/**
 * Created by zzp on 2016/6/24.
 */
public class EverydayAdapter extends BaseAdapter {

    private List<RiActivityRecommend> mList;

    private Context mContext;

    private HashMap<Integer, View> viewChache = new HashMap<>();

    public EverydayAdapter(Context context, List<RiActivityRecommend> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mList.size() <= 0) {
            return 1;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setListData(List<RiActivityRecommend> list) {
        this.mList = list;
        notifyDataSetChanged();


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //暂无数据的布局
        if (mList.size() <= 0) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout
                    .item_no_data_layout, parent, false);


        } else {

            ViewHolder viewHolder;

            if (viewChache.get(position) == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout
                        .item_everyday_list_layout, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
                viewChache.put(position, convertView);
            } else {
                convertView = viewChache.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }

            x.image().bind(viewHolder.acPoster, mList.get(position).getAc_poster());
            viewHolder.acTitle.setText(mList.get(position).getAc_title());
            viewHolder.acTime.setText(mList.get(position).getAc_time());
            viewHolder.acPlace.setText(mList.get(position).getAc_place());
            viewHolder.acReadNum.setText(" " + mList.get(position).getAc_read_num());
            viewHolder.acCostTags.setText(" " + "免费活动");

            try {
                if (StringUtils.compareTime(mList.get(position).getAc_time())){
                   viewHolder.acBackground.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }else {
                    viewHolder.acBackground.setBackgroundColor(Color.WHITE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return convertView;
    }




    /**
     * 适配器布局
     */
    static class ViewHolder {

        private final ImageView acPoster;
        private final TextView acTitle;
        private final TextView acTime;
        private final TextView acPlace;

        /**
         * 字段中暂无此值
         */
        private final TextView acCostTags;
        private final TextView acReadNum;
        private final LinearLayout acBackground;

        ViewHolder(View view) {

            acPoster = (ImageView) view.findViewById(R.id.everyday_adapter_img_item);
            acTitle = (TextView) view.findViewById(R.id.everyday_adapter_txt_title);
            acTime = (TextView) view.findViewById(R.id.everyday_adapter_txt_time);
            acPlace = (TextView) view.findViewById(R.id.everyday_adapter_txt_place);
            acCostTags = (TextView) view.findViewById(R.id.everyday_adapter_txt_tag);
            acReadNum = (TextView) view.findViewById(R.id.everyday_adapter_txt_num);
            acBackground = (LinearLayout) view.findViewById(R.id.everyday_adapter_bg_item);


        }

    }

}
