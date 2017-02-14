package com.example.ishita.myprojectapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Friendlist extends AppCompatActivity {
    ListView obj;
    ArrayList array_list;
    ArrayAdapter arrayAdapter;
    public static Activity friendact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        Context context = getApplicationContext();
        obj=(ListView)findViewById(R.id.friendlist);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        String status="";
        friendact=this;
        //String user=
        context = getApplicationContext();
        SharedPreferences sharedPreferences=context.getSharedPreferences("mypref",context.MODE_PRIVATE);
        String user=sharedPreferences.getString("username","");
        System.out.println("FRIENDLIST");
        System.out.println("username"+user);
        array_list=new ArrayList<>();
        //loading all available surveys
        List<String> allNames=new ArrayList<String>();

        if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting()){// Loading from server
            System.out.println("Loading from server for : "+user);
            ListofFriend friends=new ListofFriend("findlist",getBaseContext(),obj,arrayAdapter);
            friends.execute(user);
            int i=0;
            while(i<obj.getChildCount()) {
                array_list.add(allNames.get(i));
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

                if(message.trim().equals("No friends found"))return;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friendlist.friendact);
                alertDialogBuilder.setMessage("Do you want to block "+message);
                alertDialogBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        return;
                    }
                });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}