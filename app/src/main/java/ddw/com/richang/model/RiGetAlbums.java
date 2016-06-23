package ddw.com.richang.model;

/**
 * 精选列表
 * Created by zzp on 2016/6/21.
 */
public class RiGetAlbums {


    /**
     * album_id : 1
     * album_name : 六月份你不可错过的读书交友会
     * album_img : http://img.myrichang.com/img/banner/2016-06/06/1-6.jpg
     * album_desc : 六月，你读书了吗
     * album_time : 2016-06-06 12:00:00
     * read_num : 10
     */

    private String album_id;
    private String album_name;
    private String album_img;
    private String album_desc;
    private String album_time;
    private String read_num;

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_img() {
        return album_img;
    }

    public void setAlbum_img(String album_img) {
        this.album_img = album_img;
    }

    public String getAlbum_desc() {
        return album_desc;
    }

    public void setAlbum_desc(String album_desc) {
        this.album_desc = album_desc;
    }

    public String getAlbum_time() {
        return album_time;
    }

    public void setAlbum_time(String album_time) {
        this.album_time = album_time;
    }

    public String getRead_num() {
        return read_num;
    }

    public void setRead_num(String read_num) {
        this.read_num = read_num;
    }

    @Override
    public String toString() {
        return "RiGetAlbums{" +
                "album_id='" + album_id + '\'' +
                ", album_name='" + album_name + '\'' +
                ", album_img='" + album_img + '\'' +
                ", album_desc='" + album_desc + '\'' +
                ", album_time='" + album_time + '\'' +
                ", read_num='" + read_num + '\'' +
                '}';
    }
}
