package edu.fau.ngamarra2014.sync_care;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    private String params = "";

    // constructor
    public JSONParser() {

    }

    public void setParams(QueryString query){
        params = query.toString();
        Log.d("look", "setParams: " + query);
    }
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method) {
        // Making HTTP request
        try {

            // check for request method
            if(method == "POST"){
//                // request method is POST
                byte[] postData = params.getBytes( StandardCharsets.UTF_8 );
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
                urlConnection.setDoOutput(true);
                //urlConnection.setChunkedStreamingMode(0);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.write(postData);

                is = urlConnection.getInputStream();
                wr.close();
                urlConnection.disconnect();

            }else if(method == "GET"){
                // request method is GET
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(url + "?" + params).openConnection();
                is = urlConnection.getInputStream();
                urlConnection.disconnect();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }

            json = sb.toString();
            is.close();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}
