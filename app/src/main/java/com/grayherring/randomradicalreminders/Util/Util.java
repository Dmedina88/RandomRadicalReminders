package com.grayherring.randomradicalreminders.Util;

/**
 * Created by David on 12/23/2014.
 */
public class Util {

    public static final String LOG = "RRRLOG";
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    public static long timeStringToMillis(String time) {

        long hour = getHour(time);
        long minute = getMinute(time);
        hour = hour * HOUR;
        minute = minute * MINUTE;
        return hour + minute;
    }

    public static int getHour(String time) {
        String[] pieces = time.split(":");

        return (Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces = time.split(":");

        return (Integer.parseInt(pieces[1]));
    }

}
