package ddw.com.richang.controller.data.oldversion;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dingdewen on 15/7/10.
 * 此类功能包括：
 * 构造函数
 * 传入Activity的Context，若不传参只能使用文件读写
 * 文件读写
 * writeFile(fileName,content)
 * getFile(fileName,[default])
 * Sharedpreference读写
 * writeSharedPreference(key,value)
 * getSharedPreference(key,[default])
 * sqlite数据库
 * startSQL([database])
 * endSQL()
 * SQL_execute(sql)
 * SQL_select_list(sql)
 */

public class Storage {
    protected Context context;//存储实例化过程中Activity的Content
    protected String Error;//记录抛出的异常
    //SharedPreference
    protected String shareId;
    //SQL:
//    注意：使用SQL相关方法必须先调用startSQL(),可传入数据库名作为参数。之后调用endSQL()释放资源
    protected SQLiteDatabase database;
    protected String databaseName;

    //    构造函数
//    如果不传入Context，则数据库和sharedPreference不可用
    public Storage() {
        this.context = null;
    }

    public Storage(Context context) {
        this.context = context;
    }

    //  向文件写入字符串,成功返回true,失败返回false
    public boolean writeFile(String fileName, String content) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() +
                "/" + fileName;
        try {
            String dirPath = path.substring(0, path.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.isDirectory())
                dir.mkdirs();
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = content.getBytes();
            out.write(buffer, 0, buffer.length);
            return true;
        } catch (Exception e) {
            Error = e.getMessage();
            return false;
        }
    }

    //    获取文件内容，如果获取失败则返回设置的默认参数值
    public String getFile(String fileName, String defaultStr) {
        String content = this.getFile(fileName);
        if (null == content)
            return defaultStr;
        else
            return content;
    }

    //    获取文件并以字符串形式返回
    public String getFile(String fileName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() +
                "/" + fileName;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;//文件不存在返回空
            }
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            String content = "";
            while (in.read(buffer, 0, buffer.length) > 0) {
                content += new String(buffer);
            }
            return content;
        } catch (Exception e) {
            Error = e.getMessage();
            return null;
        }
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public boolean setSharedPreference(String key, Object value) {
        if (null == context)
            return false;
        try {
            if (null == this.shareId)
                this.shareId = "storage";
            SharedPreferences sharedPreferences = context.getSharedPreferences(this.shareId,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (value instanceof String)
                editor.putString(key, (String) value);
            else if (value instanceof Integer)
                editor.putInt(key, (Integer) value);
            else if (value instanceof Float)
                editor.putFloat(key, (Integer) value);
            else if (value instanceof Boolean)
                editor.putBoolean(key, (Boolean) value);
            else if (value instanceof Long)
                editor.putLong(key, (Long) value);
            else
                editor.putString(key, value.toString());
            return editor.commit();
        } catch (Exception e) {
            Error = e.getMessage();
            return false;
        }
    }

    //    获取sharedPreference，如果获取失败返回默认参数值
    public Object getSharedPreference(String key, Object defaultObj) {
        Object obj = this.getSharedPreference(key);
        if (null == obj)
            return defaultObj;
        else
            return obj;
    }

    //    获取sharedPreference
    public Object getSharedPreference(String key) {
        if (null == context)
            return null;
        if (null == this.shareId)
            this.shareId = "storage";
        SharedPreferences sharedPreferences = context.getSharedPreferences(this.shareId, Context
                .MODE_PRIVATE);
        Map<String, Object> map = (Map<String, Object>) sharedPreferences.getAll();
        return map.get(key);
    }

    public void setDatabaseName(String name) {
        this.databaseName = name;
    }

    public boolean startSQL() {
        if (null == this.databaseName || this.databaseName.equals(""))
            this.databaseName = "c0_storage_cl";
        return this.startSQL(this.databaseName);
    }

    public boolean startSQL(String databaseName) {
        if (null == context)
            return false;
        try {
            SQL data = new SQL(context, databaseName, null, 1);
            database = data.getWritableDatabase();
            return true;
        } catch (Exception e) {
            Error = e.getMessage();
            return false;
        }
    }

    public void endSQL() {
        if (null != database) {
            database.close();
        }
    }

    //执行SQL语句
    public boolean SQL_execute(String sql) {
        Matcher m = Pattern.compile("^(insert|update|delete|drop|alter).*").matcher(sql.trim());
        if (!m.find())
            return false;
        try {
            database.execSQL(sql);
            return true;
        } catch (Exception e) {
            Error = e.getMessage();
            return false;
        }
    }

    //    执行select语句，并返回数组
    public ArrayList<HashMap<String, String>> SQL_select(String sql) {
        Matcher m = Pattern.compile("^select.*", Pattern.CASE_INSENSITIVE).matcher(sql.trim());
        if (!m.find())
            return null;
        Cursor cursor;
        try {
            cursor = database.rawQuery(sql, new String[]{});
            if (null == cursor)
                return null;
            if (!cursor.moveToFirst())
                return null;
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            list.clear();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.clear();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String key = cursor.getColumnName(i);
                    String value = cursor.getString(i);
                    map.put(key, value);
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            Error = e.getMessage();
            return null;
        }
    }


    //定义内部类
    protected class SQL extends SQLiteOpenHelper {
        public SQL(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
            super(context, name, cursorFactory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}


