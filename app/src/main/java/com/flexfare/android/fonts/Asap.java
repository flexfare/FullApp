package com.flexfare.android.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by kodenerd on 9/17/17.
 */

public class Asap extends AppCompatTextView {

    public Asap(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public Asap(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public Asap(Context context) {
        super(context);
    }
    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/asap.ttf");
        setTypeface(tf, 1);
    }
}
