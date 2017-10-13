package com.flexfare.android.social.managers.listeners;

import com.flexfare.android.social.model.PostListResult;

/**
 * Created by kodenerd on 10/9/17.
 */

public interface OnPostListChangedListener<Post> {

    public void onListChanged(PostListResult result);
}
