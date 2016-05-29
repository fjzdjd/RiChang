package ddw.com.richang.model;

import java.util.List;

/**
 * 获取任务详情
 * Created by zzp on 2016/5/29.
 */
public class RiGetActivityDetail {


    /**
     * code : 200
     * msg : 获取成功！
     * data : {"ac_id":"1058","usr_id":"80","usr_name":"日常.com","usr_pic":"http://img.myrichang.com/upload/1464014023a4d989e4abba81344fc137d020174754.png","ac_poster":"http://img.myrichang.com/upload/2016-05-24_19:5cd98f00b204e9800998ecf8427e.png","ac_poster_top":"","ac_title":"穿越世纪的目光\u2014\u2014杨绛","ac_time":"2016-06-18 17:00:00","ac_place":"深圳福田区图书馆负一楼报告厅","ac_collect_num":"0","ac_read_num":"2","ac_pay":"暂无","ac_size":"暂无","ac_speaker":"郭娟等","ac_sustain_time":"2时","ac_theme":{"theme_id":"8","theme_name":"会议"},"ac_tags":[{"tag_id":"2","tag_name":"学生"},{"tag_id":"3","tag_name":"学者"},{"tag_id":"4","tag_name":"白领"},{"tag_id":"7","tag_name":"文艺"}],"ac_desc":"郭娟，文学硕士，人在水上》、《纸上民国》。","ac_html":"第410期","ac_phone":"无","ac_collect":"0","plan":"0"}
     */

    private String code;
    private String msg;
    /**
     * ac_id : 1058
     * usr_id : 80
     * usr_name : 日常.com
     * usr_pic : http://img.myrichang.com/upload/1464014023a4d989e4abba81344fc137d020174754.png
     * ac_poster : http://img.myrichang.com/upload/2016-05-24_19:5cd98f00b204e9800998ecf8427e.png
     * ac_poster_top :
     * ac_title : 穿越世纪的目光——杨绛
     * ac_time : 2016-06-18 17:00:00
     * ac_place : 深圳福田区图书馆负一楼报告厅
     * ac_collect_num : 0
     * ac_read_num : 2
     * ac_pay : 暂无
     * ac_size : 暂无
     * ac_speaker : 郭娟等
     * ac_sustain_time : 2时
     * ac_theme : {"theme_id":"8","theme_name":"会议"}
     * ac_tags : [{"tag_id":"2","tag_name":"学生"},{"tag_id":"3","tag_name":"学者"},{"tag_id":"4","tag_name":"白领"},{"tag_id":"7","tag_name":"文艺"}]
     * ac_desc : 郭娟，文学硕士，人在水上》、《纸上民国》。
     * ac_html : 第410期
     * ac_phone : 无
     * ac_collect : 0
     * plan : 0
     */

    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
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
        private String ac_read_num;
        private String ac_pay;
        private String ac_size;
        private String ac_speaker;
        private String ac_sustain_time;
        /**
         * theme_id : 8
         * theme_name : 会议
         */

        private AcThemeBean ac_theme;
        private String ac_desc;
        private String ac_html;
        private String ac_phone;
        private String ac_collect;
        private String plan;
        /**
         * tag_id : 2
         * tag_name : 学生
         */

        private List<AcTagsBean> ac_tags;

        @Override
        public String toString() {
            return "DataBean{" +
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
                    ", ac_read_num='" + ac_read_num + '\'' +
                    ", ac_pay='" + ac_pay + '\'' +
                    ", ac_size='" + ac_size + '\'' +
                    ", ac_speaker='" + ac_speaker + '\'' +
                    ", ac_sustain_time='" + ac_sustain_time + '\'' +
                    ", ac_theme=" + ac_theme +
                    ", ac_desc='" + ac_desc + '\'' +
                    ", ac_html='" + ac_html + '\'' +
                    ", ac_phone='" + ac_phone + '\'' +
                    ", ac_collect='" + ac_collect + '\'' +
                    ", plan='" + plan + '\'' +
                    ", ac_tags=" + ac_tags +
                    '}';
        }

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

        public String getAc_read_num() {
            return ac_read_num;
        }

        public void setAc_read_num(String ac_read_num) {
            this.ac_read_num = ac_read_num;
        }

        public String getAc_pay() {
            return ac_pay;
        }

        public void setAc_pay(String ac_pay) {
            this.ac_pay = ac_pay;
        }

        public String getAc_size() {
            return ac_size;
        }

        public void setAc_size(String ac_size) {
            this.ac_size = ac_size;
        }

        public String getAc_speaker() {
            return ac_speaker;
        }

        public void setAc_speaker(String ac_speaker) {
            this.ac_speaker = ac_speaker;
        }

        public String getAc_sustain_time() {
            return ac_sustain_time;
        }

        public void setAc_sustain_time(String ac_sustain_time) {
            this.ac_sustain_time = ac_sustain_time;
        }

        public AcThemeBean getAc_theme() {
            return ac_theme;
        }

        public void setAc_theme(AcThemeBean ac_theme) {
            this.ac_theme = ac_theme;
        }

        public String getAc_desc() {
            return ac_desc;
        }

        public void setAc_desc(String ac_desc) {
            this.ac_desc = ac_desc;
        }

        public String getAc_html() {
            return ac_html;
        }

        public void setAc_html(String ac_html) {
            this.ac_html = ac_html;
        }

        public String getAc_phone() {
            return ac_phone;
        }

        public void setAc_phone(String ac_phone) {
            this.ac_phone = ac_phone;
        }

        public String getAc_collect() {
            return ac_collect;
        }

        public void setAc_collect(String ac_collect) {
            this.ac_collect = ac_collect;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public List<AcTagsBean> getAc_tags() {
            return ac_tags;
        }

        public void setAc_tags(List<AcTagsBean> ac_tags) {
            this.ac_tags = ac_tags;
        }

        public static class AcThemeBean {
            private String theme_id;
            private String theme_name;

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


    @Override
    public String toString() {
        return "RiGetActivityDetail{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
