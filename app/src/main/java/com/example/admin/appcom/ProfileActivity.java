package com.example.admin.appcom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {
    private EditText name,email,phone;
    TextView textView;
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Update Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(ProfileActivity.this,MainActivity.class));
              finish();
            }
        });


        final sharedPrefData sharedPrefDataOB = new sharedPrefData();
       final userPOJO userPOJOOB = sharedPrefDataOB.getUserData(ProfileActivity.this);
       name  = findViewById(R.id.tname);
       email = findViewById(R.id.temail);
       phone = findViewById(R.id.tphone2);
       textView = findViewById(R.id.type);
       imageView = findViewById(R.id.pro_pic);
       button = findViewById(R.id.submit);
       textView.setText("You are logged in through "+userPOJOOB.getSignInType());
       if (userPOJOOB.getMobile_no()!=0)
       phone.setText(userPOJOOB.getMobile_no()+"");



        Glide.with(ProfileActivity.this)

                .load(userPOJOOB.getImageURL())

                .into(imageView);
        name.setText(userPOJOOB.getName());
        email.setText(userPOJOOB.getEmail());
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String uname  = name.getText().toString();
                                          String uemail  = email.getText().toString();
                                          String uphone  = phone.getText().toString();
                                         userPOJO  pojo  = new userPOJO();
                                         pojo.setImageURL(userPOJOOB.getImageURL());
                                         pojo.setSignInType(userPOJOOB.getSignInType());
                                         pojo.setName(uname);
                                         pojo.setEmail(uemail);
                                         pojo.setMobile_no(Long.parseLong(uphone));
                                          sharedPrefDataOB.saveUserData(pojo,ProfileActivity.this);
                                          Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                                      }
                                  }
        );




    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
    }
}
