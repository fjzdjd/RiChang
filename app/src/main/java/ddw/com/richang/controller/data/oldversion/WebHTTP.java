package ddw.com.richang.controller.data.oldversion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
		published by DDW on 22th april, 2015
*/
public class WebHTTP{

    private String url;

    private Map<String,Object> get;
    private Map<String,Object> post;
    private Map<String,Object> cookie;

    private InputStream input;

    //构造函数，传入url地址
    public WebHTTP(String url) throws Exception{
        this.url="http://"+url.replaceAll("http[s]?://","");
        this.get=null;
        this.post=null;
        this.cookie=null;
        this.input=null;
    }
    //设置get参数
    public void setGet(Map<String,Object> params){
        this.get=params;
    }
    public void setGet(String[] key,Object[] value){
        this.get=toMap(key,value);
    }
    //设置post参数
    public void setPost(Map<String,Object> params){
        this.post=params;
    }
    public void setPost(String[] key,Object[] value){
        this.post=toMap(key,value);
    }
    //设置cookie
    public void setCookie(Map<String,Object> params){
        this.cookie=params;
    }public void setCookie(String[] key,Object[] value){
        this.cookie=toMap(key,value);
    }
    //关闭！！切记要在getStr()或save()方法结束之后要调用close()方法关闭输入流
    public void close() throws IOException{
        if(null!=this.input) 		this.input.close();
    }
    //清空get、post、cookie
    public void clear(){
        this.get=null;
        this.post=null;
        this.cookie=null;
    }

    //访问网络，并以字符串方式返回
    public String getStr() throws IOException{
        this.getConnection();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len=this.input.read(buffer))>-1){
            out.write(buffer,0,len);
        }
        String res=new String(out.toByteArray());
        //获取文字编码：
        Pattern p=Pattern.compile("<meta[^>]+?charset=[^>]+?>");
        Matcher m=p.matcher(res);
        if(m.find()){
            Pattern p2=Pattern.compile("charset=\"?[A-z0-9]+\"?");
            Matcher m2=p2.matcher(m.group().replaceAll("-",""));
            if(m2.find()){
                String set=m2.group().replaceAll("\"","").split("=")[1].toUpperCase();
                res=new String(out.toByteArray(),set);
            }
        }
        out.close();
        close();
        return res;
    }

    //将网络内容保存到文件
    public boolean save(String dir){
        return save(new File(dir));
    }
    public boolean save(File file){
        try{
            if(!file.exists()) file.createNewFile();
            this.getConnection();
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while((len=this.input.read(buffer))>-1){
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            close();
        }catch(Exception e){
            file.delete();
            return false;
        }
        return false;
    }

    private void getConnection() throws IOException{
        String urlStr=this.url;
        //get:
        if(null!=this.get){
            String r=this.get.toString();
            r=r.substring(1,r.length()-1).replaceAll(", ","&");
            urlStr+=("?"+r);
        }
        URL _url=new URL(urlStr);
        HttpURLConnection connection=(HttpURLConnection) _url.openConnection();
        //cookie:
        if(null!=this.cookie){
            String r=this.cookie.toString();
            r=r.substring(1,r.length()-1).replaceAll(", ",";");
            connection.addRequestProperty("Cookie",r);
        }
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        //post:
        if(null!=this.post){
            connection.setDoOutput(true);
            connection.setDoInput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            String r=this.post.toString();
            r=r.substring(1,r.length()-1).replaceAll(", ","&");
            out.print(r);
            out.flush();
        }
        connection.connect();
        this.input=connection.getInputStream();
    }

    private Map<String,Object> toMap(String[] key,Object[] value){
        int len=key.length;
        if(len > value.length)	len=value.length;
        if(0==len)return null;
        Map<String,Object> map=new HashMap<String,Object>();
        for(int i=0;i<len;i++){
            map.put(key[i],value[i]);
        }
        return map;
    }

    public static String getStr(String url){
        try {
            WebHTTP w = new WebHTTP(url);
            String res=w.getStr();
            w.close();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
