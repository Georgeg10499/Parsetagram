package com.georgeg10499.parsetagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.georgeg10499.parsetagram.Model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ImageButton ibFeed;
    ImageButton ibProfile;
    ImageButton ibCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ibCapture = (ImageButton) findViewById(R.id.ibCapture);
        ibFeed = (ImageButton) findViewById(R.id.ibFeed);
        ibProfile = (ImageButton) findViewById(R.id.ibProfile);

        final Post.Query postsQuery = new Post.Query();
        postsQuery
                .getTop()
                .withUser();


        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e==null){
                    for (int i = 0;i<objects.size(); i++){
                        Log.d("FeedActivity", "Post ["+i+"] = "
                                + objects.get(i).getDescription()
                                + "\n username = " + objects.get(i).getUser().getUsername());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    public void changeFragment(View view){
        Fragment fragment;

        if (view == findViewById(R.id.ibFeed)) {

            ibCapture.setImageResource(R.drawable.instagram_new_post_outline_24);
            ibFeed.setImageResource(R.drawable.instagram_home_filled_24);
            ibProfile.setImageResource(R.drawable.instagram_user_outline_24);
            FeedFragment feedFragment = new FeedFragment();
            fragment = feedFragment;

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentWindow, fragment);

            ft.commit();


        } else if (view == findViewById(R.id.ibCapture)) {

            ibCapture.setImageResource(R.drawable.instagram_new_post_filled_24);
            ibFeed.setImageResource(R.drawable.instagram_home_outline_24);
            ibProfile.setImageResource(R.drawable.instagram_user_outline_24);

            CameraFragment cam = new CameraFragment();
            fragment = cam;

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentWindow, fragment);

            ft.commit();

        } else if (view == findViewById(R.id.ibProfile)) {


            ibCapture.setImageResource(R.drawable.instagram_new_post_outline_24);
            ibFeed.setImageResource(R.drawable.instagram_home_outline_24);
            ibProfile.setImageResource(R.drawable.instagram_user_filled_24);

            ProfileFragment profileFragment = new ProfileFragment();
            fragment = profileFragment;

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentWindow, fragment);

            ft.commit();
        }

    }



}
