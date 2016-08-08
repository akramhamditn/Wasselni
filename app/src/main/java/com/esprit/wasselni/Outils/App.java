package com.esprit.wasselni.Outils;


import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "UF46E99YJJbhStTcqQyCshrBsnQ3C9o8Ha3ob6RH", "T6IhVyPBBzOFnVL2Tuwb7aspBaxmtDpgwQmkpyrf");
    }
}