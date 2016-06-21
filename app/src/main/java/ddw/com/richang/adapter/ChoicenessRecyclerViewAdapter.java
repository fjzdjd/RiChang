package ddw.com.richang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zzp on 2016/6/21.
 */
public class ChoicenessRecyclerViewAdapter extends RecyclerView
        .Adapter<ChoicenessRecyclerViewAdapter.ViewHolder> {

    public ChoicenessRecyclerViewAdapter() {
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
