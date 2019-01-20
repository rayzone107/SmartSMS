package com.rachitgoyal.smartsms.module.main.sms_listener;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public interface SmsListener {
    public void messageReceived(String address, String messageText, String date);
}
