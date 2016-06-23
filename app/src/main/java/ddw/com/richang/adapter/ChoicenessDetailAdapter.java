package ddw.com.richang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.model.RiGetAlbumnAcs;
import ddw.com.richang.ui.everyday.ContentDetailActivity;

/**
 * 精选详情 adapter
 * Created by zzp on 2016/6/23.
 */
public class ChoicenessDetailAdapter extends RecyclerView.Adapter<ChoicenessDetailAdapter
        .ViewHolder> {

    private Context mContext;

    private List<RiGetAlbumnAcs> mList;

    public ChoicenessDetailAdapter(Context context, List<RiGetAlbumnAcs> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_choiceness_detail_layout,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        x.image().bind(holder.mImageView,mList.get(position).getAc_album_img());
        holder.mTitle.setText(mList.get(position).getAc_title());
        holder.mContent.setText(mList.get(position).getAc_desc());
        holder.mReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ContentDetailActivity.class);
                intent.putExtra("ac_id",mList.get(position).getAc_id());
                mContext.startActivity(intent);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageView;
        private final TextView mTitle;
        private final TextView mContent;
        private final TextView mReadMore;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id
                    .choiceness_detail_img_poster);
            mTitle = (TextView) itemView.findViewById(R.id.choiceness_detail_txt_title);
            mContent = (TextView) itemView.findViewById(R.id
                    .choiceness_detail_txt_content);
            mReadMore = (TextView) itemView.findViewById(R.id
                    .choiceness_detail_txt_readMore);

        }
    }

}
