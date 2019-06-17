package com.jennifer.jennifer.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by wutongtech_shengmao on 2017/7/7 15:24.
 * 作用：时间转换工具类
 */

public class TimeUtils {

    /**
     * 获取指定类型的格式转换器
     *
     * @param partten 格式
     * @return
     */
    public static SimpleDateFormat getFormat(String partten) {
        return new SimpleDateFormat(partten);
    }

    /**
     * 将毫秒时间转换成字符串
     *
     * @param timeMillis
     * @param pattern    如果该参数为空，则默认为yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String timeFormat(long timeMillis, String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }


    /**
     * 获取指定时间的毫秒值
     *
     * @param time 时间"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long timeFormat(String time) {

        long getTime = 0;
        try {
            getTime = getFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            getTime = 0;
        }

        return getTime;
    }

    /**
     * 将时间转换成HH:mm:ss
     *
     * @param time 时间毫秒数
     * @return
     */
    public static String getTime(long time) {

        int seconds = (int) (time / 1000);  // 3700

        int hour = seconds / 3600;
        int minute = (seconds - hour * 3600) / 60;
        int second = seconds - hour * 3600 - minute * 60;

        String result = "";
        result = (hour < 10 ? "0" + hour : hour) + ":" +
                (minute < 10 ? "0" + minute : minute) + ":" +
                (second < 10 ? "0" + second : second);
        return result;
    }

    /**
     * 将时间转换成HH:mm:ss或mm:ss
     *
     * @param time 时间毫秒数
     * @return
     */
    public static String getTimeVideo(long time) {

        int seconds = (int) (time / 1000);  // 3700

        int hour = seconds / 3600;
        int minute = (seconds - hour * 3600) / 60;
        int second = seconds - hour * 3600 - minute * 60;

        String result = "";
        if (hour <= 0)
            result = (minute < 10 ? "0" + minute : minute) + ":" +
                    (second < 10 ? "0" + second : second);
        else
            result = (hour < 10 ? "0" + hour : hour) + ":" +
                    (minute < 10 ? "0" + minute : minute) + ":" +
                    (second < 10 ? "0" + second : second);
        return result;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的时间转换成yyyy.MM.dd  HH:mm
     *
     * @param time 时间
     * @return 转换后的时间字符串
     */
    public static String getShowTime(String time) {
        long timeMillis = timeFormat(time);
        SimpleDateFormat format = getFormat("yyyy-MM-dd  HH:mm");
        return format.format(new Date(timeMillis));
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的时间转换成yyyy年MM月dd日 HH:mm:ss-HH:mm:ss的格式
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return yyyy年MM月dd日 HH:mm:ss-HH:mm:ss （开始-结束）
     */
    public static String getLessonShowTime(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return "";
        }
        long start = timeFormat(startTime);
        long end = timeFormat(endTime);
        if (start == 0 || end == 0) {
            return "";
        }
        SimpleDateFormat format1 = getFormat("yyyy年MM月dd日 HH:mm:ss");
        SimpleDateFormat format2 = getFormat("HH:mm:ss");
        try {
            return format1.format(new Date(start)) + "-" + format2.format(new Date(end));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的时间转换成MM月dd日 HH:mm-HH:mm的格式
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return MM月dd日 HH:mm-HH:mm （开始-结束）
     */
    public static String getLessonShowTime2(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return "";
        }
        long start = timeFormat(startTime);
        long end = timeFormat(endTime);
        if (start == 0 || end == 0) {
            return "";
        }
        SimpleDateFormat format1 = getFormat("MM月dd日 HH:mm");
        SimpleDateFormat format2 = getFormat("HH:mm");
        try {
            return format1.format(new Date(start)) + "-" + format2.format(new Date(end));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的时间转换成yyyy年MM月dd日 HH:mm:ss-HH:mm:ss的格式
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return yyyy年MM月dd日 HH:mm:ss-HH:mm:ss （开始-结束）
     */
    public static String getLessonShowTime(String startTime, long endTime) {
        if (TextUtils.isEmpty(startTime)) {
            return "";
        }
        if (endTime < 0) {
            endTime = 0;
        }
        long start = timeFormat(startTime);
        long end = start + endTime;
        if (start == 0 || end == 0) {
            return "";
        }
        SimpleDateFormat format1 = getFormat("yyyy年MM月dd日 HH:mm:ss");
        SimpleDateFormat format2 = getFormat("HH:mm:ss");
        try {
            return format1.format(new Date(start)) + "-" + format2.format(new Date(end));
        } catch (Exception e) {
            return "";
        }
    }

    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String getCoachLength(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return "";
        }
        long start = timeFormat(startTime);
        long end = timeFormat(endTime);
        if (start == 0 || end == 0) {
            return "";
        }
        return getTime(end - start);
    }

    /**
     * 格式化时间 当年格式MM月dd日 HH:mm，其他年格式yyyy年MM月dd日 HH:mm
     *
     * @param time 带格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getFormatTime(String time) {
        long start = timeFormat(time);
        Calendar calendar = Calendar.getInstance();
        long currentFirstTime = getTimeMillis(calendar.get(Calendar.YEAR), 1, 1, 0, 0, 0);//获取当年第一秒时间戳;//获取当年第一秒时间戳
        SimpleDateFormat format;
        if (start < currentFirstTime) { //不是当年返回 yyyy年MM月dd日 HH:mm
            format = getFormat("yyyy-MM-dd HH:mm");
        } else { //当年返回 MM月dd日 HH:mm
            format = getFormat("MM-dd HH:mm");
        }
        return format.format(new Date(start));
    }

    /**
     * 格式化时间 当年格式MM月dd日 HH:mm-HH:mm，其他年格式yyyy年MM月dd日 HH:mm=HH:mm
     *
     * @param startTime 带格式yyyy-MM-dd HH:mm:ss
     * @param endTime   带格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getFormatTime(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime))
            return "";
        long start = timeFormat(startTime);
        Calendar calendar = Calendar.getInstance();
        long currentFirstTime = getTimeMillis(calendar.get(Calendar.YEAR), 1, 1, 0, 0, 0);//获取当年第一秒时间戳;//获取当年第一秒时间戳
        SimpleDateFormat format;
        if (start < currentFirstTime) { //不是当年返回 yyyy年MM月dd日 HH:mm
            format = getFormat("yyyy-MM-dd HH:mm");
        } else { //当年返回 MM月dd日 HH:mm
            format = getFormat("MM-dd HH:mm");
        }
        startTime = format.format(new Date(start));
        if (TextUtils.isEmpty(endTime))
            return startTime;
        long end = timeFormat(endTime);
        format = getFormat("HH:mm");
        endTime = format.format(new Date(end));
        return startTime + "-" + endTime;
    }

    /**
     * 格式化时间 当天格式HH:mm,昨天格式 昨天HH:mm,当年格式MM-dd HH:mm，其他年格式yyyy-MM-dd HH:mm
     *
     * @param time 带格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getFormatTimeDetail(String time) {
        long timeMillis = timeFormat(time);
        Calendar calendar = Calendar.getInstance();
        long yearFirstTime = getTimeMillis(calendar.get(Calendar.YEAR), 1, 1, 0, 0, 0);//获取当年第一秒时间戳
        long yesterdayFirstTime = getTimeMillis(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);//昨天第一秒
        long todayFirstTime = getTimeMillis(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);//今天第一秒
        SimpleDateFormat format;
        if (timeMillis < yearFirstTime) { //不是当年返回 yyyy年MM月dd日 HH:mm
            format = getFormat("yyyy-MM-dd HH:mm");
        } else {
            if (timeMillis > todayFirstTime)
                return getFormat("HH:mm").format(new Date(timeMillis));//今天返回HH:mm
            else if (timeMillis > yesterdayFirstTime && timeMillis < todayFirstTime)
                return "昨天" + getFormat("HH:mm").format(new Date(timeMillis));//昨天返回 昨天HH:mm
            else
                return getFormat("MM-dd HH:mm").format(new Date(timeMillis));//当年返回 MM月dd日 HH:mm
        }
        return format.format(new Date(timeMillis));
    }

    /**
     * 根据具体时间获取时间戳
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static long getTimeMillis(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTimeInMillis();
    }

}
