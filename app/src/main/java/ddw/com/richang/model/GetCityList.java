package ddw.com.richang.model;

/**
 * 城市数据，少了图片信息需要另外接口获取
 * Created by zzp on 2016/5/25.
 */
public class GetCityList {


    /**
     * ct_id : 1
     * ct_name : 北京
     */

    private String ct_id;
    private String ct_name;

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
