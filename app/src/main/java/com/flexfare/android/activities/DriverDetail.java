package com.flexfare.android.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flexfare.android.ProfUpdate;
import com.flexfare.android.R;
import com.flexfare.android.Response;
import com.flexfare.android.SharedPrefKeys;
import com.flexfare.android.helper.ConnectionHelper;
import com.flexfare.android.helper.FloatingNav;
import com.flexfare.android.model.ServerResponse;
import com.flexfare.android.social.activities.MainActivity;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
//import immortalz.me.library.TransitionsHeleper;
//import immortalz.me.library.bean.InfoBean;
//import immortalz.me.library.method.ColorShowMethod;

/**
 * Created by kodenerd on 8/7/17.
 */

public class DriverDetail extends BaseActivity {

    final public static int REQUEST_CODE_WRITE_EXT_STORAGE_PERMISSIONS = 321;
    @Bind(R.id.carUpdate)
    FloatingActionButton carUpdate;
    @Bind(R.id.plate)
    TextView carPlate;
    @Bind(R.id.carType)
    TextView driver_Type;
    @Bind(R.id.driRoute)
    TextView driRoute;

    TextView headerName, headerPlate;
    View header;
    ImageView headerImg;

    private Bitmap bitmap;
    private Uri mImageCaptureUri;
    private CircleProgressDialog dialog;
    private FloatingNav mFloatingNavigationView;

    ImageView imageView, carImage;
    Button updateProfile;
    ActionMenuItemView editHeader;
    TextView tv_driver_name, tv_driver_mobile, tv_car_reg_number, tv_driver_description, username, category, txtReg;
    String image_name;
    String uploaded_image;

    Snackbar network, refresh;

    @Override
    public void onResume() {
        super.onResume();
//        trimCache(this);
    }
    @Override
    public void onStart(){
        super.onStart();
//        try {
//            trimCache(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public static void trimCache(Context context) {
        try {
            File dir = context.getExternalCacheDir();
            if (dir != null && dir.isDirectory()){
                deleteDir(dir);
            }
        } catch (Exception e) {

        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] child = dir.list();
            for (int i = 0;
                 i < child.length;
                 i++) {
                boolean success = deleteDir(new File(dir, child[i]));
                if (success){
                    return false;
                }
            }
        } return dir.delete();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen);
        ButterKnife.bind(this);

//        trimCache(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_human_greeting);
        toolbar.inflateMenu(R.menu.menu_detail);
        editHeader = (ActionMenuItemView) findViewById(R.id.edit_header);


        mFloatingNavigationView = (FloatingNav) findViewById(R.id.floating_navigation_view);
        header = mFloatingNavigationView.getHeaderView(0);
        headerName = (TextView) header.findViewById(R.id.userName);
        headerPlate = (TextView) header.findViewById(R.id.txtReg);
        headerImg = (ImageView) header.findViewById(R.id.headerImg);

        mFloatingNavigationView.setOnClickListener(v -> {
            mFloatingNavigationView.open();
        });

        mFloatingNavigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.news_feed) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (id == R.id.driver_detail) {
                startActivity(new Intent(this, DriverDetail.class));
            } else if (id == R.id.point_me) {
                startActivity(new Intent(this, SpotMe.class));
            } else if (id == R.id.findRoute) {
                startActivity(new Intent(this, MapsFinder.class));
            } else if (id == R.id.updateProf) {
                startActivity(new Intent(this, ProfUpdate.class));
            } else if (id == R.id.logout) {
                DriverDetail.this.finish();
            }
            mFloatingNavigationView.close();
            return true;
        });

        dialog = new CircleProgressDialog(this);
        imageView = (ImageView) findViewById(R.id.detailAvatar);
        tv_driver_name = (TextView) findViewById(R.id.dName);
        tv_driver_mobile = (TextView) findViewById(R.id.mobile);
        tv_car_reg_number = (TextView) findViewById(R.id.plate);
        category = (TextView) findViewById(R.id.car);
        tv_driver_description = (TextView) findViewById(R.id.detail);
        username = (TextView) findViewById(R.id.userName);
        carImage = (ImageView) findViewById(R.id.carImage);
        updateProfile = (Button) findViewById(R.id.verify_btn);
        updateProfile.setVisibility(View.GONE);

        editHeader.setOnClickListener(v ->  {
            try {
                chooseImageFile(v);
            }catch (Exception e){
                Log.e("FlexFare",e.toString());
            }
        });
        carUpdate.setOnClickListener(v -> {
            if (isOnline()) {
                image_name = carPlate.getText().toString();
                uploaded_image = getStringImage(bitmap);
                DriverDetail.ApiTaskUploadImage ApiCall = new DriverDetail.ApiTaskUploadImage(DriverDetail.this);
                ApiCall.execute(uploaded_image, image_name);
                carUpdate.setVisibility(View.GONE);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "No Internet to perform upload", Snackbar.LENGTH_LONG).show();
            }
        });

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
//        }

        updateProfile.setOnClickListener(v -> {
            startActivity(new Intent(DriverDetail.this, ProfUpdate.class));
        });
//        if (getPathString() != null) {
//
//            File file = new File(getPathString());
//            if (file.exists()) {
//                Log.d("FlexApp","fetching profile picture");
//                imageView.setImageBitmap(BitmapFactory.decodeFile(getPathString()/*, options*/));
//            } else {
//                Log.d("FlexApp","no previous profile picture saved");
//            }
//
//        }
//
//        if (getCarPathString() != null) {
//
//            File file = new File(getPathString());
//            if (file.exists()) {
//                Log.d("FlexApp","fetching profile picture");
//                carImage.setImageBitmap(BitmapFactory.decodeFile(getCarPathString()/*, options*/));
//            } else {
//                Log.d("FlexApp","no previous profile picture saved");
//            }
//
//        }
        Log.d("FlexApp", "Calling Async task to load driver details");
        if (isOnline()) {
            try {
                loadData();
            } catch (Exception e) {
            }
        } else {
            network = Snackbar.make(findViewById(R.id.carImage),
                    "Failed to load Profile. Please connect your Internet and refresh", Snackbar.LENGTH_INDEFINITE);
            network.setAction("refresh", v ->  {
                if (isOnline()) {
                    try {
                    loadData();
                        refresh.show();
                    }
                catch (Exception e) {}
                }
                else { refresh = Snackbar.make(findViewById(android.R.id.content), "No Internet detected. Failed to load Profile", Snackbar.LENGTH_INDEFINITE);
                    refresh.setAction("refresh", v1 -> {
                        if (isOnline()) {
                            loadData();
                        }
                    });
                }
                refresh.show();
            });
            network.show();
        }
    }
    public void loadData() {
        new DriverAsyncLoader().execute();
    }

    private void chooseImageFile(View v)
    {
        if(hasReadExternalStoragePermissions()) {
            CropImage.startPickImageActivity(this);
        }else{
            requestReadExternalStoragePermission();
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);



            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {

                mImageCaptureUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {

                startCropImageActivity(imageUri);
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //((ImageView) findViewById(R.id.carImage)).setImageURI(result.getUri());
                carUpdate.setVisibility(View.GONE);
                Bitmap selectedImageBitmap = null;

                try {

                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                } catch (IOException e) {

                    Log.d("Flexfare","error in converting resultUri Uri to bitmap: " + e.toString());

                    return;

                }
                if (selectedImageBitmap != null) {

                    carImage.setImageBitmap(selectedImageBitmap);

                    saveSelectedGalleryImage(selectedImageBitmap);

                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Toast.makeText(this, "crop failed: " + result.getError(), Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(android.R.id.content), "crop failed: " + result.getError(), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void saveSelectedGalleryImage(Bitmap selectedImage) {

        //create directory
        File mediaStorageDir = null;

        if (createProfilePicDirectory() == null) {

            return;

        } else {

            mediaStorageDir = createProfilePicDirectory();

        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        File destination = new File(mediaStorageDir.getPath(),
                File.separator + "flexfare_car_image_"+getUserID()+".jpg");
        FileOutputStream fo;

        try {

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

            saveString(SharedPrefKeys.CAR_IMAGE_PATH, destination.getPath());
            /*saving image to server*/
            try {
                new UploadFileToServer().execute();
            }catch(Exception e){
                Log.e("FlexFare",e.toString());
            }
            Log.d("FlexFare","selected image saved successfully");
            /*Error fixed*/
        } catch (FileNotFoundException e) {
            Log.d("FlexFare","error in saving profile picture: " + e.toString());
        } catch (IOException e) {
            Log.d("FlexFare","error in saving profile picture: " + e.toString());

        }
    }

    public static File createProfilePicDirectory() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "FlexFare");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("FlexApp","Oops! Failure in Creating "
                        + "FlexFare" + " directory");

                return null;

            }
        }

        return mediaStorageDir;

    }

    public void saveString(String key, @Nullable String text) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, text);
        editor.commit();

    }

    public String getString(String key, @Nullable String defaultValue) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preference.getString(key, defaultValue);
    }
    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] imagebyte = byteArrayOutputStream.toByteArray();
        String encodeString = Base64.encodeToString(imagebyte, Base64.DEFAULT);
        return encodeString ;
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

//    @OnClick(R.id.detailAvatar)
//    public void onClick() {
//        TransitionsHeleper.startActivity(this, AvatarActivity.class, imageView);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        trimCache(this);
//        try {
//            trimCache(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
//    public static void trimCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            if (dir != null && dir.isDirectory()){
//                deleteDir(dir);
//            }
//        } catch (Exception e) {
//
//        }
//    }
//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] child = dir.list();
//            for (int i = 0;
//                 i < child.length;
//                 i++) {
//                boolean success = deleteDir(new File(dir, child[i]));
//                if (success){
//                    return false;
//                }
//            }
//        } return dir.delete();
//    }
    @Override
    public void onBackPressed(){
        if (mFloatingNavigationView.isOpened()) {
            mFloatingNavigationView.close();
        } else {
            //super.onBackPressed();
            ActivityCompat.finishAffinity(this);
        }
    }

    public String getUserID() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preference.getString(SharedPrefKeys.USER_ID, "");
    }

//    public String getPathString() {
//        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        return preference.getString(SharedPrefKeys.PROFILE_PIC_PATH, "");
//    }
//    public String getCarPathString() {
//        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        return preference.getString(SharedPrefKeys.CAR_IMAGE_PATH, "");
//    }

    private class ApiTaskUploadImage extends AsyncTask<String, Void, String> {

        Context context ;
        CircleProgressDialog progressDialog ;
        String upload_image_url = "http://dttslimited.com/flexfare/upload.php" ;

        ApiTaskUploadImage(Context context){
            this.context = context ;
        }


        @Override
        protected void onPreExecute() {
                progressDialog = new CircleProgressDialog(context);
                progressDialog.setText("Updating...");
                progressDialog.setProgressColor(Color.parseColor("#4CAF50"));
                progressDialog.setTextColor(Color.parseColor("#FF9800"));
                progressDialog.setCancelable(false);
                progressDialog.showDialog();
                super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = null;

            try {
                URL url = new URL(upload_image_url) ;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream() ;
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8")) ;

                String image = params[0];
                String name = params[1];
                String data = URLEncoder.encode("image", "UTF-8")+"="+URLEncoder.encode(image, "UTF-8")+"&"+
                        URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8") ;

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                int responsecode = httpURLConnection.getResponseCode() ;
                if(responsecode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream() ;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")) ;
                    stringBuilder = new StringBuilder() ;
                    String line = "" ;
                    while((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line + "\n") ;
                    }
                }
                httpURLConnection.disconnect();

            }
            catch (MalformedURLException e) {
                Log.d("++++Malformed+++++", String.valueOf(e)) ;
            }
            catch (IOException e) {
                Log.d("++++IOException+++++", String.valueOf(e)) ;
            }

            return stringBuilder.toString().trim();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String json) {
            if (isOnline()) {
            try {
                progressDialog.dismiss();
                JSONObject mainJSONobj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1)) ;
                Log.d("mainJSONobj=====", String.valueOf(mainJSONobj)) ;

                String response = mainJSONobj.getString("response") ;
                Log.d("RESPONSE=====", response) ;

                Toast.makeText(DriverDetail.this, response, Toast.LENGTH_SHORT) ;
            }
            catch (JSONException e) {
                Log.d("++++JSONException+++++", String.valueOf(e)) ;
            }
            } else {
                Snackbar.make(findViewById(android.R.id.content), "No Internet to perform upload", Snackbar.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onRestart() {
        super.onRestart();

    }
    private class DriverAsyncLoader extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            checkConnectivity();
            super.onPreExecute();
            dialog.showDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            checkConnectivity();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            DataOutputStream dataout = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {

                URL url = new URL("http://www.flexfare.org/api/driverdetails.php");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.connect();
                Log.d("FlexApp", "Connected to server");

                Map<String, Object> parameters = new LinkedHashMap<>();
                parameters.put("id", getUserID());


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : parameters.entrySet()) {
                    if (postData.length() != 0) {
                        postData.append('&');
                    }
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();

                dataout = new DataOutputStream(urlConnection.getOutputStream());
                // perform POST operation
                dataout.writeBytes(urlParameters);

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Log.i("FlexApp", "Buffer Empty");
                }
                result = buffer.toString();
                Log.d("FlexApp", "getting result");

            } catch (Exception e) {
                Log.e("FlexApp", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FlexApp", "Error closing stream", e);
                    }
                }
            }
            Log.i("FlexApp", result.trim());


            return result;
        }

        @Override
        protected void onPostExecute(String serverResponse) {
            checkConnectivity();
            super.onPostExecute(serverResponse);
            dialog.dismiss();

            if(serverResponse.trim().equals("null")){
                Log.i("FlexApp", "response empty");
                //Toast.makeText(DriverDetail.this, "")
                updateProfile.setVisibility(View.VISIBLE);
            }
            else {
                Gson jsonResponse = new Gson();
                ServerResponse response = jsonResponse.fromJson(serverResponse, ServerResponse.class);
                Log.d("FlexApp", response.toString());
                Log.d("FlexApp", "Initializing activity with response");
                tv_driver_name.setText(response.getMessage().getName());
                tv_driver_mobile.setText(response.getMessage().getDriver_number());
                tv_car_reg_number.setText(response.getMessage().getRegistration_number());
                tv_driver_description.setText(response.getMessage().getAbout());
                driver_Type.setText(response.getMessage().getDriver_type());
                category.setText(response.getMessage().getCar_type());
                headerName.setText(response.getMessage().getName());
                headerPlate.setText(response.getMessage().getRegistration_number());
                driRoute.setText(response.getMessage().getRoute());

                String driverImage = response.getMessage().getDriverImg();
                String carImg = response.getMessage().getCarImg();
                String headerAvi = response.getMessage().getDriverImg();

                try {
                    Picasso.with(DriverDetail.this).load("http://www.flexfare.org/api/images/" + driverImage).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageView);
                    Picasso.with(DriverDetail.this).load("http://www.flexfare.org/api/images/" + headerAvi).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(headerImg);
                    Picasso.with(DriverDetail.this).load("http://www.flexfare.org/api/images/" + carImg).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(carImage);
                } catch (Exception e){
                }
            }
        }
    }

    private void checkConnectivity(){
        if(ConnectionHelper.isConnectedOrConnecting(getApplicationContext())) {
            hideErrorsBar(true);
        }else {
            hideErrorsBar(false);
        }
    }

    private class UploadFileToServer extends AsyncTask<String, String, String> {
        File sourceFile;
        private int totalSize = 0;
        CircleProgressDialog progressDialog ;

        @Override
        protected void onPreExecute() {

            progressDialog = new CircleProgressDialog(DriverDetail.this) ;
            progressDialog.setText("Updating....");
            progressDialog.setProgressColor(Color.parseColor("#4CAF50"));
            progressDialog.setTextColor(Color.parseColor("#FF9800"));
            progressDialog.setCancelable(false);
            progressDialog.showDialog();
            sourceFile = new File(getString(SharedPrefKeys.CAR_IMAGE_PATH,""));
            Log.d("FlexFare",getString(SharedPrefKeys.CAR_IMAGE_PATH,""));
            totalSize = (int) sourceFile.length();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String responseString = null;

            HttpURLConnection connection = null;
            String fileName = sourceFile.getName();
            DataOutputStream outputStream = null;
            InputStream inputStream = null;

            String twoHyphens = "--";
            String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
            String lineEnd = "\r\n";

            String result = "";

            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL("http://www.flexfare.org/api/carimageupload.php");
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "image" + "\"; filename=\"" + fileName + "\"" + lineEnd);
                //outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
                outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

                outputStream.writeBytes(lineEnd);

                //bytesAvailable = fileInputStream.available();
                //bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //buffer = new byte[bufferSize];

                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    outputStream.write(buf, 0, bytesRead);
                    // outputStream.flush();
                  /*  progress += bytesRead; // Here progress is total uploaded bytes
                    publishProgress(""+(int)((progress*100)/totalSize)); // sending progress percent to publishProgress
                    Thread.sleep(1000);*/
                }

                outputStream.writeBytes(lineEnd);

                // Upload POST Data


                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(getUserID());
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                if (200 != connection.getResponseCode()) {
                    throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
                }

               /* inputStream = connection.getInputStream();
                result = this.convertStreamToString(inputStream);*/
                Log.d("FlexFare","Response code: " + connection.getResponseCode());
                // AppUtil.debug(result);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                responseString = builder.toString();

                fileInputStream.close();
                inputStream.close();
                outputStream.flush();
                outputStream.close();

                return result;
            } catch (Exception e) {
                Log.e("FlexFare",e.toString());
                //throw new Exception(e);
            }

            return responseString;


        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {

                Log.d("FlexFare", "Response from server: " + result);

                // showing the server response in an alert dialog
                progressDialog.dismiss();
                Gson jsonResponse = new Gson();
                Response convertedResponse = jsonResponse.fromJson(result, Response.class);

                Log.i("FlexFare", convertedResponse.getStatus() + "\n" + convertedResponse.getMessage());

                // if (convertedResponse.getStatus() == "1") {
                Toast.makeText(DriverDetail.this, convertedResponse.getMessage(), Toast.LENGTH_LONG).show();
                // } else /*if (convertedResponse.getStatus() == "0")*/ {


                // }
                super.onPostExecute(result);
            }
            /* }else {
                //appUtil.toastShort("Upload Error: please try again");
                appUtil.toastShort(result);
            }*/
        }
    }

    public boolean hasReadExternalStoragePermissions() {
        Log.d("FlexFare","checking External permissions are present...");
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            int ReadExtPermission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (ReadExtPermission != PackageManager.PERMISSION_GRANTED) {

                Log.d("FlexFare","ReadExternalStoragePermissions is absent");

                return false;

            }
            Log.d("FlexFare","ReadExternalStoragePermissions is present...");
            return true;

        }
        Log.d("FlexFare","ReadExternalStoragePermissions is present...");

        return true;
    }
    @TargetApi(23)
    public void requestReadExternalStoragePermission() {

        Log.d("FlexFare","about to check if relevant permission are present");
        int hasReadExternalStoragePermission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> permissions = new ArrayList<>();

        if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            Log.d("FlexFare","WRITE_EXTERNAL_STORAGE permission is not present");

            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

        if (!permissions.isEmpty()) {
            Log.d("FlexFare","permission request presented to user");
            this.requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_WRITE_EXT_STORAGE_PERMISSIONS);

        }
    }
}