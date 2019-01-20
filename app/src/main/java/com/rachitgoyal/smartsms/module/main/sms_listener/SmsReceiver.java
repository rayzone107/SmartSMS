package com.rachitgoyal.smartsms.module.main.sms_listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public class SmsReceiver extends BroadcastReceiver {

    //interface
    private static SmsListener mListener;

    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                for (SmsMessage receivedMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    if (receivedMessage == null) {
                        break;
                    }
                    String date = String.valueOf(receivedMessage.getTimestampMillis());
                    String address = receivedMessage.getDisplayOriginatingAddress();
                    String  text= receivedMessage.getDisplayMessageBody();
                    mListener.messageReceived(address, text, date);
                }
            }
        }
    }

    public void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
