package ddw.com.richang.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具
 */
public class CommonUtils {
    private static final String EMPTY_IP_ADDRESS = "0.0.0.0";
    private static final int INT_LENGTH = 9;


    /***
     * 手机号码验证（严格）
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * md5加密 返回32位加密的结果
     *
     * @param src
     * @return
     * @see [类�?�类#方法、类#成员]
     */
    public static String md5Encode(String src) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            md.update(src.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    /**
     * �?查网络连�?
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        if (null == context) {
            return null;
        }

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info;
    }

    public static WifiInfo getWifiInfo(Context context) {
        if (null == context) {
            return null;
        }

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }

    /**
     * 获得模拟器的IP地址
     *
     * @return String 本机ip地址
     */
    public static String getLocalIpAddress(Context context) {
        String address = "";

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();

        address = Formatter.formatIpAddress(ipAddress);

        if (!StringUtils.isEmpty(address) && address.length() >= EMPTY_IP_ADDRESS.length()
                && !EMPTY_IP_ADDRESS.equals(address)) {

            return address;
        }

        NetworkInterface intf = null;
        InetAddress inetAddress = null;
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> enumIpAddr = null;

            for (; en.hasMoreElements(); ) {
                intf = en.nextElement();
                enumIpAddr = intf.getInetAddresses();

                for (; enumIpAddr.hasMoreElements(); ) {
                    inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        address = inetAddress.getHostAddress();

                        return address;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.i("", ex.toString());
        }

        return address;
    }

    /**
     * 字符串转整型
     *
     * @param str
     * @return
     */
    public static int parseINT(String str, int defaultInt) {
        int rInt = defaultInt;

        if (StringUtils.isEmpty(str)) {
            return rInt;
        }

        if (INT_LENGTH < str.length()) {
            str = str.substring(str.length() - INT_LENGTH);
        }

        try {
            rInt = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            rInt = defaultInt;

        }

        return rInt;
    }

    /**
     * 获取文件大小
     *
     * @param url
     * @return
     */
    public static long getFileSize(String url) {
        if (StringUtils.isEmpty(url)) {
            return 0;
        }

        File file = new File(url);

        if (file.isDirectory()) {
            return 0;
        }

        return file.length();
    }

    /**
     * 获取List的长�?
     *
     * @param list
     * @return list为空指针或�?�空集合，返�?0；否则返回具体长�?
     */
    public static int sizeOf(List<?> list) {
        if (null == list) {
            return 0;
        }

        return list.size();
    }

    public static String appendStr(String... strings) {
        StringBuffer sb = new StringBuffer();

        if (null != strings && strings.length > 0) {
            for (String str : strings) {
                sb.append(str);
            }
        }

        return sb.toString();
    }


    /**
     * autolistview
     *
     * @return
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    /**
     * autolistview
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    /**
     * 统一处理IO的关闭操�?
     *
     * @param c
     */
    public static void closeIO(Closeable c) {
        if (null == c) {
            return;
        }

        try {
            c.close();
        } catch (IOException e) {

        }
    }

    public static <T> T getCastObject(Class<T> c, Object obj) {
        if (null == obj || null == c) {

            return null;
        }

        T body = null;

        if (c.equals(obj.getClass())) {
            try {
                body = c.cast(obj);
            } catch (ClassCastException e) {

            }
        }

        return body;
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i],
                            defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }


    /**
     * obj转map
     *
     * @param map 转出的map
     * @param obj 需要转换的对象
     */
    public static void javaBeanToMap(Map<String, String> map, Object obj) {
        // 获得对象所有属性
        Field fields[] = obj.getClass().getDeclaredFields();
        Field field = null;
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            field.setAccessible(true);// 修改访问权限
            try {
                String key = field.getName();
                String value = (String) field.get(obj);
                map.put(key, (String) value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } // 读取属性值
        }
    }

    /**
     * 获取时间差
     * <P>数据格式:2016-12-12 00:00:00</P>
     */
    public static String getTimeDiff(String deadLine) {
        String timeDiff = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = df.parse(time);
            Date date = df.parse(deadLine);
            long l = date.getTime() - now.getTime();
            if (l < 0) {
                timeDiff = "该订单不可接";
            } else {
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                timeDiff = "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDiff;
    }


    /**
     * 获取外网IP
     *
     * @param params
     * @return
     */
    private static String GetNetIp2(String params) {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL(params);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream,
                        "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                //从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}", start);
                line = strber.substring(start, end + 1);
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将位图转换为二进制
     *
     * @param bmp         图片
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 字符串转换为集合
     *
     * @param src
     * @return
     */
    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            result.add(src[i]);
        }
        return result;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getDpi(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        int height = 0;
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            height = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getWpi(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        int width = 0;
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            width = dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return width;
    }

    /**
     * 获取屏幕宽度
     *
     * @param poCotext
     * @return
     */
    public static int[] getScreenWH(Context poCotext) {
        WindowManager wm = (WindowManager) poCotext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return new int[]{width, height};
    }

    /**
     * 获取当前虚拟高度
     *
     * @param poCotext
     * @return
     */
    public static int getVrtualBtnHeight(Context poCotext) {
        int location[] = getScreenWH(poCotext);
        int realHeiht = getDpi((Activity) poCotext);
        int virvalHeight = realHeiht - location[1];
        return virvalHeight;
    }


    /**
     * 获取当前虚拟宽度
     *
     * @param poCotext
     * @return
     */
    public static int getVrtualBtnWidth(Context poCotext) {
        int location[] = getScreenWH(poCotext);
        int realWidth = getWpi((Activity) poCotext);
        int virvalWidth = realWidth - location[1];
        return virvalWidth;
    }

    /**
     * 取得版本号
     *
     * @param context
     * @return
     */
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }



}
