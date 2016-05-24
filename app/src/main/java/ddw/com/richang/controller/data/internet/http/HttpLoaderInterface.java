package ddw.com.richang.controller.data.internet.http;

import java.io.File;
import java.util.Map;


public interface HttpLoaderInterface {
    void request(String url);//一定要调用这个发送请求


    String getString(String charset);
    String getString();
    String getHTML();

    Map<String,Object> getCookie();

    void download(File file);
    void download(String path);

}
