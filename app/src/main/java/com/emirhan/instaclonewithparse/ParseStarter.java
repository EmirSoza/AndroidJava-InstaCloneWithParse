package com.emirhan.instaclonewithparse;

import android.app.Application;

import com.parse.Parse;

public class ParseStarter extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.getLogLevel();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("APP_ID")
                // if defined
                .clientKey("CLIENT_KEY")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
