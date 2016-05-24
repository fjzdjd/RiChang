package ddw.com.richang.controller.view.list;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ddw.com.richang.model.ACT;
import ddw.com.richang.R;
import ddw.com.richang.controller.data.imgloader.bitmap.CircleView;
import ddw.com.richang.controller.data.imgloader.bitmap.DarkView;
import ddw.com.richang.controller.data.imgloader.bitmap.NormalView;
import ddw.com.richang.controller.data.imgloader.core.ImageLoader;
import ddw.com.richang.service.AlarmPlan;

/**
 * Created by dingdewen on 16/1/17.
 */
public class ColumAdapter extends BaseAdapter{
    private Activity activity;
    private ArrayList<Bitmap> pool;
    public ArrayList<ACT> listItem;
    public ColumAdapter(Activity activity,ArrayList<ACT> items){
        this.listItem=items;
        this.activity=activity;
    }
    public ColumAdapter(Activity activity,ArrayList<ACT> items,ArrayList<Bitmap> pool){
        this.listItem=items;
        this.activity=activity;
        this.pool=pool;
    }

    public int getCount(){
        return this.listItem.size();
    }
    public Object getItem(int pos){
        return this.listItem.get(pos);
    }
    public long getItemId(int pos){
        return pos;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemView listItemView;
        if(null==convertView){
            convertView= LayoutInflater.from(this.activity).inflate(R.layout.listitem,null);
            listItemView = new ListItemView();
            listItemView.imageView=(ImageView)convertView.findViewById(R.id.list_item_img);
            listItemView.title=(TextView)convertView.findViewById(R.id.list_item_title);
            listItemView.time=(TextView)convertView.findViewById(R.id.list_item_time);
            listItemView.place=(TextView)convertView.findViewById(R.id.list_item_place);
            listItemView.tags=(TextView)convertView.findViewById(R.id.list_item_tags);
            listItemView.pubpic=(ImageView)convertView.findViewById(R.id.publisher_pic);
            listItemView.pubname=(TextView)convertView.findViewById(R.id.publisher_name);
            convertView.setTag(listItemView);
        }else{
            listItemView=(ListItemView)convertView.getTag();
        }
        listItemView.title.setText(this.listItem.get(position).getTitle());
        listItemView.place.setText(this.listItem.get(position).getPlace());
        listItemView.time.setText(this.listItem.get(position).getTime().substring(0, 16));
        listItemView.tags.setText(this.listItem.get(position).getTagsString());
        ImageLoader.getInstance().displayImage(listItem.get(position).pic + "@!profile", listItemView.pubpic, CircleView.BITMAPDISPLAYER);
        listItemView.pubname.setText(this.listItem.get(position).name);
        //判断时间是否已过期：
        final int color=Color.parseColor("#aCaCaC");
        if(this.listItem.get(position).getTime().compareTo(AlarmPlan.now.getDatetime())<0){
            listItemView.title.setTextColor(color);
            listItemView.place.setTextColor(color);
            listItemView.time.setTextColor(color);
            listItemView.tags.setTextColor(color);
            ((TextView)convertView.findViewById(R.id.didian)).setTextColor(color);
            ((TextView)convertView.findViewById(R.id.shijian)).setTextColor(color);
            ImageLoader.getInstance().displayImage(listItem.get(position).getPicture()+"@!display",listItemView.imageView, DarkView.BITMAPDISPLAYER);
        }else ImageLoader.getInstance().displayImage(listItem.get(position).getPicture() + "@!display", listItemView.imageView, NormalView.BITMAPDISPLAYER);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
