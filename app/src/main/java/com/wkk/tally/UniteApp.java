package com.wkk.tally;

import android.app.Application;

import com.wkk.tally.db.DBManager;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.initDB(getApplicationContext());
    }
}
