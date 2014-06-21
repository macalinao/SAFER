package com.firefightershelp;

import android.app.Application;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaveenT on 6/21/14.
 */
public class MyApplication extends Application {
    static String user = "Bob";
    static List<ParseUser> users;
    static List<String> usernames;
    @Override
    public void onCreate() {
        super.onCreate();
        //Debug Application
        //Parse.initialize(this, "B5H6u8fl4PXkvPpNKwxkXyoBHX5dXk9IxfXAwMwM", "nHJX7KClG8ql3CIuG3qDmBdIb0oOFiAuBsI2Wviy");
        //Prod Application
        users = new ArrayList<ParseUser>();
        usernames = new ArrayList<String>();
        Parse.initialize(this, "pAwMknVW1OcjKXYK7Lpj5Jibigfzio8kb2CdBScO", "YRL8bqthghkC5TsIPdybWGTIzuwab2i808JiSeUZ");
        //Setting the custom Actionbar
        //user = ParseUser.getCurrentUser();
        //Using the app security and the Parse default ACL(access control levels)
        /*ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access while disabling public write access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);*/
       /* defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);*/
        //Code for the push service in the application
        //PushService.setDefaultPushCallback(this, ExpandedPostActivity.class);
        //ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseQuery query =ParseUser.getQuery();
        try {
           users= query.find();
            for (ParseUser user : users) {
                usernames.add(user.getUsername());
            }
        }catch (ParseException e){
            e.printStackTrace();
        }


    }

}
