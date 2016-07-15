package ddw.com.richang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.model.RiGetPopularComments;

/**
 * 所有评论
 * Created by zzp on 2016/7/15.
 */
public class AllCommentAdapter extends RecyclerView.Adapter<AllCommentAdapter.ViewHolder> {

    private Context mContext;

    private List<RiGetPopularComments> mList;

    private ImageOptions options;

    public AllCommentAdapter(Context mContext, List<RiGetPopularComments> mList) {
        this.mContext = mContext;
        this.mList = mList;
        options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.imggrey)
                .setLoadingDrawableId(R.mipmap.imggrey).setUseMemCache(true).setCircular(true)
                .build();

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_evaluate_layout,
                parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RiGetPopularComments bean = mList.get(position);
        x.image().bind(holder.mImageHeader, bean.getUsr_pic(), options);
        holder.mName.setText(bean.getUsr_name());
        holder.mTime.setText(bean.getComment_time());
        holder.mContent.setText(bean.getComment_content());
        holder.mLikeNum.setText(bean.getComment_praise_num());


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageHeader;
        private final TextView mName;
        private final TextView mTime;
        private final TextView mContent;
        private final TextView mLikeNum;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageHeader = (ImageView) itemView.findViewById(R.id.evaluate_img_comment);
            mName = (TextView) itemView.findViewById(R.id.evaluate_txt_name);
            mTime = (TextView) itemView.findViewById(R.id.evaluate_txt_time);
            mContent = (TextView) itemView.findViewById(R.id.evaluate_txt_title);
            mLikeNum = (TextView) itemView.findViewById(R.id.evaluate_txt_likeNum);

        }
    }
}
