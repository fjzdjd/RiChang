package ddw.com.richang.controller.data.internet.cache.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.print.PrintJob;

import java.util.Vector;
import ddw.com.richang.controller.data.internet.cache.Encription;
import ddw.com.richang.controller.data.internet.http.HttpLoader;

/**
 * Created by dingdewen on 16/3/8.
 */
public class BitmapManager{
    Bitmap bitmap=null;//图片本身
    String md5;//url md5
    String path;//存放路径

    //图片状态 1-不存在  2-正在下载  3-下载完成
    private static int NOTEXIST=1;
    private static int IDLE=2;
    private static int COMPLETE=3;
    int bitmapState;

    //handler
    Vector<BitmapExcutor> excutors=new Vector<BitmapExcutor>();

    //生命周期
    int refactor;


    //constructor
    BitmapManager(final String url){
        this.md5=Encription.MD5(url);
        this.path=BitmapCacher.EVN+this.md5;
        //这个类只会出现一次，所以构造函数需要在新线程加载
        bitmapState=NOTEXIST;
        this.bitmap= BitmapFactory.decodeFile(this.path);
        if(null==this.bitmap){//不存在或为空
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(bitmapState!=IDLE){
                        HttpLoader http=new HttpLoader();
                        http.request(url);
                        http.download(path);
                    }
                    while(bitmapState==IDLE){
                        try{
                            Thread.sleep(100);
                        }catch (Exception e){}
                    }
                    bitmap= BitmapFactory.decodeFile(path);
                    bitmapState=COMPLETE;
                    excute();
                }
            }).start();
            bitmapState=IDLE;
        }else{
            this.bitmapState=COMPLETE;
        }
    }

    void setHandler(BitmapExcutor bx){
        excutors.add(bx);
        if(this.bitmapState==COMPLETE) excute();
    }

    private void excute(){
        for(BitmapExcutor bx:excutors){
            bx.run();
            excutors.remove(bx);
        }
    }
}
