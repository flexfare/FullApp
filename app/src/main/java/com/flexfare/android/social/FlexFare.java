package com.flexfare.android.social;

import android.app.Application;

import com.flexfare.android.social.managers.DatabaseHelper;
import com.google.firebase.database.FirebaseDatabase;

//import com.devs.acr.AutoErrorReporter;

/**
 * Created by kodenerd on 9/18/17.
 */

public class FlexFare extends Application {

    public static final String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        ApplicationHelper.initDatabaseHelper(this);
        //DatabaseHelper.getInstance(this).subscribeToNewPosts();

//        AutoErrorReporter.get(this)
//                .setEmailAddresses("cazewonder@gmail.com")
//                .setEmailSubject("I found this error")
//                .start();
    }
}
