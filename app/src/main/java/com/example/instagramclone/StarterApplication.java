package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import static com.parse.Parse.enableLocalDatastore;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        // Enable Local Datastore
        enableLocalDatastore(this);

        //Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                .clientKey("R1oRGIiETkFA")
                .server("http://52.14.228.32/parse/")
                .build()
        );


//        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
