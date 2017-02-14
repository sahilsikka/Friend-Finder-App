package com.example.ishita.myprojectapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Ishita on 11/18/2016.
 */
public class UpdatePassword extends AsyncTask<String, Void, String> {

    URL urlloc;
    HttpURLConnection conn;
    Context context;
    String statusField;
    public UpdatePassword(Context context, String statusField) {
        this.context = context;
        this.statusField = statusField;
        //System.out.println("Welcome");
    }

    @Override
    protected void onPostExecute(String result) {

        if(result.trim().equalsIgnoreCase("password updated")) {

            this.statusField = "Update Successful";

            Toast.makeText(context, "Password Updated", Toast.LENGTH_SHORT).show();

            System.out.println("status " + this.statusField);

            Intent i=new Intent(context,IndexActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);


        }
            else if (result.trim().equalsIgnoreCase("old password is incorrect")) {
            Toast.makeText(context, "old password is incorrect", Toast.LENGTH_SHORT).show();
            }
        else
        {
            Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {

           // String ip=Integer.toString(R.string.ipaddress);
            urlloc = new URL("http://192.168.0.13/connection/changepassword.php");
            System.out.println("url is "+urlloc);
            SharedPreferences sharedPreferences=context.getSharedPreferences("mypref",context.MODE_PRIVATE);

            arg0[0]=sharedPreferences.getString("username","");

            conn = (HttpURLConnection) urlloc.openConnection();
            // System.out.println("url is "+conn+" "+urlloc);
            // System.out.println("name is"+ arg0[1]);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);
            System.out.println("\" b \" ");
            String g="\'"+arg0[0].toString()+"\'";
            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("userName","\'"+arg0[0].toString()+"\'")
                    .appendQueryParameter("oldp", "\'"+arg0[1].toString()+"\'")
                    .appendQueryParameter("newp", "\'"+arg0[2].toString()+"\'");
            String query = builder.build().getEncodedQuery();
            System.out.println("query "+query);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

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


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception";
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
