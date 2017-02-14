package com.example.ishita.myprojectapplication;

import android.content.Context;
        import android.content.Intent;
        import android.location.Location;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.v7.app.NotificationCompat;
        import android.util.Log;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

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
        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by rgrac on 11/14/2016.
 */
public class ListofFriend  extends AsyncTask<String,Void,String> {
    private String statusField;
    private Context context;
    public List<Friends> allNames = new ArrayList<Friends>();
    String method;
    ListView listv;
    ArrayAdapter arrayAdapter;
    List<String> friendl=new ArrayList<String>();
    URL urllocations,urllist;
    HttpURLConnection conn;
    GoogleMap mMap;
    LatLng latLng;

    ListofFriend(String method, Context context, ListView obj,ArrayAdapter arrayAdapter) {
        this.context = context;
        this.method=method;
        this.arrayAdapter=arrayAdapter;
        this.statusField = statusField;
        this.listv=obj;
        //System.out.println("Welcome");
        try {
            urllist=new URL("http://192.168.0.13/connection/friendslist.php");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {
        if(method.equals("findlist")){
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) urllist.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                System.out.println("Finding friends"+ arg0[0]);

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("username", "\'"+arg0[0]+"\'");
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

                    System.out.println("********************* " +result);
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
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        System.out.print("RESULT###### "+result);
        if(result.trim().equalsIgnoreCase("no locations found")) {
            this.statusField="no locations found";
            System.out.println("status "+statusField);
        }
        else if(result.trim().equalsIgnoreCase("no friends found")) {
            this.statusField="no friends found";
            System.out.println("status "+statusField);
        }
       else {

            System.out.println("Result: "+result.trim());
            String splitComma[] = result.split("\\$\\$");

            for (int i = 0; i < splitComma.length; i++) {
                String name=splitComma[i];
                friendl.add(name);
                System.out.println(name);
            }
            if(friendl!=null) {
                arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, friendl);
                listv.setAdapter(arrayAdapter);
            }


        }
        return;
    }

}

