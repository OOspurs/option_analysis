package com.spurspro.option.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String pattern = "yyyy-MM-dd HH:mm:ss";
    public static final String other_pattern = "yyyy/MM/dd HH:mm:ss";
    public static final String DAY_PATTERN = "yyyy-MM-dd";
    public static final String DAY_OTHER_PATTERN = "yyyy/M/dd";
    public static final String DATE_OTHER_PATTERN = "yyyy-MM-dd HH:mm";


    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    public static String formatDate(Date date, String patten) {
        if (StringUtils.isBlank(patten)) patten = pattern;
        SimpleDateFormat format = new SimpleDateFormat(patten);
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    public static Date parseDate(String datestr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        if (StringUtils.isBlank(datestr)) return null;
        try {
            return format.parse(datestr);
        } catch (ParseException e) {
            format = new SimpleDateFormat(other_pattern);
            try {
                return format.parse(datestr);
            } catch (Exception e1) {
                logger.error("解释日期失败!", e);
            }
            return null;
        }
    }

    public static Date parseDate(String datestr) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        if (StringUtils.isBlank(datestr)) return null;
        try {
            return format.parse(datestr);
        } catch (ParseException e) {
            format = new SimpleDateFormat(other_pattern);
            try {
                return format.parse(datestr);
            } catch (Exception e1) {
                logger.error("解释日期失败!", e);
            }
            return null;
        }
    }

    public static String formatDateToGMT_8(Date date, String patten) {
        if (StringUtils.isBlank(patten)) patten = pattern;
        SimpleDateFormat format = new SimpleDateFormat(patten);
        format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        if (date == null) {
            return "";
        }
        return format.format(date);
    }


    /**
     * 根据日期和时区获取杜当天的时间戳
     * @param date
     * @param timeZone +08:00
     *
     * @since 2.00.02
     */
    public static Long formatDateToDayTimestamp(Date date, String timeZone) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            timeZone = "GMT" + timeZone;
            format.setTimeZone(TimeZone.getTimeZone(timeZone));
            SimpleDateFormat gmtFormat = new SimpleDateFormat(DAY_PATTERN);

            Date newDate = gmtFormat.parse(format.format(date));
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(newDate);
            // 将时分秒,毫秒域清零
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            return cal1.getTimeInMillis();
        } catch (Exception e) {
            logger.error("formatDateToDay error", e);
            return null;
        }
    }


    /**
     * 在原日期上增加天数
     * @param cur
     * @param day
     * @return Long
     */
    public static Long getNewDate(Date cur, Integer day) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(cur);   //设置时间
        cal1.add(Calendar.DATE, day); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
        return cal1.getTimeInMillis();
    }

    /**
     * 获取date的结束时间
     * @param date
     * @since 2.01.06
     */
    public static long getEndOfDate(Date date) {
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(date);
        dateEnd.set(Calendar.HOUR, 23);
        dateEnd.set(Calendar.MINUTE, 59);
        dateEnd.set(Calendar.SECOND, 59);
        dateEnd.set(Calendar.MILLISECOND, 999);
        return dateEnd.getTimeInMillis();
    }

    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            //System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }
}
