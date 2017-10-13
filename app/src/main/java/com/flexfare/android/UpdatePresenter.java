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
import java.util.Map;

/**
 * Created by Blessyn on 8/25/2017.
 */

public class UpdatePresenter {

    UpdateView view;

    public  UpdatePresenter(UpdateView view){
        this.view = view;
    }

    public void sendToServer(String user_id,String name,String phone_number, String car_type, String reg_number, String about, String route, String driver_type){


       Log.i("FlexApp", "ID" + " = " + user_id);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        DataOutputStream dataout = null;

        // Will contain the raw JSON response as a string.
        String result = null;

        try {

            URL url = new URL("http://www.flexfare.org/api/adddriver.php");


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("user_id", user_id);
            params.put("driver_name", name);
           // params.put("car_name", carName);
            params.put("car_name", car_type);
            params.put("car_registration",reg_number);
            params.put("phone_number", phone_number);
            params.put("driver_descr", about);
            params.put("route", route);
            params.put("driver_type", driver_type);

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
            view.showResult(convertedResponse.getMessage());
        } else /*if (convertedResponse.getStatus() == "0")*/ {

            view.showResult(convertedResponse.getMessage());
        }

    }

}
