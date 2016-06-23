package ddw.com.richang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.model.RiCheckIndustry;
import ddw.com.richang.ui.everyday.ContentDetailActivity;

/**
 * 专栏 (except精选)
 * Created by zzp on 2016/6/21.
 */
public class CheckIndustryAdapter extends RecyclerView
        .Adapter<CheckIndustryAdapter.ViewHolder> {

    private Context mContext;

    private List<RiCheckIndustry> mListData;

    public CheckIndustryAdapter(Context context, List<RiCheckIndustry> listData) {

        this.mContext = context;
        this.mListData = listData;
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comprehensive_layout,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        x.image().bind(holder.mImageView, mListData.get(position).getAc_poster());

        holder.mTitle.setText(mListData.get(position).getAc_title());

        holder.mTime.setText(mListData.get(position).getAc_time());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ContentDetailActivity.class);
                intent.putExtra("ac_id", mListData.get(position).getAc_id());
                mContext.startActivity(intent);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView mCardView;
        private final ImageView mImageView;
        private final TextView mTitle;
        private final TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.comprehensive_cardView);
            mTime = (TextView) itemView.findViewById(R.id.comprehensive_txt_time);
            mImageView = (ImageView) itemView.findViewById(R.id.comprehensive_img_poster);
            mTitle = (TextView) itemView.findViewById(R.id.comprehensive_txt_title);

        }
    }


}
