package com.example.ishita.myprojectapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editUsername,editPassword;
    public  static Activity main,mainact;
    public static final String mypreferences="mypref";
    public SharedPreferences sharedPreferences;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        main=this;mainact=this;
        super.onCreate(savedInstanceState);
        Intent  in=getIntent();

        if(in.getStringExtra("STATUS")!=null)
            if (in.getStringExtra("STATUS").equals("Try again later"))
                Toast.makeText(this,"Try Again Later",Toast.LENGTH_SHORT).show();

        sharedPreferences=getSharedPreferences(mypreferences,this.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        editUsername= (EditText) findViewById(R.id.username);
        editPassword= (EditText) findViewById(R.id.password);

        final Button bLogin = (Button) findViewById(R.id.bLogin);

        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);


        registerLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

    }
    public void login(View view){
        String status="";
        Register register = new Register(getApplicationContext(),status);
        register.execute("login",editUsername.getText().toString(),editPassword.getText().toString());

        Log.d("STATUS",status);
        if (status.equals("Login Successful")) {

            Intent loginIntent = new Intent(MainActivity.this, IndexActivity.class);
            loginIntent.putExtra("STATUS","Login succesful");
            startActivity(loginIntent);
            finish();

        }


      /*  Intent in=new Intent(this,IndexActivity.class);
        startActivity(in);
        finish();*/
    }

    public void SignUp(View view){
        Intent in=new Intent(this,RegisterActivity.class);
        startActivity(in);
    }
}
