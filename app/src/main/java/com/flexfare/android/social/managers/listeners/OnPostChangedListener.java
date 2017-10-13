package com.flexfare.android.social.managers.listeners;

import com.flexfare.android.social.model.Post;

/**
 * Created by kodenerd on 10/9/17.
 */

public interface OnPostChangedListener {
    public void onObjectChanged(Post obj);

    public void onError(String errorText);
}
