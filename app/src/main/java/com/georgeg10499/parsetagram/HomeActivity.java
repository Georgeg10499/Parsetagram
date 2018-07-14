package com.georgeg10499.parsetagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity{


    ImageButton ibFeed;
    ImageButton ibProfile;
    ImageButton ibCapture;

    FrameLayout flContainer;
    FragmentTransaction fragmentTransaction;

    Fragment feedFragment;
    Fragment cameraFragment;
    Fragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        // define your fragments here
        feedFragment = new FeedFragment();
        cameraFragment = new CameraFragment();
        profileFragment = new ProfileFragment();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, feedFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.fragment_feed:
                        fragmentTransaction.replace(R.id.flContainer, feedFragment).commit();
                        return true;
                    case R.id.fragment_camera:
                        fragmentTransaction.replace(R.id.flContainer, cameraFragment).commit();
                        return true;
                    case R.id.fragment_profile:
                        fragmentTransaction.replace(R.id.flContainer, profileFragment).commit();
                        return true;
                }
                return true;
            }
        });

    }

//    @Override
//    public void onFragmentInteraction(String string) {
//        //listened
//        mSearchString = string;
//        focusSearView();
//    }
}
