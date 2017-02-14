package com.example.ishita.myprojectapplication;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ishita on 11/19/2016.
 */
public class UpdateNotificationStatus extends AsyncTask<String,Void,String> {
    private String statusField;
    private Context context;
    public List<Friends> allNames = new ArrayList<Friends>();

    URL urlrequest;
    HttpURLConnection conn;
    String status,method;
    public UpdateNotificationStatus(Context context,String status) {
        this.context = context;


    }
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {
        //System.out.println("Welcome");
        try {
            urlrequest= new URL("http://192.168.0.13/connection/requestupdate.php");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) urlrequest.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //System.out.println("url is "+conn+" "+urlrequest);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("username","\'"+ arg0[0].trim()+"\'")
                    .appendQueryParameter("friendusername", "\'"+arg0[1].trim()+"\'")
                    .appendQueryParameter("status", arg0[2]);
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "exception";
        }

        try {
            int response_code = conn.getResponseCode();
            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                System.out.println("********************sql is "+result);
                // Pass data to onPostExecute method
                return (result.toString());
            } else {
                return ("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        } finally {
            conn.disconnect();
        }

    }

    @Override
    protected void onPostExecute(String result){
        System.out.println(result);

        if(result.trim().equalsIgnoreCase("Request Accepted")) {
            this.statusField="Request Accepted";
            System.out.println("status "+statusField);
        }
        else {
            this.statusField="Try Again";
            System.out.println("status "+statusField);
        }

        return;
    }
}

