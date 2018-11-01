package com.ti.zbus_manager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public static final String YYYY_MM_DD_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
    public static final String YYYY_MM_DD_hh_mm = "yyyy-MM-dd hh:mm";

    public static SimpleDateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * 获取时间的分
     *
     * @param date
     * @return
     */
    public static int getDateMinute(Date date) {
        String time = DateUtils.getDateFormat(DateUtils.YYYY_MM_DD_hh_mm_ss).format(date);
        String[] strings = time.split(" ");
        String[] split = strings[1].split(":");
        return Integer.parseInt(split[1]);
    }

    /**
     * 获取给定时间前几分钟的时间
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date getMinuteAgoTime(Date date, int minute) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MINUTE, -minute);
        Date time = instance.getTime();
        return time;
    }

    public static Date getNoSecondDate(Date date){
        String dateStr = getDateFormat(YYYY_MM_DD_hh_mm).format(date);
        Date minuteDate = null;
        try {
            minuteDate = getDateFormat(YYYY_MM_DD_hh_mm).parse(dateStr);
            return minuteDate;
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("转化为去掉秒的时间错误");
            return date;
        }
    }
    public static void main(String[] args) {

    }
}
