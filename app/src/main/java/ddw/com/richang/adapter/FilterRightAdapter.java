package ddw.com.richang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ddw.com.richang.R;
import ddw.com.richang.model.filter.FilterEntity;


/**
 * @author zzp
 */
public class FilterRightAdapter extends BaseListAdapter<FilterEntity> {

    private FilterEntity selectedEntity;

    public FilterRightAdapter(Context context) {
        super(context);
    }

    public FilterRightAdapter(Context context, List<FilterEntity> list) {
        super(context, list);
    }

    public void setSelectedEntity(FilterEntity filterEntity) {
        this.selectedEntity = filterEntity;
        for (FilterEntity entity : getData()) {
            entity.setSelected(entity.getKey().equals(selectedEntity.getKey()));
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter_one, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FilterEntity entity = getItem(position);

        holder.tvTitle.setText(entity.getKey());
        if (entity.isSelected()) {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.orange));
        } else {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.font_black_3));
        }

        return convertView;
    }

    static class ViewHolder {

        ImageView ivImage;

        TextView tvTitle;

        ViewHolder(View view) {
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
