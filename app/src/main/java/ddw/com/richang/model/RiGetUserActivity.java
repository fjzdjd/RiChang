package ddw.com.richang.model;

import java.util.List;

/**
 * 获取用户参加的、收藏的、发布的、报名的活动
 * Created by zzp on 2016/6/8.
 */
public class RiGetUserActivity {


    /**
     * ac_id : 1508
     * ac_title : Wishbao · 慢慢漫漫遊 2016 音乐分享会杭州站
     * ac_poster : http://img.myrichang
     * .com/upload/2016-05-29_21:44:05_d41d8cd98f00b204e9800998ecf8427e.png
     * ac_poster_top :
     * theme_id : 8
     * theme_name : 其他
     * ac_time : 2016-06-26 20:30:00
     * ac_sustain_time : 2时
     * ac_place : 杭州 西湖区 万塘路262号万糖汇城市生活广场南楼2F
     * ac_size : 暂无
     * ac_pay : 免费
     * usr_id : 74
     * ac_type :
     * ac_review : 1
     * ac_status : 1
     * ac_desc : 无
     * ac_tags : [{"tag_id":"2","tag_name":"学生"},{"tag_id":"12","tag_name":"生活"},{"tag_id":"4",
     * "tag_name":"白领"}]
     * usr_name : 日常
     * usr_pic : http://img.myrichang.com/img/src/logo.png
     * ac_read_num : 14
     * ac_praise_num :
     * ac_collect_num :
     */

    private String ac_id;
    private String ac_title;
    private String ac_poster;
    private String ac_poster_top;
    private String theme_id;
    private String theme_name;
    private String ac_time;
    private String ac_sustain_time;
    private String ac_place;
    private String ac_size;
    private String ac_pay;
    private String usr_id;
    private String ac_type;
    private String ac_review;
    private String ac_status;
    private String ac_desc;
    private String usr_name;
    private String usr_pic;
    private String ac_read_num;
    private String ac_praise_num;
    private String ac_collect_num;
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

    public String getAc_title() {
        return ac_title;
    }

    public void setAc_title(String ac_title) {
        this.ac_title = ac_title;
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

    public String getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(String theme_id) {
        this.theme_id = theme_id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public String getAc_time() {
        return ac_time;
    }

    public void setAc_time(String ac_time) {
        this.ac_time = ac_time;
    }

    public String getAc_sustain_time() {
        return ac_sustain_time;
    }

    public void setAc_sustain_time(String ac_sustain_time) {
        this.ac_sustain_time = ac_sustain_time;
    }

    public String getAc_place() {
        return ac_place;
    }

    public void setAc_place(String ac_place) {
        this.ac_place = ac_place;
    }

    public String getAc_size() {
        return ac_size;
    }

    public void setAc_size(String ac_size) {
        this.ac_size = ac_size;
    }

    public String getAc_pay() {
        return ac_pay;
    }

    public void setAc_pay(String ac_pay) {
        this.ac_pay = ac_pay;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public String getAc_type() {
        return ac_type;
    }

    public void setAc_type(String ac_type) {
        this.ac_type = ac_type;
    }

    public String getAc_review() {
        return ac_review;
    }

    public void setAc_review(String ac_review) {
        this.ac_review = ac_review;
    }

    public String getAc_status() {
        return ac_status;
    }

    public void setAc_status(String ac_status) {
        this.ac_status = ac_status;
    }

    public String getAc_desc() {
        return ac_desc;
    }

    public void setAc_desc(String ac_desc) {
        this.ac_desc = ac_desc;
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

    public String getAc_read_num() {
        return ac_read_num;
    }

    public void setAc_read_num(String ac_read_num) {
        this.ac_read_num = ac_read_num;
    }

    public String getAc_praise_num() {
        return ac_praise_num;
    }

    public void setAc_praise_num(String ac_praise_num) {
        this.ac_praise_num = ac_praise_num;
    }

    public String getAc_collect_num() {
        return ac_collect_num;
    }

    public void setAc_collect_num(String ac_collect_num) {
        this.ac_collect_num = ac_collect_num;
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
