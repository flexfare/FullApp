package com.flexfare.android.social;

import com.flexfare.android.social.managers.DatabaseHelper;

/**
 * Created by kodenerd on 10/9/17.
 */

public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationHelper.initDatabaseHelper(this);
        DatabaseHelper.getInstance(this).subscribeToNewPosts();
    }
}
