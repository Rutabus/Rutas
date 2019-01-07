package com.aqp.rutabus.rutabusaqp;

import android.app.Application;
import android.content.Context;

import com.aqp.rutabus.rutabusaqp.data.AppDataBase;
import com.facebook.stetho.Stetho;

public class App extends Application {

    private static AppDataBase db;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
        db = AppDataBase.getInstance(getApplicationContext());
    }

    public static Context getContext(){
        return context;
    }

    public static AppDataBase getDb(){
        return db;
    }
}
