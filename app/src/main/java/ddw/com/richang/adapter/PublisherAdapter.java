package ddw.com.richang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.model.RiGetPublisherRecommend;

/**
 * 发布者推荐列表
 * Created by zzp on 2016/6/28.
 */
public class PublisherAdapter extends BaseAdapter {

    private Context mContext;

    private HashMap<Integer, View> viewChache = new HashMap<>();

    private List<RiGetPublisherRecommend> mList;

    public PublisherAdapter(Context context, List<RiGetPublisherRecommend>
            riGetPublisherRecommends) {
        this.mContext = context;
        this.mList = riGetPublisherRecommends;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setListData(List<RiGetPublisherRecommend> list) {
        this.mList = list;
        notifyDataSetChanged();


    }

    @Override
    public int getCount() {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (viewChache.get(position) == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_publisher_layout,
                    null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            viewChache.put(position, convertView);
        } else {
            convertView = viewChache.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTitle.setText(mList.get(position).getPublisher_name());
        viewHolder.mContent.setText(mList.get(position).getPublisher_sign());
        x.image().bind(viewHolder.mImageView, mList.get(position).getPublisher_pic());

        viewHolder.mFocusNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //处理关注事件
            }
        });

        return convertView;
    }


    public static class ViewHolder {


        private final ImageView mImageView;
        private final TextView mFocusNum;
        private final TextView mTitle;
        private final TextView mContent;

        public ViewHolder(View itemView) {

            mImageView = (ImageView) itemView.findViewById(R.id.publisher_img_header);
            mTitle = (TextView) itemView.findViewById(R.id.publisher_txt_title);
            mFocusNum = (TextView) itemView.findViewById(R.id.publisher_txt_focus_num);
            mContent = (TextView) itemView.findViewById(R.id.publisher_txt_content);

        }
    }

}
