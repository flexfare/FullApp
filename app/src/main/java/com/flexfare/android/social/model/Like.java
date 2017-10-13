package com.flexfare.android.social.model;

import java.util.Calendar;

/**
 * Created by kodenerd on 10/9/17.
 */

public class Like {

    private String id;
    private String authorId;
    private long createdDate;


    public Like() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Like(String authorId) {
        this.authorId = authorId;
        this.createdDate = Calendar.getInstance().getTimeInMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedDate() {
        return createdDate;
    }
}
