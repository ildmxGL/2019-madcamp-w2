package com.example.q.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button contactsBtn = (Button)findViewById(R.id.contactsBtn);
        final Button galleryBtn = (Button)findViewById(R.id.galleryBtn);
        final Button customBtn= (Button)findViewById(R.id.customBtn);

        init();
        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                galleryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                customBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                FragmentManager fragmentManager= getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new ContactsFragment());
                fragmentTransaction.commit();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                galleryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                customBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                FragmentManager fragmentManager= getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new GalleryFragment());
                fragmentTransaction.commit();
            }
        });

        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                galleryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                customBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                FragmentManager fragmentManager= getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new CustomFragment());
                fragmentTransaction.commit();
            }
        });
    }

    public void init(){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,new ContactsFragment());
        fragmentTransaction.commit();
    }
}
