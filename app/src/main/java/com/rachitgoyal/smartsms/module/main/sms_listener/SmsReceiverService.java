package com.rachitgoyal.smartsms.module.main.sms_listener;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.rachitgoyal.smartsms.R;
import com.rachitgoyal.smartsms.module.main.MainActivity;
import com.rachitgoyal.smartsms.module.main.MainPresenter;
import com.rachitgoyal.smartsms.util.NotificationHelper;

public class SmsReceiverService extends Service implements SmsListener {

    private static final String NOTIFICATION_CHANNEL_ID = "10002";
    private static final String NOTIFICATION_CHANNEL_NAME = "Ongoing";

    private SmsReceiver mSmsReceiver;

    public static Intent startService(Context context) {
        return new Intent(context, SmsReceiverService.class);
    }

    public SmsReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSmsReceiver = new SmsReceiver();
        registerReceiver(mSmsReceiver, new IntentFilter(SmsReceiver.SMS_RECEIVED));
        mSmsReceiver.bindListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        return Service.START_STICKY;
    }

    private void startForegroundService() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        } else {
            mBuilder = new NotificationCompat.Builder(this);
        }
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setSubText(getString(R.string.sms_receiver))
                .setContentText(getString(R.string.launch_smartsms))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSmsReceiver != null) {
            unregisterReceiver(mSmsReceiver);
            mSmsReceiver.bindListener(null);
        }
    }

    @Override
    public void messageReceived(String address, String messageText, String date) {
        MainPresenter presenter = new MainPresenter();
        String name = presenter.getContactName(getContentResolver(), address);
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotification(name == null || name.isEmpty() ? address : name, messageText);
        sendBroadcastToActivity();
    }

    private void sendBroadcastToActivity() {
        sendBroadcast(new Intent(MainActivity.SmsBroadcastReceiver.ACTION));
    }
}
