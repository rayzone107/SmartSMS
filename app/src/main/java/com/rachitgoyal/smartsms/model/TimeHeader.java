package com.rachitgoyal.smartsms.model;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public class TimeHeader extends Model {
    private String timeSegment;
    private String date;
    private int itemCount;

    public TimeHeader() {
    }

    public TimeHeader(String timeSegment, String date, int itemCount) {
        this.timeSegment = timeSegment;
        this.date = date;
        this.itemCount = itemCount;
    }

    public String getTimeSegment() {
        return timeSegment;
    }

    public void setTimeSegment(String timeSegment) {
        this.timeSegment = timeSegment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
