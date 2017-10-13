package com.flexfare.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.flexfare.android.R;
import com.flexfare.android.SharedPrefKeys;
import com.flexfare.android.model.ServerResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
//import immortalz.me.library.TransitionsHeleper;
//import immortalz.me.library.bean.InfoBean;
//import immortalz.me.library.method.ColorShowMethod;

/**
 * Created by kodenerd on 9/18/17.
 */

public class AvatarActivity extends AppCompatActivity {

    @Bind(R.id.avatar)
    ImageView avatar;
    Context context;
    String serverResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        TransitionsHeleper.getInstance()
//                .setShowMethod(new ColorShowMethod(R.color.colorPrimary, R.color.colorPrimaryDark) {
//                    @Override
//                    public void loadCopyView(InfoBean bean, ImageView copyView) {
//
//                    }
//                    @Override
//                    public void loadTargetView(InfoBean bean, ImageView targetView) {
//
//                    }
//                }).show(this, avatar);

        Gson jsonResponse = new Gson();
        ServerResponse response = jsonResponse.fromJson(serverResponse, ServerResponse.class);

        Picasso.with(this).load(SharedPrefKeys.USER_ID+SharedPrefKeys.PROFILE_PIC_PATH+response.getMessage().getCarImg()).into(avatar);

        setContentView(R.layout.avatar_activity);

    }

}
