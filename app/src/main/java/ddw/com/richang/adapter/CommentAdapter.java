package ddw.com.richang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.model.RiGetPopularComments;

/**
 * 用户评论
 * Created by zzp on 2016/7/13.
 */
public class CommentAdapter extends BaseAdapter {

    private Context mContext;

    private List<RiGetPopularComments> mListData;

    private LayoutInflater mInflater;

    private ImageOptions options;

    private HashMap<Integer, View> viewChache = new HashMap<>();

    public CommentAdapter(Context mContext, List<RiGetPopularComments> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
        this.mInflater = LayoutInflater.from(mContext);
        options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.imggrey)
                .setLoadingDrawableId(R.mipmap.imggrey).setUseMemCache(true).setCircular(true)
                .build();
    }

    public void setData(List<RiGetPopularComments> mListData) {
        this.mListData = mListData;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        if (viewChache.get(position) == null) {
            convertView = mInflater.inflate(R.layout
                    .item_detail_evaluate_layout, null);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
            viewChache.put(position, convertView);
        } else {
            convertView = viewChache.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RiGetPopularComments bean = mListData.get(position);

        viewHolder.mLikeNum.setText(bean.getComment_praise_num());
        viewHolder.mPublishTime.setText(bean.getComment_time());
        viewHolder.mTitle.setText(bean.getComment_content());
        viewHolder.mUserName.setText(bean.getUsr_name());

        x.image().bind(viewHolder.mUserPic, bean.getUsr_pic(), options);

        return convertView;
    }

    class ViewHolder {

        @ViewInject(R.id.user_img_comment)
        ImageView mUserPic;

        @ViewInject(R.id.user_txt_name)
        TextView mUserName;

        @ViewInject(R.id.user_txt_time)
        TextView mPublishTime;

        @ViewInject(R.id.user_txt_title)
        TextView mTitle;

        @ViewInject(R.id.user_txt_likeNum)
        TextView mLikeNum;


    }
}
