//package com.flexfare.android.activities;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.flexfare.android.FeedMain;
//import com.flexfare.android.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by kodenerd on 9/18/17.
// */
//
//public class FeedRegister extends AppCompatActivity {
//
//    private FirebaseAuth firebaseAuth;
//    String email, name, password;
//    @Bind(R.id.f_name)
//    EditText etName;
//    @Bind(R.id.signup_em)
//    EditText etEmail;
//    @Bind(R.id.pwd)
//    EditText etPassword;
//    private ProgressDialog dialog;
//    private DatabaseReference mDatabase;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.signup_screen);
//        ButterKnife.bind(this);
//        firebaseAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
//
//        dialog = new ProgressDialog(this);
//    }
//
//    @OnClick(R.id.signup_btn)
//    public void signUp() {
//
//        name = etName.getText().toString();
//        email = etEmail.getText().toString();
//        password = etPassword.getText().toString();
//        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//            dialog.setMessage("Signing Up...");
//            dialog.show();
//
//            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        AuthResult authResult = task.getResult();
//                        Log.d("authResult", String.valueOf(authResult));
//                        String user_id = firebaseAuth.getCurrentUser().getUid();
//                        DatabaseReference current_user_id = mDatabase.child(user_id);
//                        //them nut user_id cac thuoc tinh
//                        current_user_id.child("uId").setValue(user_id);
//                        current_user_id.child("name").setValue(name);
//                        current_user_id.child("image").setValue("default");
//
//                        Intent iMain = new Intent(FeedRegister.this, FeedMain.class);
//                        iMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(iMain);
//                        dialog.dismiss();
//                        Toast.makeText(FeedRegister.this, "Account created successfully", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        dialog.dismiss();
//                        Toast.makeText(FeedRegister.this, "ban big white", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }else {
//            Toast.makeText(this, "You need to fill out all fields", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
