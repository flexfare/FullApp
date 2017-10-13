package com.flexfare.android;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Blessyn on 8/16/2017.
 */

public class LoginPresenter {

    LoginValidation logVal;

    public LoginPresenter(LoginValidation logVal) {
        this.logVal = logVal;
    }

    public void loginUser(List<LoginDetails> login) {


        String email = login.get(0).getEmail();
        String p_word = login.get(0).getPassword();


        Log.i("FlexApp", email + "\n" + p_word);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        DataOutputStream dataout = null;

        // Will contain the raw JSON response as a string.
        String result = null;

        try {

            URL url = new URL("http://www.flexfare.org/api/login.php");


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();

            Map<String, Object> params = new LinkedHashMap<>();

            params.put("email", email);
            params.put("password", p_word);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
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
        Log.i("FlexApp", result);

        Gson jsonResponse = new Gson();
        Response convertedResponse = jsonResponse.fromJson(result, Response.class);

        Log.i("FlexApp", convertedResponse.getStatus() + "\n" + convertedResponse.getMessage());

        if (convertedResponse.getStatus() == "1") {
            logVal.showLoginSuccess(convertedResponse.getMessage());
        } else /*if (convertedResponse.getStatus() == "0")*/ {

            logVal.showServerError(convertedResponse.getMessage());
        }

        // regValid.showRegistrationSuccess();
        /*try{

            JSONObject obj = new JSONObject(result);
            String status = obj.getString("status");

            Log.i("FlexApp", "The code :"+ status);
            if(status.equals("1")){
                regValid.showRegistrationSuccess();
                Log.i("FlexApp", "The code :"+ status);
            }

        }catch (JSONException je){
            Log.e("FlexApp")
        }*/

    }
}
