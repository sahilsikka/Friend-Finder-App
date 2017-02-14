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
 * Created by Ishita on 11/23/2016.
 */
public class Emergency extends AsyncTask<String, Void, String> {
    URL urlemergency;
    private String statusField;
    HttpURLConnection conn;
    private Context context;

    public Emergency(Context context, String statusField) {
        this.context = context;
        this.statusField = statusField;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("$%$%$%$%$% "+result);
        if(result.trim().equalsIgnoreCase("Successful")) {
            this.statusField="Update Successful";
            Toast.makeText(context,"registration is successful",Toast.LENGTH_SHORT).show();
            System.out.println("status "+this.statusField);
        }
        else {
            this.statusField=result;
          //  Toast.makeText(context,"registration is successful",Toast.LENGTH_SHORT).show();
          //  System.out.println("status "+this.statusField);

        }
    }
    public String getStatusField(){
        return this.statusField;
    }
    protected String doInBackground(String...arg0) {
        try {

            urlemergency = new URL("http://192.168.0.13/connection/emergency.php");
            conn = (HttpURLConnection) urlemergency.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Append parameters to URL

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("method",arg0[0])
                    .appendQueryParameter("username",arg0[1])
                    .appendQueryParameter("contact", arg0[2]);
            String query = builder.build().getEncodedQuery();
            //System.out.println("query "+query);
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
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println("Emergency contact result"+result.toString());
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+result.toString()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
