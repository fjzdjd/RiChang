package ddw.com.richang.model;

/**
 * user information
 * Created by zzp on 2016/5/12.
 */
public class RiUserLogin  {


    /**
     * code : 200
     * msg : 登录成功！
     * data : {"usr_id":"4","usr_name":"日常君","usr_sign":"行胜于言└(●°u°●)\u200b 」",
     * "usr_pic":"http://img.myrichang.com/upload/14603908043809389a8815edb346a8363dab87ee7a
     * .png","usr_sex":"1","usr_phone":"15871723883","usr_mail":"kunyyong@qq.com","ct_id":"3",
     * "ct_name":"武汉"}
     */

    private int code;
    private String msg;
    /**
     * usr_id : 4
     * usr_name : 日常君
     * usr_sign : 行胜于言└(●°u°●)​ 」
     * usr_pic : http://img.myrichang.com/upload/14603908043809389a8815edb346a8363dab87ee7a.png
     * usr_sex : 1
     * usr_phone : 15871723883
     * usr_mail : kunyyong@qq.com
     * ct_id : 3
     * ct_name : 武汉
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
        private String usr_id;
        private String usr_name;
        private String usr_sign;
        private String usr_pic;
        private String usr_sex;
        private String usr_phone;
        private String usr_mail;
        private String ct_id;
        private String ct_name;

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

        public String getUsr_sign() {
            return usr_sign;
        }

        public void setUsr_sign(String usr_sign) {
            this.usr_sign = usr_sign;
        }

        public String getUsr_pic() {
            return usr_pic;
        }

        public void setUsr_pic(String usr_pic) {
            this.usr_pic = usr_pic;
        }

        public String getUsr_sex() {
            return usr_sex;
        }

        public void setUsr_sex(String usr_sex) {
            this.usr_sex = usr_sex;
        }

        public String getUsr_phone() {
            return usr_phone;
        }

        public void setUsr_phone(String usr_phone) {
            this.usr_phone = usr_phone;
        }

        public String getUsr_mail() {
            return usr_mail;
        }

        public void setUsr_mail(String usr_mail) {
            this.usr_mail = usr_mail;
        }

        public String getCt_id() {
            return ct_id;
        }

        public void setCt_id(String ct_id) {
            this.ct_id = ct_id;
        }

        public String getCt_name() {
            return ct_name;
        }

        public void setCt_name(String ct_name) {
            this.ct_name = ct_name;
        }
    }
}
