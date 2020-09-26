package com.emirhan.instaclonewithparse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    EditText userName, passWord;
    String userNameText, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userName = findViewById(R.id.signUserName);
        passWord = findViewById(R.id.signPassword);
        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser != null){
            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
            startActivity(intent);
        }

    }

    public void signUp(View view){
        ParseUser user = new ParseUser();
        userNameText = userName.getText().toString();
        pass = passWord.getText().toString();
        user.setUsername(userNameText);
        user.setPassword(pass);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Signed up successfully",Toast.LENGTH_LONG).show();
                    // Intent
                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void signIn(View view){
        userNameText = userName.getText().toString();
        pass = passWord.getText().toString();
        ParseUser.logInInBackground(userNameText, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, user.getUsername() +" Signed in successfully",Toast.LENGTH_LONG).show();
                    //Intent
                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    startActivity(intent);

                }
            }
        });
    }
}