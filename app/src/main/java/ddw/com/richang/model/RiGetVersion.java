package ddw.com.richang.model;

/**
 * 获取版本信息
 * Created by zzp on 2016/6/16.
 */
public class RiGetVersion {


    /**
     * code : 200
     * msg : 操作成功！
     * data : {"v_id":"2","app_version":"1.01","app_url":"http://oss.myrichang.com/appinstall/1
     * .01.apk","app_msg":"过期活动变灰","app_type":"1"}
     */

    private int code;
    private String msg;
    /**
     * v_id : 2
     * app_version : 1.01
     * app_url : http://oss.myrichang.com/appinstall/1.01.apk
     * app_msg : 过期活动变灰
     * app_type : 1
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
        private String v_id;
        private String app_version;
        private String app_url;
        private String app_msg;
        private String app_type;

        public String getV_id() {
            return v_id;
        }

        public void setV_id(String v_id) {
            this.v_id = v_id;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public String getApp_url() {
            return app_url;
        }

        public void setApp_url(String app_url) {
            this.app_url = app_url;
        }

        public String getApp_msg() {
            return app_msg;
        }

        public void setApp_msg(String app_msg) {
            this.app_msg = app_msg;
        }

        public String getApp_type() {
            return app_type;
        }

        public void setApp_type(String app_type) {
            this.app_type = app_type;
        }
    }
}
