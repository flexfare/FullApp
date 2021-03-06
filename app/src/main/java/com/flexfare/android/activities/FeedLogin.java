//package com.flexfare.android.activities;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.flexfare.android.FeedMain;
//import com.flexfare.android.R;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by kodenerd on 9/18/17.
// */
//
//public class FeedLogin extends AppCompatActivity {
//
//    @Bind(R.id.login_em)
//    EditText etEmail;
//    @Bind(R.id.login_pwd)
//    EditText etPassword;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    ProgressDialog dialog;
//
//    private final int RC_SIGN_IN = 1;
//    private GoogleApiClient mGoogleApiClient;
//    private final String TAG = "Login";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.news_login);
//        ButterKnife.bind(this);
//        mAuth = FirebaseAuth.getInstance();
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
//
//        mDatabase.keepSynced(true);
//
//        dialog = new ProgressDialog(this);
//
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//    }
//    //-----sign in google---//
//    //@OnClick(R.id.btGoogle)
//    public void signinGoogle(){
//        signIn();
//    }
//    private void signIn() {
//        dialog.setMessage("Start sign up...");
//        dialog.show();
//
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//            if (result.isSuccess()) {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//                dialog.dismiss();
//
//            } else {
//                // Google Sign In failed, update UI appropriately
//                // ...
//                dialog.dismiss();
//            }
//        }
//    }
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(FeedLogin.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }else {
//                            checkUserExist();
//                            dialog.dismiss();
//                        }
//
//                    }
//                });
//    }
//
//
//    //---finish google----//
//
//    @OnClick(R.id.sign_btn)
//    public void login() {
//        checkLogin();
//    }
//
//    private void checkLogin() {
//        String email = etEmail.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//            dialog.setMessage("Login...");
//            dialog.show();
//
//            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        checkUserExist();
//                        dialog.dismiss();
//                    } else {
//                        Toast.makeText(FeedLogin.this, "Login error", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(this, "You can bury the ear", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        }
//    }
//
//    public void checkUserExist() {
//        final String uId = mAuth.getCurrentUser().getUid();
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild(uId)) {
//                    Intent iMain = new Intent(FeedLogin.this, FeedMain.class);
//                    iMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(iMain);
//                    Toast.makeText(FeedLogin.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
//
//
//                } else {
//                    Intent iMain = new Intent(FeedLogin.this, FeedSetting.class);
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
//
////    @OnClick(R.id.signUp)
////    public void register() {
////        Intent iRegister = new Intent(FeedLogin.this, FeedRegister.class);
//////        iRegister.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        startActivity(iRegister);
////    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//}
