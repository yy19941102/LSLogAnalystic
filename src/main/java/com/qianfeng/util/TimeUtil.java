package com.qianfeng.util;

import com.qianfeng.common.DateEnum;
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

    /**
     * 根据时间戳和type来获取对应值
     * @param time
     * @param type
     * @return
     */
    public static int getDataInfo(long time, DateEnum type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        if (DateEnum.YEAR.equals(type)) {
            return calendar.get(Calendar.YEAR);
        }
        if (DateEnum.SEASON.equals(type)) {
            int month = calendar.get(Calendar.MONTH) + 1;
            // 123  1  ； 456  2 ； 789 3 ；10,11，12 4
            if (month % 3 == 0) {
                return month / 3;
            }
            return month / 3 + 1;
        }
        if (DateEnum.MONTH.equals(type)){
            int month=calendar.get(Calendar.MONTH)+1;
            return month;
        }
        if (DateEnum.WEEK.equals(type)){
            return calendar.get(Calendar.WEEK_OF_YEAR);
        }
        if (DateEnum.DAY.equals(type)){
            return calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (DateEnum.HOUR.equals(type)){
            return calendar.get(Calendar.HOUR_OF_DAY);
        }
        throw new RuntimeException("该类型暂不支持时间信息获取.type:"+type.typeName);
    }

    /**
     * 根据时间戳获取当天所在周的第一天
     * @param time
     * @return
     */
    public static long getFirstDayOfWeek(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_WEEK,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    public static void main(String[] args) {
        System.out.println(getDataInfo(1530720000000l,DateEnum.DAY));
        System.out.println(getDataInfo(1530720000000l,DateEnum.SEASON));
        System.out.println(getDataInfo(1530720000000l,DateEnum.MONTH));
        System.out.println(getFirstDayOfWeek(1530720000000l));
    }
}
