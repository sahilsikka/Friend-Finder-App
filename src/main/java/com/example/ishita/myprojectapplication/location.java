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
 * Created by Ishita on 11/14/2016.
 */public class location extends AsyncTask<String, Void, String> {
URL urlloc;
    private String statusField;
    HttpURLConnection conn;
    private Context context;

    public location(Context context, String statusField) {
        this.context = context;
        this.statusField = statusField;
        //System.out.println("Welcome");
    }

    @Override
    protected void onPostExecute(String result) {
        if(result==null){
            Intent in=new Intent(context,MainActivity.class);
            in.putExtra("STATUS","Try again later");
            context.startActivity(in);
            IndexActivity.index.finish();
        }
        if(result.trim().equalsIgnoreCase("Successful")) {

            this.statusField="Update Successful";

            Toast.makeText(context,"registration is successful",Toast.LENGTH_SHORT).show();

            System.out.println("status "+this.statusField);



        }


    }


    protected String doInBackground(String...arg0) {
        try {


            urlloc = new URL("http://192.168.0.13/connection/location.php");
            SharedPreferences sharedPreferences=context.getSharedPreferences("mypref",context.MODE_PRIVATE);

            arg0[0]=sharedPreferences.getString("username","");

            conn = (HttpURLConnection) urlloc.openConnection();
           // System.out.println("name is"+ arg0[1]);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String g="\'"+arg0[0].toString()+"\'";
            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("userName",g)
                    .appendQueryParameter("curr_lat", new StringBuffer(arg0[1]).toString())
                    .appendQueryParameter("curr_long", new StringBuffer(arg0[2]).toString());
            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            try {
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
