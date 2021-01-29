package com.example.admin.appcom;

import android.app.Fragment;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class contact_list_activity extends AppCompatActivity implements list_frag.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Contact List");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
