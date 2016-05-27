package com.zhhtao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangHaiTao on 2016/5/6.
 */
public class FormatUtil {

    /**
     * 将long类型的ms数 转换成日期
     * @param time
     * @return
     */
    public static String getDate(long time) {
        String date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(time);
        return date;
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getNowDate() {
        return getDate(new Date().getTime());
    }

    /**
     * 将long类型的ms数 转换成日期和时间
     * @param time
     * @return
     */
    public static String getDateTime(long time) {
        String date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = format.format(time);
        return date;
    }

    /**
     * 获取当前日期和时间
     * @return
     */
    public static String getNowDateTime() {
        return getDateTime(new Date().getTime());
    }

    /**
     * 根据字符串生成日期
     * @param dateString
     * @return
     */
    Date getDateWithDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


}
