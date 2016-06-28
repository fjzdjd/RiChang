package ddw.com.richang.model;

/**
 * 获取发布者推荐
 * Created by zzp on 2016/6/28.
 */
public class RiGetPublisherRecommend {


    /**
     * publisher_id : 4
     * publisher_name : 轩小羽
     * publisher_sign : 行胜于言└(●°u°●)​ 」
     * publisher_pic : http://img.myrichang.ee7a.png
     */

    private String publisher_id;
    private String publisher_name;
    private String publisher_sign;
    private String publisher_pic;

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public String getPublisher_sign() {
        return publisher_sign;
    }

    public void setPublisher_sign(String publisher_sign) {
        this.publisher_sign = publisher_sign;
    }

    public String getPublisher_pic() {
        return publisher_pic;
    }

    public void setPublisher_pic(String publisher_pic) {
        this.publisher_pic = publisher_pic;
    }


    @Override
    public String toString() {
        return "RiGetPublisherRecommend{" +
                "publisher_id='" + publisher_id + '\'' +
                ", publisher_name='" + publisher_name + '\'' +
                ", publisher_sign='" + publisher_sign + '\'' +
                ", publisher_pic='" + publisher_pic + '\'' +
                '}';
    }
}
