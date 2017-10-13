package com.flexfare.android;

/**
 * Created by Blessyn on 8/16/2017.
 */

public interface LoginValidation {

    void doRegistratio();
    void showLoginSuccess(String result);
    void showServerError(String response);

}
