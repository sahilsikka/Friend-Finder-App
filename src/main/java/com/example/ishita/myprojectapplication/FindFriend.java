package com.example.ishita.myprojectapplication;

/**
 * Created by Ishita on 11/21/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
public class FindFriend  extends AsyncTask<String,Void,String> {
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

    FindFriend(String method, Context context, String statusField, ArrayList<Friends> friends, GoogleMap mMap,LatLng latlng) {
        this.context = context;
        this.method=method;
        this.latLng=latlng;
        this.statusField = statusField;
        this.allNames=friends;
        this.mMap=mMap;
        //System.out.println("Welcome");
        try {
            urllocations= new URL("http://192.168.0.13/connection/findfriends.php");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {
        if(method.equals("findlocations")) {
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) urllocations.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(40000);
                conn.setRequestMethod("POST");
                System.out.println("Finding locations"+ arg0[0]);
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("username", arg0[0]);
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
                String splitComma[] = result.trim().split("\\$\\$");
                boolean location = false;
                Marker mCollectMarker;
                for (int i = 0; i < splitComma.length; i++) {
                    String info[] = splitComma[i].split("::");
                    if(info.length<3){
                        return;
                    }
                    String username=info[0];

                    Double lat=Double.parseDouble(info[1]);
                    Double longit=Double.parseDouble(info[2]);
                    Friends friend = new Friends(username,lat,longit);
                    IndexActivity.allNames.add(friend);
                    LatLng latLng1 = new LatLng(lat, longit);
                    // String dist=CalculationByDistance(latLng,latLng1);
                    float dist[]=new float[1];
                    Float time1,time2;

                    Location.distanceBetween(latLng.latitude,latLng.longitude,latLng1.latitude,latLng1.longitude,dist);
                    dist[0]*=0.000621371;
                    long factor = (long) Math.pow(10, 2);
                    dist[0] = dist[0] * factor;
                    long tmp = Math.round(dist[0]);
                    dist[0]= (float) tmp / factor;
                    time1=(dist[0]/40)*60*factor;
                    time2=dist[0]*20;
                    time2=time2*factor;
                    tmp=Math.round(time2);
                    time2=(float) tmp / factor;
                    tmp=Math.round(time1);
                    time1=(float) tmp / factor;
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng1)
                            .title(username)
                            .snippet("Distance:"+dist[0]+"mi, walk: "+time2+" min,drive: "+time1+"min");
                    mCollectMarker = mMap.addMarker(markerOptions);
                    if(dist[0]<1) {
                      /*  mCollectMarker.hideInfoWindow();
                        NotificationCompat.Builder mBuilder =
                                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.notificationicon)
                                        .setContentTitle("Buddy Locator")
                                        .setContentText(username + " is near you!");
                        mBuilder.build();*/
                        Toast.makeText(context,username+" is near you ",Toast.LENGTH_SHORT).show();
                    }
            }

        }
        return;
    }
    public String CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        String dist= kmInDec+" KM";
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return dist;
    }
}

