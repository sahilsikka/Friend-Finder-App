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
 * Created by Ishita on 11/17/2016.
 */
public class invite extends AsyncTask<String,Void,String> {
    URL urlloc;
    private String statusField;
    HttpURLConnection conn;
    private Context context;

    public invite(Context context) {
        this.context = context;
        this.statusField = statusField;
        //System.out.println("Welcome");
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {


            System.out.println("uname is "+arg0[0]);

            urlloc = new URL("http://192.168.0.13/connection/sendinvite.php");
            conn = (HttpURLConnection) urlloc.openConnection();
            // System.out.println("url is "+conn+" "+urlloc);
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
                    .appendQueryParameter("user_name",arg0[0]);
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

                    System.out.println("result in invite is "+ result);

                    if(result.toString().trim().equalsIgnoreCase("username found"))
                    {

                        urlloc = new URL("http://192.168.0.13/connection/addconnection.php");
                        conn = (HttpURLConnection) urlloc.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        SharedPreferences sharedPreferences=context.getSharedPreferences("mypref",context.MODE_PRIVATE);

                        String uname_from= "\'"+sharedPreferences.getString("username","").toString()+"\'";

                        //String g="\'"+arg0[0].toString()+"\'";
                        // Append parameters to URL
                         builder = new Uri.Builder()
                                .appendQueryParameter("username1",uname_from)
                                 .appendQueryParameter("username2","\'"+arg0[0]+"\'")
                                 .appendQueryParameter("status",Integer.toString(1));
                         query = builder.build().getEncodedQuery();
                        System.out.println("query "+query);
                         os = conn.getOutputStream();
                         writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(query);
                        writer.flush();
                        writer.close();
                        os.close();
                        conn.connect();



                    int response_code1 = conn.getResponseCode();
                    System.out.println(response_code);
                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        // Read data sent from server
                         input = conn.getInputStream();
                         reader = new BufferedReader(new InputStreamReader(input));
                         result = new StringBuilder();


                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                    // Pass data to onPostExecute method
                    return (result.toString());
                } }


            }
                return ("username not found");
            }catch (IOException e) {
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


    @Override
    protected void onPostExecute(String result) {
        System.out.println("result is "+result);

        if(result.trim().equalsIgnoreCase("sent")) {
            this.statusField="Sent Successful";
            Toast.makeText(context,"invite is successful",Toast.LENGTH_SHORT).show();
            System.out.println("status "+this.statusField);
            Intent in=new Intent(context,IndexActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
            IndexActivity.index.finish();

        }

        else if(result.trim().equalsIgnoreCase("username not found")){

            this.statusField="failure";

            Toast.makeText(context,"username not found",Toast.LENGTH_SHORT).show();

            System.out.println("status "+this.statusField);

        }
        else {

            Toast.makeText(context,"Try again Later!!!",Toast.LENGTH_SHORT).show();
        }


    }
}
