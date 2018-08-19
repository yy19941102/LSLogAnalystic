package com.qianfeng.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @DESCRIPTION 全局的时间工具类
 */
public class TimeUtil {
    private static final String DEFAULT_DATA_FORMAT = "yyyy-MM-dd"; // 默认时间格式

    /**
     * 判断时间是否有效,返回true:有效
     *
     * @param date
     * @return
     */
    public static boolean isValidateData(String date) {
        Matcher matcher = null;
        Boolean res = false;
        String regexp = "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
        if (StringUtils.isNotEmpty(date)) {
            Pattern pattern = Pattern.compile(regexp);
            matcher = pattern.matcher(date);
        }

        return false;
    }

    /**
     * 获取昨天默认格式的日期
     *
     * @return
     */
    public static String getYesterday() {
        return getYesterday(DEFAULT_DATA_FORMAT);
    }

    /**
     * 获取昨天指定格式的日期
     *
     * @param pattern
     * @return
     */
    public static String getYesterday(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 将时间戳转化为指定格式的日期
     *
     * @param time 15289878934549
     * @return 2018-07-05
     */
    public static String parseLong2String(long time) {
        return parseLong2String(time, DEFAULT_DATA_FORMAT);
    }

    public static String parseLong2String(long time, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    /**
     * 将默认的日期格式转化为时间戳
     *
     * @param date
     * @return
     */
    public static long parseLong2String(String date) {
        return parseString2Long(date, DEFAULT_DATA_FORMAT);
    }

    public static long parseString2Long(String date, String pattern) {
        Date dt = null;
        try {
            dt = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt == null ? 0 : dt.getTime();
    }


}
