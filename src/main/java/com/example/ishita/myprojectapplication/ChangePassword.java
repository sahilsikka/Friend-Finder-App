package com.example.ishita.myprojectapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    public static Activity pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


    }

    public void ChangePassword(View view)
    {
        pass=this;
        String oldp=((EditText)findViewById(R.id.oldp)).getText().toString();
        String newp=((EditText)findViewById(R.id.newp)).getText().toString();
        String rp=((EditText)findViewById(R.id.rp)).getText().toString();
        if(!newp.equals(rp))
        {
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        }

        UpdatePassword u=new UpdatePassword(this,"");
        u.execute("",oldp,newp);
    }

}
