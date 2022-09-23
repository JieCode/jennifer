package com.jennifer.jennifer.test;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * @author jingjie
 * @date :2022/1/22 15:36
 * TODO:
 */
public class SemesterTest {
    public static void main(String[] args) {
        // 从键盘接收数据
        while (true) {
            Scanner scan = new Scanner(System.in);
            printSemester(scan.nextLine());
        }
    }

    private static void printSemester(String str) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        try {
            long currentTime = dateToStamp(str);
            long currentYearLastSemester = dateToStamp(currentYear + "-01-22 00:00:00");
            long currentYearNextSemester = dateToStamp(currentYear + "-07-15 00:00:00");
            if (currentTime > currentYearLastSemester && currentTime < currentYearNextSemester) {
                //下册
                System.out.println("下册");
            } else  {
                //上册
                System.out.println("上册");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        String res;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return ts;
    }
}
