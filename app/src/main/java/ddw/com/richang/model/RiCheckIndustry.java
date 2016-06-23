package ddw.com.richang.model;

import java.util.List;

/**
 * 获取专栏list信息
 * Created by zzp on 2016/6/22.
 */
public class RiCheckIndustry {


    /**
     * ac_id : 2086
     * usr_id : 79
     * usr_name : 日常
     * usr_pic : http://img.myrichang.com/img/src/logo.png
     * ac_poster : http://img.myrichang.com/img/gallery/industry/11/11%20(37).png
     * ac_poster_top :
     * ac_title : 美国养老金体系对中国的启示
     * ac_time : 2016-06-15 19:00:00
     * ac_place : 清华经管学院 伟伦报告厅
     * ac_collect_num :
     * ac_praise_num :
     * ac_read_num : 1
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

    @Override
    public String toString() {
        return "RiCheckIndustry{" +
                "ac_id='" + ac_id + '\'' +
                ", usr_id='" + usr_id + '\'' +
                ", usr_name='" + usr_name + '\'' +
                ", usr_pic='" + usr_pic + '\'' +
                ", ac_poster='" + ac_poster + '\'' +
                ", ac_poster_top='" + ac_poster_top + '\'' +
                ", ac_title='" + ac_title + '\'' +
                ", ac_time='" + ac_time + '\'' +
                ", ac_place='" + ac_place + '\'' +
                ", ac_collect_num='" + ac_collect_num + '\'' +
                ", ac_praise_num='" + ac_praise_num + '\'' +
                ", ac_read_num='" + ac_read_num + '\'' +
                ", ac_tags=" + ac_tags +
                '}';
    }
}
