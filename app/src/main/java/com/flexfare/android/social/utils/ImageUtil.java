package com.flexfare.android.social.utils;

import com.flexfare.android.social.enums.UploadImagePrefix;

import java.util.Date;

/**
 * Created by kodenerd on 10/9/17.
 */

public class ImageUtil {

    public static String generateImageTitle(UploadImagePrefix prefix, String parentId) {
        if (parentId != null) {
            return prefix.toString() + parentId;
        }

        return prefix.toString() + new Date().getTime();
    }
}
