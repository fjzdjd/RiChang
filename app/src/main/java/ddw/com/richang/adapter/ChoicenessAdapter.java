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
import ddw.com.richang.model.RiGetAlbums;
import ddw.com.richang.ui.column.ChoicenessDetailActivity;

/**
 * Created by zzp on 2016/6/21.
 */
public class ChoicenessAdapter extends RecyclerView
        .Adapter<ChoicenessAdapter.ViewHolder> {

    private Context mContext;

    private List<RiGetAlbums> mListData;

    public ChoicenessAdapter(Context context, List<RiGetAlbums> listData) {

        this.mContext = context;
        this.mListData = listData;
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_choiceness_layout,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        x.image().bind(holder.mImageView, mListData.get(position).getAlbum_img());

        holder.mTitle.setText(mListData.get(position).getAlbum_name());

        //跳转详情界面
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChoicenessDetailActivity.class);
                intent.putExtra("album_id", mListData.get(position).getAlbum_id());
                intent.putExtra("album_name", mListData.get(position).getAlbum_name());
                mContext.startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView mCardView;
        private final ImageView mImageView;
        private final TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.choiceness_cardView);
            mImageView = (ImageView) itemView.findViewById(R.id.choiceness_img_poster);
            mTitle = (TextView) itemView.findViewById(R.id.choiceness_txt_title);

        }
    }


}
