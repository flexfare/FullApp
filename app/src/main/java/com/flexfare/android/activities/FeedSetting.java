//package com.flexfare.android.activities;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.MenuItem;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.flexfare.android.FeedMain;
//import com.flexfare.android.R;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by kodenerd on 9/18/17.
// */
//
//public class FeedSetting extends AppCompatActivity {
//
//    @Bind(R.id.imAvatar)
//    ImageView imAvatar;
//    @Bind(R.id.etName)
//    EditText etName;
//    private static final int GALLERY_REQUEST = 1;
//    private Uri imageUri = null;
//
//    private DatabaseReference mDatabaseUser;
//    private FirebaseAuth mAuth;
//    private StorageReference mStorage;
//    private ProgressDialog dialog;
//    private String name = "";
//    private String image = "";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.feed_setting);
//        ButterKnife.bind(this);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setHomeButtonEnabled(true);
//
//        name = getIntent().getStringExtra("name");
//        image = getIntent().getStringExtra("image");
//        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("User");
//        mAuth = FirebaseAuth.getInstance();
//
//        mStorage = FirebaseStorage.getInstance().getReference().child("Profile_image");
//        dialog = new ProgressDialog(this);
//
//        if (!name.equals("setting")){
//            etName.setText(name);
//        }
//        if (!image.equals("setting")){
//            Glide.with(getApplicationContext())
//                    .load(image)
//                    .error(R.drawable.no_image)
//                    .into(imAvatar);
//        }
//        if (image.equals("")) {
//            Snackbar.make(findViewById(android.R.id.content), "Upload image", Snackbar.LENGTH_LONG);
//        }
//    }
//
//    @OnClick(R.id.imAvatar)
//    public void getImage(){
//        Intent gallerIntent = new Intent();
//        gallerIntent.setAction(Intent.ACTION_GET_CONTENT);
//        gallerIntent.setType("image/*");
//        startActivityForResult(gallerIntent, GALLERY_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
//
//            Uri imageUri = data.getData();
//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);
//
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//                imageUri = result.getUri();
//                imAvatar.setImageURI(imageUri);
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//    }
//
//    @OnClick(R.id.btSetting)
//    public void setting(){
//        final String name = etName.getText().toString().trim();
//        final String uID = mAuth.getCurrentUser().getUid();
//        if (!TextUtils.isEmpty(name) && imageUri != null){
//            dialog.setMessage("Setting...");
//            dialog.show();
//
//            StorageReference filepath = mStorage.child(imageUri.getLastPathSegment());
//            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
//
//                    mDatabaseUser.child(uID).child("uId").setValue(uID);
//                    mDatabaseUser.child(uID).child("name").setValue(name);
//                    mDatabaseUser.child(uID).child("image").setValue(downloadUri);
//                    dialog.dismiss();
//
//                    Intent iMain = new Intent(FeedSetting.this, FeedMain.class);
//                    startActivity(iMain);
//                }
//            });
//        }
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home){
//            finish();
//        }
//        return true;
//    }
//}
