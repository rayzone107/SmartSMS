package com.rachitgoyal.smartsms.module.main;

import android.content.ContentResolver;

import com.rachitgoyal.smartsms.model.Model;

import java.util.List;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public interface MainContract {

    interface Presenter {
        List<Model> readAllSMS(ContentResolver contentResolver);

        String getContactName(ContentResolver contentResolver, String phoneNumber);
    }
}
