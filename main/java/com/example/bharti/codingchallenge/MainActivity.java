package com.example.bharti.codingchallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText pass;
    EditText username;
    private String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void login(View view) {
        pass = (EditText)findViewById(R.id.editText2);
        username = (EditText)findViewById(R.id.editText);
        String userName = username.getText().toString();
        Password = pass.getText().toString();
        final String PASSWORD_PATTERN= "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{7,100}$";
        Pattern p = Pattern.compile(PASSWORD_PATTERN);
        Matcher m = p.matcher(Password);

        if(userName.length() == 0)
            Toast.makeText(getApplicationContext(), "Username cannot be empty",Toast.LENGTH_SHORT).show();



        else if(m.matches())
        {
            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FirstPage.class));
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
        }
    }
}
