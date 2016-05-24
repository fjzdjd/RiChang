package ddw.com.richang.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public final class StringUtils
{
	private static StringBuffer strBf = new StringBuffer();
	
    /**
     * 字符串是否为空或空字符串
     * 
     * @param str
     * @return true:字符串为空或空字符串; false:字符串不是空字符�?
     */
    public static boolean isEmpty(String str)
    {
        return (null == str || str.isEmpty());
    }
    
    public static String appendStr(String... strings)
    {
    	strBf.setLength(0);
    	for(String str : strings)
    	{
    		strBf.append(str);
    	}
    	return strBf.toString();
    }

    public String[] getStrs(String str)
    {
        return str.split(" ");
    }

    public static boolean isLetter(String str)
    {
        return str.matches("[A-Za-z]+");
    }

    public static boolean isNumber(String str)
    {
        return str.matches("[0-9]+");
    }

    public static boolean isHanzi(String str)
    {
        return str.matches("[�?-龥]+");
    }

    public String getString(String[] strs, int type)
    {
        String[] newStrs = new String[strs.length];
        int j = 0;
        for (int i = 0; i < strs.length; i++)
        {
            String firstLetter = strs[i].substring(0, 1);
            if (isLetter(firstLetter))
            {
                if (type == 0)
                {
                    newStrs[j] = firstLetter;
                }
                else
                {
                    newStrs[j] = (strs[i] + " ");
                }
                j++;
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < newStrs.length; k++)
        {
            if (newStrs[k] != null)
            {
                sb.append(newStrs[k]);
            }
        }
        return sb.toString();
    }

    public static String parseMoblie(String mobile)
    {
        if (mobile == null)
            return null;
        return mobile.replace("-", "");
    }

    public static String parseIP(String ipAndPort)
    {
        return parseAddress(ipAndPort, 0);
    }

    public static String parsePort(String ipAndPort)
    {
        return parseAddress(ipAndPort, 1);
    }

    public static String parseNode(String src, String node)
    {
        if ((isEmpty(src)) || (isEmpty(node)))
        {
            return null;
        }
        int begin = src.indexOf("<" + node + ">");
        int end = src.indexOf("</" + node + ">");
        String returnString = src.substring(begin + node.length() + 2, end)
                .trim();

        return returnString;
    }

    public static String GetNodeData(String src, String node)
    {
        if ((isEmpty(src)) || (isEmpty(node)))
        {
            return null;
        }
        int begin = src.indexOf("<" + node);
        int end = src.indexOf("</" + node + ">");
        String returnString = src.substring(begin, end + node.length() + 3)
                .trim();

        return returnString;
    }

    public static String GetCDataString(String src)
    {
        if ((isEmpty(src)) || (isEmpty("<![CDATA[")) || (isEmpty("]]")))
        {
            return null;
        }
        int begin = src.indexOf("<![CDATA[");
        int end = src.lastIndexOf("]]");
        if ((begin == -1) || (end == -1))
        {
            return null;
        }
        String returnString = src.substring(begin + "<![CDATA[".length(), end)
                .trim();

        return returnString;
    }

    private static String parseAddress(String ipAndPort, int flag)
    {
        if ((ipAndPort == null) || ("".equals(ipAndPort.trim())))
        {
            return null;
        }

        int end = ipAndPort.indexOf(":");

        if (end <= 0)
        {
            return null;
        }

        String temp = null;

        switch (flag)
        {
            case 0:
                temp = ipAndPort.substring(0, end);
                break;
            case 1:
                temp = ipAndPort.substring(end + 1);
        }

        return temp;
    }

    public static String getStringFromStream(InputStream inputStream)
    {
        if (inputStream == null)
        {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                inputStream));
        String returnString = null;
        try
        {
            char[] buf = new char[1024];
            StringBuffer sb = new StringBuffer();
            int b;
            while ((b = br.read(buf)) != -1)
            {
                sb.append(buf, 0, b);
            }
            returnString = sb.toString();
            sb = null;
            buf = (char[]) null;

            inputStream.close();
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return returnString;
    }

    public static String subString(String string, String trimString)
    {
        if ((isEmpty(string)) || (isEmpty(trimString)))
        {
            return null;
        }

        return string.substring(trimString.length());
    }

    public static byte[] stringToBytes(String s, int length)
    {
        while (s.getBytes().length < length)
        {
            s = s + " ";
        }
        return s.getBytes();
    }

    public static String bytesToString(byte[] b)
    {
        StringBuffer result = new StringBuffer("");
        int length = b.length;
        for (int i = 0; i < length; i++)
        {
            result.append((char) (b[i] & 0xFF));
        }
        return result.toString();
    }

    public static String string2Unicode(String s)
    {
        try
        {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = s.getBytes("unicode");
            for (int i = 2; i < bytes.length - 1; i += 2)
            {
                out.append("u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++)
                {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);

                out.append(str);
                out.append(str1);
                out.append(" ");
            }
            return out.toString().toUpperCase(Locale.CHINA);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String unicode2String(String unicodeStr)
    {
        StringBuffer sb = new StringBuffer();
        String str[] = unicodeStr.toUpperCase(Locale.CHINA).split("U");
        for (int i = 0; i < str.length; i++)
        {
            if (str[i].equals(""))
                continue;
            char c = (char) Integer.parseInt(str[i].trim(), 16);
            sb.append(c);
        }
        return sb.toString();
    }

    public static String toString(String str)
    {
        return null == str ? "" : str;
    }
    
    public static String getValueFromUrl(String url, String key)
    {
        if(isEmpty(url) || isEmpty(key))
        {
            return null;
        }
        
        String value = null;
        if(url.contains(key))
        {
            value = url.substring(url.indexOf(key));
            if(value.contains("&"))
            {
                value = value.substring(key.length(), value.indexOf('&'));
            }
        }
        return value;
    }
    
    public static String localPathToUri(String localPath)
    {
        return "file://" + localPath;
    }
    
    public static boolean isNetUrl(String url)
    {
        if(isEmpty(url))
        {
            return false;
        }
        
        if(url.startsWith("http://"))
        {
            return true;
        }
        
        if(url.startsWith("https://"))
        {
            return true;
        }
        
        if(url.startsWith("ftp://"))
        {
            return true;
        }
        
        return false;
    }
}
