package com.flexfare.android.social.enums;

/**
 * Created by kodenerd on 10/9/17.
 */

public enum ProfileStatus {
    PROFILE_CREATED(0), NOT_AUTHORIZED(1), NO_PROFILE(2);

    int status;

    ProfileStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
