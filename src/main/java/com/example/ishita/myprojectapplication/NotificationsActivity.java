package com.example.ishita.myprojectapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {
    ArrayList<String> array_list;
    ArrayAdapter arrayAdapter;
    ListView obj;
    SharedPreferences pref;
    public static Activity notifact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        final Context context = getApplicationContext();
        obj=(ListView)findViewById(R.id.listView);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        String status="";
        notifact=this;
        //String user=
        pref = this.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        final String user=pref.getString("username",null);
        array_list=new ArrayList<>();
        //loading all available surveys
        if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting()){// Loading from server
            System.out.println("Loading from server for : "+user);
            new Notification(this, obj, arrayAdapter, R.id.listView, status).execute(user);
            int i=0;
            while(i<obj.getChildCount()) {
                array_list.add((String) obj.getItemAtPosition(i));
            }
        }
        else
        {
            Toast toast = Toast.makeText(this, "No Network Found", Toast.LENGTH_SHORT);
            toast.show();
        }
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String message=(String) obj.getItemAtPosition(position);

                final String status="";

                if(message.trim().equals("No Notifications"))return;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NotificationsActivity.notifact);
                alertDialogBuilder.setMessage("Do you want to give location access to "+message);
                alertDialogBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String accept="2";
                        UpdateNotificationStatus passchange = new UpdateNotificationStatus(getApplicationContext(),status);
                        passchange.execute(user,message,accept);
                        Intent next=new Intent(getBaseContext(),NotificationsActivity.class);
                        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(next);
                    }
                });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String decline="3";
                        UpdateNotificationStatus passchange = new UpdateNotificationStatus(getApplicationContext(),status);
                        passchange.execute(user,message,decline);
                        Intent next=new Intent(getBaseContext(),NotificationsActivity.class);
                        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(next);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}
class Notification extends AsyncTask<String, Void,String> {
    String status;
    private Context context;
    ListView surveys;
    String surid,username;
    ArrayAdapter arrayAdapter;
    String download="no";
    ArrayList<String> array_list;
    int id;

    public Notification(Context context, ListView surveys, ArrayAdapter arrayAdapter, int id, String status) {
        this.context=context;
        this.surveys=surveys;
        this.status = status;
        this.arrayAdapter=arrayAdapter;
        this.id=id;
    }
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected String doInBackground(String... arg0) {

        try {
            username = "\'"+arg0[0].trim()+"\'";
            String getnotif= "http://192.168.0.13/connection/getNotification.php";
            System.out.println(" username check123" + arg0[0]);
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            URL url = new URL(getnotif);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("notification is "+sb);
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    protected void onPostExecute(String result) {
        array_list = new ArrayList<>();
        System.out.print(result);
        try {
            if(result.trim().equalsIgnoreCase("No Notifications")){// no survey for that user
                array_list.add(result);
            }
            else {
                String splitComma[] = result.split("\\$\\$");
                System.out.print(splitComma.length);

                for(int i=0;i<splitComma.length-1;i++){
                    array_list.add(i, splitComma[i]);
                    Log.d("request from: ",splitComma[i]);
                }
            }
            if(array_list!=null) {
                arrayAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,array_list);
                surveys.setAdapter(arrayAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }}