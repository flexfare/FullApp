package com.flexfare.android.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by kodenerd on 10/2/17.
 */

public class Qanelas extends AppCompatTextView {

    public Qanelas(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/qanelas.otf"));
    }
}
