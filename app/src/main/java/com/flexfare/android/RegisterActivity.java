package com.flexfare.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.flexfare.android.activities.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements RegistrationValidation{


    private EditText firstname, lastname, username, password, repeat_password, email, code, mobile;
    private Button signup;

    Registration registration;
    List<Registration> registrationList = new ArrayList<Registration>();

    RegistrationPresenter presenter;

    ProgressDialog pd;
    AnimationDrawable animationDrawable;
    @Bind(R.id.regScreen)
    ScrollView mainScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        animationDrawable = (AnimationDrawable) mainScreen.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        //initialise presenter
        presenter = new RegistrationPresenter(this);

        initialization();
    }

    private void initialization() {

        pd = new ProgressDialog(RegisterActivity.this);
        firstname = (EditText) findViewById(R.id.f_name);
        lastname = (EditText) findViewById(R.id.l_name);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.pwd);
        repeat_password = (EditText) findViewById(R.id.et_repeatpassword);
        email = (EditText) findViewById(R.id.signup_em);
        code = (EditText) findViewById(R.id.c_code);
        mobile = (EditText) findViewById(R.id.et_mobile);

        signup = (Button) findViewById(R.id.signup_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String f_name = firstname.getText().toString().trim();
                String l_name = lastname.getText().toString().trim();
                String user_name = username.getText().toString().trim();
                String p_word = password.getText().toString().trim();
                String user_email = email.getText().toString().trim();
                String verif_code = code.getText().toString().trim();
                String phone_number = mobile.getText().toString().trim();

                presenter.doValidation(f_name, l_name, user_name, p_word, user_email, phone_number, repeat_password.getText().toString().trim());

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public void showEmptyFieldsError() {
        Snackbar.make(findViewById(android.R.id.content), "One or more fields missing", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void doRegistratio() {

       signup.setClickable(false);
        signup.setText(getString(R.string.vroom));

        final Handler handler = new Handler();
        handler.postDelayed(() ->  {
                registration = new Registration(firstname.getText().toString().trim(), lastname.getText().toString().trim(),
                        username.getText().toString().trim(), password.getText().toString().trim(),
                        email.getText().toString().trim(), mobile.getText().toString().trim());
                registrationList.add(registration);

                presenter.registerUser(registrationList);
        },5000);
    }

    @Override
    public void showInvalidEmailError() {
        Snackbar.make(signup, "Invalid Email", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showInvalidPhoneNumberError() {
        Snackbar.make(signup, "Invalid Phone Number", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showRegistrationSuccess() {
        signup.setClickable(true);
        signup.setText("REGISTER");
        Snackbar.make(signup, "Registration Successful. You can login now", Snackbar.LENGTH_LONG).show();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    @Override
    public void showServerError(String response) {
        signup.setClickable(true);
        signup.setText("REGISTER");
        Snackbar.make(signup, response, Snackbar.LENGTH_SHORT).show();
        registrationList.clear();
         firstname.setText("");
        lastname.setText("");
        email.setText("");
        password.setText("");
        repeat_password.setText("");
        mobile.setText("");
    }

    @Override
    public void showPasswordMismatch() {
        Snackbar.make(signup, "Passwords do not match", Snackbar.LENGTH_SHORT).show();
    }
}
