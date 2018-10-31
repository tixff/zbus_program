package com.ti.zbus_manager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 获取时间的分
     * @param date
     * @return
     */
    public static int getDateMinute(Date date) {
        String time = DateUtils.format.format(date);
        String[] strings = time.split(" ");
        String[] split = strings[1].split(":");
        return Integer.parseInt(split[1]);
    }

    /**
     * 获取30分钟以前的时间
     * @param date
     * @return
     */
    public static Date get30MinuteAgoTime(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MINUTE, -30);
        Date time = instance.getTime();
        return time;
    }

    public static void main(String[] args) {
        Date minuteAgoTime = get30MinuteAgoTime(new Date());
        System.out.println(format.format(minuteAgoTime));
    }
}
