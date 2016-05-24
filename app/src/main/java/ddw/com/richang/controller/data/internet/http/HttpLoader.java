package ddw.com.richang.controller.data.internet.http;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpLoader implements HttpLoaderInterface{

    private Map<String,Object> get;
    private Map<String,Object> post;
    private Map<String,Object> cookie;
    private List<File> file;

    private  HttpURLConnection connection;

    //构造函数，传入url地址
    public HttpLoader(){
        this.get=null;
        this.post=null;
        this.cookie=null;
        this.file=null;
        this.connection=null;
    }
    //设置get参数
    public void setGet(Map<String,Object> params){
        this.get=params;
    }
    public void setGet(String key,Object value){
        if(this.get==null) this.get=new HashMap<String,Object>();
        this.get.put(key,value);
    }
    //设置post参数
    public void setPost(Map<String,Object> params){
        this.post=params;
    }
    public void setPOST(String key,Object value){
        if(this.post==null) this.post=new HashMap<String,Object>();
        this.post.put(key,value);
    }
    //设置cookie
    public void setCookie(Map<String,Object> params){
        this.cookie=params;
    }
    public void setCookie(String key,Object value){
        if(this.cookie==null) this.cookie=new HashMap<String,Object>();
        this.cookie.put(key,value);
    }
    //设置文件上传
    public void setFile(File file){
        if(file.exists()){
            if(null==this.file) this.file=new ArrayList<File>();
            this.file.add(file);
        }
    }
    public void setFile(List<File> filelist){
        this.file=filelist;
    }

    //清空get、post、cookie
    public static int GET=1;
    public static int POST=1<<1;
    public static int COOKIE=1<<2;
    public static int FILE=4<<2;
    public void clear(int mode){
        if( (mode & GET) >0)this.get=null;
        if( (mode & POST) >0)this.post=null;
        if( (mode & COOKIE) >0)this.cookie=null;
        if( (mode & FILE) >0)this.file=null;
    }


    //获取文字
    private byte[] strbytearray=null;
    private void getStringByteArray(){
        if(null!=strbytearray) return;
        try {
            InputStream input = this.connection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                out.write(buffer, 0, len);
            }
            this.strbytearray=out.toByteArray();
            out.close();
            input.close();
        }catch (Exception e){
            Log.e("internet",e.getMessage());
        }
    }
    public String getString(String charset){
        this.getStringByteArray();
        if(this.strbytearray==null) return null;
        if(charset==null) {
            //获取文字编码：
            String str=new String(this.strbytearray);
            Pattern p = Pattern.compile("<meta[^>]+?charset=[^>]+?>");
            Matcher m = p.matcher(str);
            if (m.find()) {
                Pattern p2 = Pattern.compile("charset=\"?[A-z0-9]+\"?");
                Matcher m2 = p2.matcher(m.group().replaceAll("-", ""));
                if (m2.find()) {
                    charset = m2.group().replaceAll("\"", "").split("=")[1].toUpperCase();
                }
            }
        }
        return charset==null? new String(this.strbytearray):new String(this.strbytearray, Charset.forName(charset));
    }
    public String getString(){
        return this.getString("UTF-8");
    }
    public String getHTML(){
        return this.getString(null);
    }



    //获取文件
    public static String ENV=null;
    public String getENV(){
        if(null==ENV) ENV=Environment.getExternalStorageDirectory().getAbsolutePath();
        return ENV;
    }
    public void download(String envPath){
        this.download(new File(this.getENV()+envPath));
    }
    public void download(File file){
        File parent=file.getParentFile();
        if(!parent.isDirectory())  parent.mkdirs();
        try{
            if(!file.exists()) file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            InputStream in=this.connection.getInputStream();
            while((len=in.read(buffer))>-1){
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        }catch(Exception e){
            Log.e("internet",e.getMessage());
            file.delete();
        }
    }


    public Map<String,Object> getCookie(){
        return this.cookie;
    }

    public void request(String url){
        try {
            String urlStr = url;
            //get:
            if (null != this.get) {
                String r = this.get.toString();
                r = r.substring(1, r.length() - 1).replaceAll(", ", "&");
                urlStr += ("?" + r);
            }
            URL _url = new URL(urlStr);
            this.connection = (HttpURLConnection) _url.openConnection();
            //cookie:
            if (null != this.cookie) {
                String r = this.cookie.toString();
                r = r.substring(1, r.length() - 1).replaceAll(", ", ";");
                this.connection.addRequestProperty("Cookie", r);
            }
            // 设置通用的请求属性
            this.connection.setRequestProperty("accept", "*/*");
            this.connection.setRequestProperty("connection", "Keep-Alive");
            this.connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //post:
            if (null != this.post) {
                this.connection.setDoOutput(true);
                this.connection.setDoInput(true);
                PrintWriter out = new PrintWriter(this.connection.getOutputStream());
                String r = this.post.toString();
                r = r.substring(1, r.length() - 1).replaceAll(", ", "&");
                out.print(r);
                out.flush();
            }
            this.connection.connect();
            List<String> cookieList = this.connection.getHeaderFields().get("Set-Cookie");
            if (null != cookieList) for (String co : cookieList) {
                String r = co.split(";")[0];
                int i = r.indexOf("=");
                this.cookie.put(r.substring(0, i), r.substring(i + 1));
            }
            this.strbytearray=null;
        }catch (Exception e){
            Log.e("internet",e.getMessage());
        }finally {
            this.clear(GET|POST);
        }
    }

}