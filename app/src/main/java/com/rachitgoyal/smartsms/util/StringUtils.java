package com.rachitgoyal.smartsms.util;

/**
 * Created by Rachit Goyal on 16/01/19.
 */
public class StringUtils {

    public static String convertHourToTimeRange(int hour) {
        String hourString;
        switch (hour) {
            case 0:
                hourString = "11 PM - 12 AM (midnight)";
                break;
            case 1:
                hourString = "12 AM - 1 AM";
                break;
            case 12:
                hourString = "11 AM - 12 PM (noon)";
                break;
            case 13:
                hourString = "12 PM - 1 PM";
                break;
            default:
                int prevHour = hour - 1;
                hourString = prevHour > 12 ? (prevHour - 12) + " PM" : prevHour + " AM";
                hourString += " - ";
                hourString += hour > 12 ? (hour - 12) + " PM" : hour + " AM";
                break;
        }
        return hourString;
    }
}
