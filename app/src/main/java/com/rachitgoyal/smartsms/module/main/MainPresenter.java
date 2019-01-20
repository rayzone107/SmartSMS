package com.rachitgoyal.smartsms.module.main;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

import com.rachitgoyal.smartsms.model.Message;
import com.rachitgoyal.smartsms.model.Model;
import com.rachitgoyal.smartsms.model.TimeHeader;
import com.rachitgoyal.smartsms.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public class MainPresenter implements MainContract.Presenter {

    public MainPresenter() {
    }

    @Override
    public List<Model> readAllSMS(ContentResolver contentResolver) {
        Uri inboxUri = Uri.parse("content://sms/inbox");

        String[] reqCols = new String[]{"_id", "person", "address", "body", "date", "read", "seen"};
        Cursor cursor = contentResolver.query(inboxUri, reqCols, null, null, "date DESC");

        List<Message> messages = new ArrayList<>();
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast() && count < 100) {
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String person = getContactName(contentResolver, address);
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String read = cursor.getString(cursor.getColumnIndex("read"));
                String seen = cursor.getString(cursor.getColumnIndex("seen"));
                Message message = new Message(_id, person, address, body, date, read, seen);
                messages.add(message);
                count++;
                cursor.moveToNext();
            }
            cursor.close();
        }
        return createListWithHeaders(messages);
    }

    @Override
    public String getContactName(ContentResolver contentResolver, String phoneNumber) {
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = contentResolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    private List<Model> createListWithHeaders(List<Message> messages) {
        List<Model> models = new ArrayList<>();
        List<Message> messagesInCurrentHour = new ArrayList<>();

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentDate = Calendar.getInstance().get(Calendar.DATE);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            Long timestamp = Long.parseLong(message.getDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            boolean moveToPreviousHour = false;

            if (currentHour != hour || currentDate != date || currentMonth != month || currentYear != year) {
                moveToPreviousHour = true;
            }

            if (moveToPreviousHour && !messagesInCurrentHour.isEmpty()) {
                addItemsToList(models, messagesInCurrentHour, currentHour, currentDate, currentMonth, currentYear);
            }
            messagesInCurrentHour.add(message);
            currentHour = hour;
            currentDate = date;
            currentMonth = month;
            currentYear = year;

            if (i == messages.size() - 1 && !messagesInCurrentHour.isEmpty()) {
                addItemsToList(models, messagesInCurrentHour, currentHour, currentDate, currentMonth, currentYear);
            }
        }
        return models;
    }

    private void addItemsToList(List<Model> models, List<Message> messagesInCurrentHour, int currentHour, int currentDate,
                                int currentMonth, int currentYear) {
        TimeHeader th = new TimeHeader();
        th.setTimeSegment(StringUtils.convertHourToTimeRange(currentHour + 1));
        th.setDate(currentDate + "/" + (currentMonth + 1) + "/" + currentYear);
        th.setItemCount(messagesInCurrentHour.size());
        models.add(th);
        models.addAll(messagesInCurrentHour);
        messagesInCurrentHour.clear();
    }
}
