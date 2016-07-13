package ddw.com.richang.model;

/**
 * Created by zzp on 2016/7/13.
 */
public class RiGetPopularComments {


    /**
     * comment_id : 1
     * usr_id : 1
     * usr_name : 日常君
     * usr_pic : http://img.myrichang.86969a930842bc2d6a91.png
     * comment_content : 哈哈哈
     * comment_time : 2016-06-01 12:00:00
     * comment_praise_num : 67
     * father_comment_id : -1
     */

    private String comment_id;
    private String usr_id;
    private String usr_name;
    private String usr_pic;
    private String comment_content;
    private String comment_time;
    private String comment_praise_num;
    private String father_comment_id;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
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

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_praise_num() {
        return comment_praise_num;
    }

    public void setComment_praise_num(String comment_praise_num) {
        this.comment_praise_num = comment_praise_num;
    }

    public String getFather_comment_id() {
        return father_comment_id;
    }

    public void setFather_comment_id(String father_comment_id) {
        this.father_comment_id = father_comment_id;
    }
}
