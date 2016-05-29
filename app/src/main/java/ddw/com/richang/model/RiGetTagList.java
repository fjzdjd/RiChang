package ddw.com.richang.model;

/**
 * 获取标签
 * Created by zzp on 2016/5/26.
 */
public class RiGetTagList {


    /**
     * tag_id : 1
     * tag_name : 互联网
     * sort_id : 1
     */

    private String tag_id;
    private String tag_name;


    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RiGetTagList) {
            RiGetTagList st = (RiGetTagList) obj;
            return (tag_name.equals(st.tag_name));
        } else {
            return super.equals(obj);
        }
    }
}
