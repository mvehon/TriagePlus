package com.cse360.project;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        //Server and client keys
        Parse.initialize(this, "gwIpoJOq7RbzrlxoTWrJ4174HLloOt4GZSIooQ6A", "14Fa2RpaDUg7Cftdqp8aBWCWd2C6beG5DUxmMMeh");
    }
}
