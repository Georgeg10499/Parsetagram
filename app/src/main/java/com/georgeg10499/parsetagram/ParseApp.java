package com.georgeg10499.parsetagram;

import android.app.Application;

import com.georgeg10499.parsetagram.Model.ImagePost;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ImagePost.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("berk-mex")
                .clientKey("deque-llaveTh3Awes0m3T3am")
                .server("http://georgeg10499-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
