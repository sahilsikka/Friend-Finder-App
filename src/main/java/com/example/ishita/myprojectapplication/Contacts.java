package com.example.ishita.myprojectapplication;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import  android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;


public class Contacts extends AppCompatActivity {
    private final int PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //callContacts(R.layout.activity_contacts);
        //callContacts();
        // onActivityResult();

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
       /* int reqCode=PICK_CONTACT,resultCode=AppCompatActivity.RESULT_OK;

        if (reqCode == PICK_CONTACT) {
            System.out.println("contacts" + reqCode);
            if (resultCode == AppCompatActivity.RESULT_OK) {
                System.out.println("contactsssss");
                Uri contactData = intent.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);

                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    Toast.makeText(this, "You've picked:" + name, Toast.LENGTH_LONG).show();
                }
            }


        }*/
    }
    private void pickContact(){
        Intent pickcontact=new Intent(Intent.ACTION_PICK,Uri.parse("content://contacts"));
        pickcontact.setType(Phone.CONTENT_TYPE);
        startActivityForResult(pickcontact,PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(reqCode, resultCode, data);
        grantUriPermission();
        if (reqCode == PICK_CONTACT){
            System.out.println("contacts "+reqCode);
            if (resultCode == AppCompatActivity.RESULT_OK) {

                Uri contactData = data.getData();
//                String[] projection={Phone.NUMBER};
//                Cursor c = getContentResolver().query(contactData,projection, null,null,null);
//                c.moveToFirst();
//                int col=c.getColumnIndex(Phone.NUMBER);
//                String number=c.getString(col);
//                Log.d("NUMBER",number);
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:"+number));
//                startActivity(intent);
                SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
                String user = sharedPreferences.getString("username", "");

                String number="";
                Emergency e=new Emergency(getApplicationContext(),number);
                e.execute("call",user,"");

            }
        }

    }
    public static final int MY_PERMISSIONS_REQUEST_CONTACT = 99;

    private void grantUriPermission() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_CONTACT);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_CONTACT);
            }
            return;
        } else {
            return;
        }
    }

}
