package com.example.ishita.myprojectapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by rgrac on 11/8/2016.
 */
public class Register extends AsyncTask<String,Void,String> {
    private String statusField;
    private Context context;
    URL urlregister,urllogin;
    HttpURLConnection conn;
    public Register(Context context, String statusField) {
        this.context = context;
        this.statusField = statusField;
        //System.out.println("Welcome");
    }
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {


            urlregister= new URL("http://192.168.0.13/connection/register.php");
            urllogin= new URL("http://192.168.0.13/connection/login.php");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception";
        }
        if (arg0[0].equals("register")) {

            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) urlregister.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", arg0[1])
                        .appendQueryParameter("password", arg0[2])
                        .appendQueryParameter("email", arg0[3])
                        .appendQueryParameter("fname", arg0[4])
                        .appendQueryParameter("sname", arg0[5]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
                System.out.println(" in try");
                int response_code = conn.getResponseCode();
                System.out.println(response_code);
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

                    System.out.println("result query ************* "+result);

                    // Pass data to onPostExecute method
                    return (result.toString());
                } else {
                    Intent in=new Intent(context,MainActivity.class);
                    in.putExtra("STATUS","Try again later");
                    context.startActivity(in);
                    RegisterActivity.reg.finish();
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }
        else if(arg0[0].equals("login")){
            try {

                System.out.println("in login");
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) urllogin.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", arg0[1].toString())
                        .appendQueryParameter("password", arg0[2].toString());
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
                    // Pass data to onPostExecute method
                    SharedPreferences sharedPreferences=context.getSharedPreferences("mypref",context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("username",arg0[1].toString());
                    editor.commit();

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
        return null;
    }



    @Override
    protected void onPostExecute(String result){
        if(result.trim().equalsIgnoreCase("Success")) {
            System.out.println("inside if");
            this.statusField="Register Successful";
            Toast.makeText(context,"registration is successful",Toast.LENGTH_SHORT).show();
            System.out.println("status "+this.statusField);
        }

        else if(result.equalsIgnoreCase("failure")){
            this.statusField="registration failed";
            Toast.makeText(context,"registration is unsuccessful ",Toast.LENGTH_SHORT).show();
        }

        else if(result.equalsIgnoreCase("Login Failed")){
            this.statusField="Login failed";
        }
        else if (result.trim().equalsIgnoreCase("Login Successful")){
            this.statusField="Login Successful";
System.out.println("login succ");
            Intent in=new Intent(context,IndexActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
            MainActivity.main.finish();

        }
        return;
    }
}