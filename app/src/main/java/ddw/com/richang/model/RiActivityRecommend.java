package ddw.com.richang.model;

import java.io.Serializable;
import java.util.List;

/**
 * 活动推荐(为你推荐)
 * Created by zzp on 2016/5/16.
 */
public class RiActivityRecommend implements Serializable {


    /**
     * ac_id : 89
     * usr_id : 6
     * usr_name : 逸章
     * usr_pic : http://img.myrichang.com/upload/1460032052d43adc0033852893518300bdea56e912.png
     * ac_poster : http://img.myrichang
     * .com/upload/2016-03-07_20:31:20_dba8b918112726028c62baa3f629f4b4.png
     * ac_poster_top :
     * ac_title : 求是学术讲座第二十讲通知：探究都市变迁：方法和结果
     * ac_time : 2016-01-28 14:20:00
     * ac_place : 中国人民大学求是楼216教室
     * ac_collect_num : 0
     * ac_praise_num :
     * ac_read_num : 8
     * ac_tags : [{"tag_id":"2","tag_name":"学生"},{"tag_id":"3","tag_name":"学者"}]
     */
    private String ac_id;
    private String usr_id;
    private String usr_name;
    private String usr_pic;
    private String ac_poster;
    private String ac_poster_top;
    private String ac_title;
    private String ac_time;
    private String ac_place;
    private String ac_collect_num;
    private String ac_praise_num;
    private String ac_read_num;

    /**
     * tag_id : 2
     * tag_name : 学生
     */
    private List<AcTagsBean> ac_tags;

    public String getAc_id() {
        return ac_id;
    }

    public void setAc_id(String ac_id) {
        this.ac_id = ac_id;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getUsr_pic() {
        return usr_pic;
    }

    public void setUsr_pic(String usr_pic) {
        this.usr_pic = usr_pic;
    }

    public String getAc_poster() {
        return ac_poster;
    }

    public void setAc_poster(String ac_poster) {
        this.ac_poster = ac_poster;
    }

    public String getAc_poster_top() {
        return ac_poster_top;
    }

    public void setAc_poster_top(String ac_poster_top) {
        this.ac_poster_top = ac_poster_top;
    }

    public String getAc_title() {
        return ac_title;
    }

    public void setAc_title(String ac_title) {
        this.ac_title = ac_title;
    }

    public String getAc_time() {
        return ac_time;
    }

    public void setAc_time(String ac_time) {
        this.ac_time = ac_time;
    }

    public String getAc_place() {
        return ac_place;
    }

    public void setAc_place(String ac_place) {
        this.ac_place = ac_place;
    }

    public String getAc_collect_num() {
        return ac_collect_num;
    }

    public void setAc_collect_num(String ac_collect_num) {
        this.ac_collect_num = ac_collect_num;
    }

    public String getAc_praise_num() {
        return ac_praise_num;
    }

    public void setAc_praise_num(String ac_praise_num) {
        this.ac_praise_num = ac_praise_num;
    }

    public String getAc_read_num() {
        return ac_read_num;
    }

    public void setAc_read_num(String ac_read_num) {
        this.ac_read_num = ac_read_num;
    }

    public List<AcTagsBean> getAc_tags() {
        return ac_tags;
    }

    public void setAc_tags(List<AcTagsBean> ac_tags) {
        this.ac_tags = ac_tags;
    }

    public static class AcTagsBean {
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
    }



}
