//package com.flexfare.android.activities;
//
//import android.content.Context;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//
//import com.flexfare.android.R;
//
//import me.shaohui.bottomdialog.BaseBottomDialog;
//
///**
// * Created by kodenerd on 9/19/17.
// */
//
//public class PostActivityAlt extends BaseBottomDialog {
//
//    private EditText mEditText;
//
//    @Override
//    public int getLayoutRes(){
//        return R.layout.activity_post_alt;
//    }
//    @Override
//    public void bindView(View v) {
//        mEditText = (EditText) v.findViewById(R.id.etDescription);
//        mEditText.post(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(mEditText, 0);
//            }
//        });
//    }
//    @Override
//    public float getDimAmount() {
//        return 0.9f;
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//}
