package com.example.ishita.myprojectapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

public class RegisterActivity extends AppCompatActivity {
    EditText editfname, editsname, editUsername, editPassword,editmail;
    String fname, sname, uname, userpassword, email;
    public static Activity reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
      //  getLayoutInflater().inflate(R.layout.activity_register,frameLayout);
    //
        reg=this;
        editfname = (EditText) findViewById(R.id.firstname);
      //  System.out.println("fname is "+editfname.getText().toString());
        editsname= (EditText) findViewById(R.id.sname);
        sname=editsname.getText().toString();
        editUsername= (EditText) findViewById(R.id.UserName);

        uname=editUsername.getText().toString();

        editPassword= (EditText) findViewById(R.id.editPassword);
        userpassword=editPassword.getText().toString();
        editmail= (EditText) findViewById(R.id.email);
        email=editmail.getText().toString();
        final Button bRegister = (Button) findViewById(R.id.bRegister);

    }

    public void register(View view){
        sname=editsname.getText().toString();
        fname=editfname.getText().toString();
        uname=editUsername.getText().toString();
        email=editmail.getText().toString();
        userpassword=editPassword.getText().toString();

        if(sname.length()==0||fname.length()==0||uname.length()==0||email.length()==0||userpassword.length()==0){
            Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show();
        }
        else {
            String status = "";
            Register register = new Register(getApplicationContext(), status);

            register.execute("register", uname, userpassword, email,fname,sname);
            System.out.println("STATUS" + status);
            Log.d("STATUS", status);
            //if (status.equals("Register Successful")) {
            Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
            RegisterActivity.this.startActivity(registerIntent);
            finish();
        }
        //}
    }

}
