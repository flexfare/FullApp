//package com.flexfare.android.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.text.format.DateUtils;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.flexfare.android.FeedMain;
//import com.flexfare.android.R;
//import com.flexfare.android.newsfeed.model.Blog;
//import com.flexfare.android.utils.FormatterUtil;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.TimeZone;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by kodenerd on 9/18/17.
// */
//@SuppressWarnings("VisibleForTests")
//public class PostActivity extends AppCompatActivity {
//
//    private final String TAG = "post";
//    @Bind(R.id.imPost)
//    ImageView imPost;
//    @Bind(R.id.etTitle)
//    EditText etTitle;
//    @Bind(R.id.etDescription)
//    EditText etDescription;
//    @Bind(R.id.imAvatar)
//    ImageView imAvatar;
//
//    private FloatingActionButton btPost;
//    private static final int GALLERY_REQUEST = 1;
//    String image = "";
//    Blog blog;
//
//
//    private Uri imageUri = null;
//
//    private StorageReference mStorage;
//    private DatabaseReference mDatabase;
//    private CircleProgressDialog dialog;
//
//    private FirebaseAuth mAuth;
//    private FirebaseUser mCurrentUser;
//    private DatabaseReference mDBUser;
//
//    public static String firebaseDBDate = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
//    public static String firebaseDBDay = "yyyy-MM-dd";
//    public static final long NOW_TIME_RANGE = DateUtils.MINUTE_IN_MILLIS * 5; // 5 minutes
//    Context context;
//    String dateTime = "yyyy-MM-dd HH:mm:ss";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_alt);
//        ButterKnife.bind(this);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setHomeButtonEnabled(true);
//
//        btPost = (FloatingActionButton) findViewById(R.id.btPost);
//        mStorage = FirebaseStorage.getInstance().getReference();
//
//        mAuth = FirebaseAuth.getInstance();
//        mCurrentUser = mAuth.getCurrentUser();
//
//        mDBUser = FirebaseDatabase.getInstance().getReference().child("User").child(mCurrentUser.getUid());
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
//
//        dialog = new CircleProgressDialog(this);
//
//        image = getIntent().getStringExtra("avatar");
//        Glide.with(getApplicationContext())
//                .load(image)
//                .error(R.drawable.no_image)
//                .into(imAvatar);
//
//
//        btPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startPost();
//            }
//        });
//    }
//
//    @OnClick(R.id.btAddImage)
//    public void addImage() {
//        imPost.setVisibility(View.VISIBLE);
//        Intent iGallery = new Intent(Intent.ACTION_GET_CONTENT);
//        iGallery.setType("image/*");
//        startActivityForResult(iGallery, GALLERY_REQUEST);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
//            imageUri = data.getData();
//            imPost.setImageURI(imageUri);
//        }else {
//            imageUri = Uri.parse("");
//        }
//    }
//    public String getTimeAgo(String dateInput) {
//        String timeformat = dateInput;
//        try {
//            long now = System.currentTimeMillis();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date convertedDate = dateFormat.parse(dateInput);
//            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
//                    convertedDate.getTime(),
//                    now,
//                    DateUtils.SECOND_IN_MILLIS);
//            timeformat = String.valueOf(relavetime1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return timeformat;
//    }
//    private void startPost() {
//
////        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
////        Date date = new Date();
////        final String time = dateFormat.format(date);
//        SimpleDateFormat cbDateFormat = FormatterUtil.getFirebaseDateFormat();
//        cbDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date date = new Date();
//        CharSequence time = FormatterUtil.dateTime;
//        //final String time = cbDateFormat.format(date);
//
//
//        final String title = etTitle.getText().toString().trim();
//        final String description = etDescription.getText().toString().trim();
//
//        if (!TextUtils.isEmpty(description)) {
//            dialog.setText("Posting...");
//            dialog.setProgressColor(Color.parseColor("#4CAF50"));
//            dialog.setTextColor(Color.parseColor("#FF9800"));
//            dialog.showDialog();
//
//            if (imageUri != null) {
//                StorageReference filepath = mStorage.child("Blog_Images").child(imageUri.getLastPathSegment());
//                Log.d(TAG + "--imageUri", imageUri.getLastPathSegment());
//                filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        Log.d(TAG + "--downloadUrl", String.valueOf(downloadUrl));
//
//                        final DatabaseReference newPost = mDatabase.push();
//
//                        mDBUser.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String key = newPost.getKey();
//
//                                newPost.child("key").setValue(key);
//                                newPost.child("title").setValue(title);
//                                newPost.child("description").setValue(description);
//                                if (imageUri != null) {
//                                    newPost.child("image").setValue(downloadUrl.toString());
//                                } else {
//                                    newPost.child("image").setValue("");
//                                }
//                                newPost.child("time").setValue(time);
//                                newPost.child("uid").setValue(mCurrentUser.getUid());
//                                newPost.child("like").setValue(0);
//                                newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            startActivity(new Intent(PostActivity.this, FeedMain.class));
//                                        }
//                                    }
//                                });
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                        dialog.dismiss();
//                        //Toast.makeText(PostActivity.this, "Post bai bar", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(findViewById(android.R.id.content), "Status Updated", Snackbar.LENGTH_LONG).show();
//                    }
//                });
//            } else {
//                final DatabaseReference newPost = mDatabase.push();
//                mDBUser.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        String key = newPost.getKey();
//
//                        newPost.child("key").setValue(key);
//                        newPost.child("title").setValue(title);
//                        newPost.child("description").setValue(description);
//                        newPost.child("time").setValue(time);
//                        newPost.child("uid").setValue(mCurrentUser.getUid());
//                        newPost.child("image").setValue("");
//                        newPost.child("like").setValue(0);
//                        newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    startActivity(new Intent(PostActivity.this, FeedMain.class));
//                                }
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//                dialog.dismiss();
//                Toast.makeText(PostActivity.this, "Post bai bar", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "What's news?", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        }
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//        }
//        return true;
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent =new Intent(this, FeedMain.class);
//        startActivity(intent);
//        finish();
//    }
//}
