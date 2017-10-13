package com.flexfare.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.flexfare.android.activities.DriverDetail;
import com.flexfare.android.fonts.Asap;
import com.flexfare.flextools.Prof_Form;
import com.google.gson.Gson;
import com.isapanah.awesomespinner.AwesomeSpinner;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by kodenerd on 8/19/17.
 */

public class ProfUpdate extends AppCompatActivity implements UpdateView{
    final public static int REQUEST_CODE_WRITE_EXT_STORAGE_PERMISSIONS = 321;

    @Bind(R.id.form)
    Prof_Form prof_form;
    UpdatePresenter presenter;

    @Bind(R.id.submit_btn)
    Button submitButton;

    @Bind(R.id.routes)
    Spinner routes;
    @Bind(R.id.dTypes)
    Spinner dType;

    private TextInputLayout firstname;
    private TextInputLayout lastname;
    private TextInputLayout mobile;
    private TextInputLayout car_type;
    private TextInputLayout car_reg_number;
    private TextInputLayout about_driver;
    private TextInputLayout driverType;
    private TextInputLayout states;


    @Bind(R.id.cars)
    AutoCompleteTextView cars;
//    private MaterialBetterSpinner routes;


    FloatingActionButton upload;

    private ImageView imageView;

    String image_name;
    String uploaded_image;
    Asap imgName;
    private Bitmap bitmap;
    Uri mImageCaptureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_form_activity);
        ButterKnife.bind(this);

        firstname = (TextInputLayout) findViewById(R.id.fName);
        lastname = (TextInputLayout) findViewById(R.id.lastName);
        mobile = (TextInputLayout) findViewById(R.id.phone_input);
        car_reg_number  = (TextInputLayout) findViewById(R.id.car_plate);
        about_driver = (TextInputLayout) findViewById(R.id.driver_descr);
        driverType = (TextInputLayout) findViewById(R.id.driver_type);
        car_type = (TextInputLayout) findViewById(R.id.car_type);
        states = (TextInputLayout) findViewById(R.id.states);
        //dType = (Spinner) findViewById(R.id.dTypes);
        imgName = (Asap) findViewById(R.id.prof_det);

        String[] car = getResources().getStringArray(R.array.cars);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, car);
        cars.setAdapter(adapter);
        cars.setThreshold(1);

        String[] driver = getResources().getStringArray(R.array.driver);
        ArrayAdapter<String> driverAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, driver);
        dType.setAdapter(driverAdapter);
        //dType.setThreshold(1);

        String[] route = getResources().getStringArray(R.array.routes);
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, route);
        routes.setAdapter(routeAdapter);
        //routes.setThreshold(1);

        presenter = new UpdatePresenter(this);
        upload = (FloatingActionButton) findViewById(R.id.up_img);

        upload.setVisibility(View.GONE);
        imageView = (ImageView) findViewById(R.id.profile_image);
        imageView.setOnClickListener(v ->  {
            chooseImageFile(v);
        });

        upload.setOnClickListener(v ->  {
            if (isOnline()) {
                image_name = imgName.getText().toString();
                uploaded_image = getStringImage(bitmap);
                ApiTaskUploadImage ApiCall = new ApiTaskUploadImage(ProfUpdate.this);
                ApiCall.execute(uploaded_image, image_name);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Please Connect Internet", Snackbar.LENGTH_LONG).show();
            }
        });

        if (getString(SharedPrefKeys.PROFILE_PIC_PATH, null) != null) {

            File file = new File(getString(SharedPrefKeys.PROFILE_PIC_PATH, null));
            if (file.exists()) {
                Log.d("FlexApp","fetching profile picture");

                imageView.setImageBitmap(BitmapFactory.decodeFile(getString(SharedPrefKeys.PROFILE_PIC_PATH, null)/*, options*/));



            } else {
                Log.d("FlexApp","no previous profile picture saved");
            }

        }

        submitButton.setOnClickListener(v -> {
            String userID = getUserID();

            /**
             * I'm thinking setting errors here isn't really necessary.
             */

            String name = firstname.getEditText().getText() + " " + lastname.getEditText().getText().toString();

            String phone_number = mobile.getEditText().getText().toString();

            String type = car_type.getEditText().getText().toString();

            String drvType = driverType.getEditText().getText().toString();

            String reg_number = car_reg_number.getEditText().getText().toString();

            String about = about_driver.getEditText().getText().toString();

            String myRoute ="abuja-lagos";

            if(name.equals("")|| phone_number.equals("") || type.equals("") || drvType.equals("") || reg_number.equals("") || about.equals("")){
                Snackbar.make(submitButton, "All fields are required", Snackbar.LENGTH_LONG).show();
            }
            else{
                submitButton.setText("Saving Information...");
                submitButton.setClickable(false);
                presenter.sendToServer(userID,name,phone_number,type,reg_number,about,
                        routes.getSelectedItem().toString(), dType.getSelectedItem().toString());

            }
        });
    }
    private void chooseImageFile(View view) {
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
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {

                startCropImageActivity(imageUri);
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // ((ImageView) findViewById(R.id.profile_image)).setImageURI(result.getUri());

                // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());

                Bitmap selectedImageBitmap = null;

                try {

                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                } catch (IOException e) {

                    Log.d("Flexfare","error in converting resultUri Uri to bitmap: " + e.toString());

                    return;

                }
                if (selectedImageBitmap != null) {

                    imageView.setImageBitmap(selectedImageBitmap);

                    saveSelectedGalleryImage(selectedImageBitmap);

                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "crop failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
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
    private class ApiTaskUploadImage extends AsyncTask<String, Void, String> {

        Context context ;
        ProgressDialog progressDialog ;
        String upload_image_url = "http://dttslimited.com/flexfare/upload.php" ;

        ApiTaskUploadImage(Context context){
            this.context = context ;
        }

        @Override
        protected void onPreExecute() {
           /* progressDialog = new ProgressDialog(context) ;
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Image Uploading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(2);
            progressDialog.setCancelable(false);
            progressDialog.show();*/
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            //  return isNetwork(params[0]);
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

            try {
                progressDialog.dismiss();
                JSONObject mainJSONobj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1)) ;
                Log.d("mainJSONobj=====", String.valueOf(mainJSONobj)) ;

                String response = mainJSONobj.getString("response") ;
                Log.d("RESPONSE=====", response) ;

                Toast.makeText(ProfUpdate.this, response, Toast.LENGTH_SHORT) ;
            }
            catch (JSONException e) {
                Log.d("++++JSONException+++++", String.valueOf(e)) ;
            }
        }
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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
                File.separator + "flexfare_profile_picture_"+getUserID()+".jpg");
        FileOutputStream fo;

        try {

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();


           /** save profile pic path in shared Preferences */

            saveString(SharedPrefKeys.PROFILE_PIC_PATH, destination.getPath());
            /*saving image to server*/
            new UploadFileToServer().execute();
            Log.d("FlexFare","selected image saved successfully");
            /*Error fixed*/
        } catch (FileNotFoundException e) {
            Log.d("FlexFare","error in saving profile picture: " + e.toString());
        } catch (IOException e) {
            Log.d("FlexFare","error in saving profile picture: " + e.toString());

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

    @OnClick(R.id.submit_btn)
    public void setSubmitButton() {
        prof_form.validate();
        if (prof_form.isValid()) {
            Log.e(getClass().getSimpleName(), "All values are valid. Ready to submit");
        } else {
            Log.e(getClass().getSimpleName(), "The last input was invalid");
        }

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

    public String getUserID() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preference.getString(SharedPrefKeys.USER_ID, "");
    }

    @Override
    public void showResult(String result) {
        submitButton.setText("Submit");
        submitButton.setClickable(true);
        if(result.equalsIgnoreCase("Success")){
            startActivity((new Intent(ProfUpdate.this, DriverDetail.class)));
        }
        else{
            Snackbar.make(submitButton, result, Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public void onResume() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        trimCache(this);
    }

    @Override
    public void onStart() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onStart();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DriverDetail.class));
//        try {
//            trimCache(getApplicationContext());
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

    private class UploadFileToServer extends AsyncTask<String, String, String> {
        File sourceFile;
        private int totalSize = 0;
        CircleProgressDialog progressDialog ;

        @Override
        protected void onPreExecute() {

            // setting progress bar to zero

            //uploadProgressBar.setVisibility(View.VISIBLE);
            //progressNumber.setVisibility(View.VISIBLE);
            progressDialog = new CircleProgressDialog(ProfUpdate.this) ;
            //progressDialog.setTitle("Please Wait");
            progressDialog.setText("Updating....");
//            progressDialog.setMessage("Image Uploading...");
//            progressDialog.setIndeterminate(true);
//            progressDialog.setProgressStyle(2);
            progressDialog.setCancelable(false);
            progressDialog.setProgressColor(Color.parseColor("#4CAF50"));
            progressDialog.setTextColor(Color.parseColor("#FF9800"));
            progressDialog.showDialog();
            sourceFile = new File(getString(SharedPrefKeys.PROFILE_PIC_PATH,""));
            Log.d("FlexFare",getString(SharedPrefKeys.PROFILE_PIC_PATH,""));
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

          /*  int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
*/
            int progress = 0;

            /*String[] q = filepath.split("/");
            int idx = q.length - 1;*/

            try {
                // File file = new File(filepath);
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL("http://www.flexfare.org/api/fileupload.php");
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
                Toast.makeText(ProfUpdate.this, convertedResponse.getMessage(), Toast.LENGTH_LONG).show();
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
}