package com.flexfare.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.flexfare.android.activities.BaseActivity;
import com.flexfare.android.activities.DriverDetail;
import com.flexfare.android.activities.FirstLaunch;
import com.flexfare.android.helper.ConnectionHelper;
import com.flexfare.android.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
@SuppressWarnings("unchecked")
public class MainActivity extends BaseActivity implements LoginValidation {

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;

    @Bind(R.id.login_em)
    EditText etEmail;
    @Bind(R.id.login_pwd)
    EditText etPassword;
    @Bind(R.id.sign_btn)
    Button btGo;
    @Bind(R.id.signUp)
    Button signUp;
    @Bind(R.id.main_screen)
    ScrollView mainScreen;

    LoginDetails loginDetails;
    LoginPresenter presenter;

    List<LoginDetails> loginList = new ArrayList<LoginDetails>();


    /**
     * Firebase Feed
     **/
    private final String TAG = "Login";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    CircleProgressDialog dialog;
    AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getUserLoggedInStatus() == true){
            startActivity(new Intent(MainActivity.this, DriverDetail.class));
        }
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        animationDrawable = (AnimationDrawable) mainScreen.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabase.keepSynced(true);
        dialog = new CircleProgressDialog(this);

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));
        Intent introIntent = new Intent(MainActivity.this, FirstLaunch.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime)
            startActivity(introIntent);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        presenter = new LoginPresenter(this);
    }

    private void initialization() {
    }

    @OnClick({R.id.sign_btn, R.id.signUp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp:
                    startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.sign_btn:
                if( etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    Snackbar.make(findViewById(android.R.id.content), "Incorrect login details", Snackbar.LENGTH_LONG).show();
                }
                else{
                    btGo.setClickable(false);
                    btGo.setText(getResources().getString(R.string.vroom));
                    loginDetails = new LoginDetails(etEmail.getText().toString(),etPassword.getText().toString());
                    loginList.add(loginDetails);
                    presenter.loginUser(loginList);
                }
                break;
        }
    }

    @Override
    public void doRegistratio() {

    }

    @Override
    public void showLoginSuccess(String result) {

        Snackbar.make(btGo, " Successful", Snackbar.LENGTH_SHORT).show();
        saveUserID(result);
        UserIsLoggedIn();
        startActivity(new Intent(MainActivity.this, DriverDetail.class));

    }

    private void saveUserID(String id) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(SharedPrefKeys.USER_ID, id);
        edit.commit();
    }

    @Override
    public void showServerError(String response) {
        btGo.setClickable(true);
        btGo.setText("Go");
        Snackbar.make(btGo, response, Snackbar.LENGTH_LONG).show();
        loginList.clear();
        etEmail.setText("");
        etPassword.setText("");

    }

    public void UserIsLoggedIn() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("User.Is.Logged.In", true);
        editor.commit();
    }

    public Boolean getUserLoggedInStatus() {

        SharedPreferences loginStat = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return loginStat.getBoolean("User.Is.Logged.In", false);

    }
    private void checkConnectivity(){
        if(ConnectionHelper.isConnectedOrConnecting(getApplicationContext())) {
            hideErrorsBar(true);
        }else {
            hideErrorsBar(false);
        }
    }
    /**
    * Feed Login
    * */
//    @OnClick(R.id.sign_btn)
//    public void login(){
//        checkLogin();
//    }
//    private void checkLogin() {
//        String email = etEmail.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//            dialog.setText("Please Wait.....");
//            dialog.showDialog();
//
//            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        checkUserExist();
//                        dialog.dismiss();
//                    } else {
//                       // Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                }
//            });
//        } else {
////            Toast.makeText(this, "You can bury the ear", Toast.LENGTH_SHORT).show();
////            dialog.dismiss();
//        }
//    }
//    public void checkUserExist() {
//        final String uId = mAuth.getCurrentUser().getUid();
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild(uId)) {
//                    Intent iMain = new Intent(MainActivity.this, DriverDetail.class);
//                    iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(iMain);
//                  //  Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
//
//
//                } else {
//                    Intent iMain = new Intent(MainActivity.this, FeedSetting.class);
//                    iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    iMain.putExtra("name", "setting");
//                    iMain.putExtra("image", "setting");
//                    startActivity(iMain);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
