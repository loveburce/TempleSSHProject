package com.elight.teaching.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dawn on 2014/9/25.
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_MONTH_DAY = "MM月dd日";

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 hh:mm";

    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;     //年
    private static final int MONTH = 30 * 24 * 60 * 60;     //月
    private static final int DAY = 24 * 60 * 60;            //日
    private static final int HOUR = 60 * 60;                //时
    private static final int MINUTE = 60;                   //分钟

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     * @param timestamp
     * @return 返回时间戳
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp){
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;       //与现在时间相差秒数
        Log.d("timeGap","timeGap : "+timeGap);
        String timeStr = null;
        if(timeGap > YEAR){
            timeStr = timeGap /YEAR + "年前";
        } else if(timeGap > MONTH){
            timeStr = timeGap / MONTH + "个月前";
        } else if(timeGap > DAY){
            timeStr = timeGap / DAY +"天前";
        } else if(timeGap > HOUR){
            timeStr = timeGap / HOUR + "小时前";
        } else if(timeGap > MINUTE){
            timeStr = timeGap /MINUTE + "分钟前";
        } else{
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     *  获取当前日期的指定格式的字符串
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format){
        if(format == null || format.trim().equals("")){
            simpleDateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            simpleDateFormat.applyPattern(format);
        }
        return simpleDateFormat.format(new Date());
    }

    /**
     * 将date转化成按指定格式的字符串
     * @param date
     * @param formatType
     * @return
     */
    public static String dateToString(Date date, String formatType){
        return new SimpleDateFormat(formatType).format(date);
    }

    /**
     * long 类型 转化成Date类型 再转化成String类型
     * @param currentTime
     * @param formatType
     * @return
     */
    public static String longToString(long currentTime, String formatType){
        String strTime = "";
        //long 类型转化Date类型的数据
        Date date = longToDate(currentTime, formatType);
        //date类型转化成string
        strTime = dateToString(date, formatType);
        return strTime;
    }

    /**
     * string 转化成date类型
     * @param strTime
     * @param formatType
     * @return
     */
    public static Date stringToDate(String strTime, String formatType){
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = format.parse(strTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    /**
     * long 类型转化成 Date类型
     * @param currentTime
     * @param formatType
     * @return
     */
    public static Date longToDate(long currentTime, String formatType){
        //根据long类型的毫秒数生成一个date类型的时间
        Date dateOld = new Date(currentTime);
        //把date类型的时间转化成string
        String dateTime = dateToString(dateOld, formatType);
        //把string类型转化成date类型
        Date date = stringToDate(dateTime, formatType);
        return  date;
    }

    /**
     * Stirng 类型转化成long类型
     * @param strTime
     * @param formatType
     * @return
     */
    public static long stringToLong(String strTime, String formatType){
        //String 类型转化成date类型
        Date date = stringToDate(strTime, formatType);
        if(date == null){
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     *  date类型转化成为long类型
     * @param date
     * @return
     */
    public static long dateToLong(Date date){
        return date.getTime();
    }

    public static String getChatTime(long time){
        long clearTime = time * 1000;
        String result = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(dateFormat.format(today)) - Integer.parseInt(dateFormat.format(otherDay));

        switch (temp){
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;
            default:
                result = getTime(clearTime);
                break;

        }
        return result;
    }

    public static String getTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time){
        SimpleDateFormat format = new SimpleDateFormat(" HH:mm");
        return format.format(new Date(time));
    }

}
