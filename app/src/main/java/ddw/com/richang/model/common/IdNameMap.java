package ddw.com.richang.model.common;

import java.util.ArrayList;

/**
 * Created by dingdewen on 15/12/15.
 */

public class IdNameMap {
    public long id;
    public String name;
    //id-key，键值，对应的类别
    public IdNameMap(long id,String name){
        this.id=id;
        this.name=name;
    }

    //find
    public static String findNameById(long id,ArrayList<IdNameMap> mylist){
        if(null==mylist) return null;
        for(int i=0;i<mylist.size();i++){
            if(mylist.get(i).id==id) return mylist.get(i).name;
        }
        return null;
    }
    public static long findIdByName(String name,ArrayList<IdNameMap> mylist){
        if(null==mylist) return -1;
        for(int i=0;i<mylist.size();i++){
            if(mylist.get(i).name.equals(name)) return mylist.get(i).id;
        }
        return -1;
    }

}
