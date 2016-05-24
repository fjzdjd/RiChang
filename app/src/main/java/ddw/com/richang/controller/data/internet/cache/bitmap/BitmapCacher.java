package ddw.com.richang.controller.data.internet.cache.bitmap;

import android.widget.ImageView;
import java.util.Hashtable;
import ddw.com.richang.controller.data.internet.cache.Cacher;
import ddw.com.richang.controller.data.internet.cache.Encription;

/**
 * Created by dingdewen on 16/3/7.
 */
public class BitmapCacher extends Cacher {
    //singleton
    static String EVN;
    public static void setEVN(String path){
        EVN=path;
    }
    private BitmapCacher(){}
    public static BitmapCacher instance=null;
    public static BitmapCacher getInstance(){
        if(instance==null) instance=new BitmapCacher();
        return instance;
    }

    //
    private Hashtable<String,BitmapManager> map=new Hashtable<String,BitmapManager>();
    private BitmapManager getBitmapManager(String url){
        BitmapManager manager;
        String md5=Encription.MD5(url);
        if(map.containsKey(md5)){
            manager=map.get(md5);
        }else{
            manager=new BitmapManager(url);
            map.put(md5,manager);
        }
        return manager;
    }
    public void DrawBitmap(final String url,final ImageView img){//直接绘制到imageview上
        BitmapExcutor be=new BitmapExcutor() {
            @Override
            public void run() {
                img.post(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(bitmapManager.bitmap);
                    }
                });
            }
        };
        this.ExcuteBitmap(url,be);
    }
    public void ExcuteBitmap(final String url,BitmapExcutor be){
        BitmapManager manager=getBitmapManager(url);
        be.setBitmapManager(manager);
        manager.setHandler(be);
    }
    //生命管理
    public void addBitmap(final String url){
        map.get(Encription.MD5(url)).refactor++;
    }
    public void recycleBitmap(final String url){
        final BitmapManager bm=map.get(Encription.MD5(url));
        if( (--bm.refactor) == 0){
            //回收bitmap
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(10*1000);
                    }catch (Exception e){}finally {//延时销毁
                        if(bm.bitmap!=null && !bm.bitmap.isRecycled()){
                            bm.bitmap.recycle();
                        }
                        map.remove(bm);
                    }
                }
            }).start();

        }

    }
}
