package dev.nadeldrucker.trafficswipe;

import android.app.Application;
import android.content.Context;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class App extends Application {

    private static App instance;

    public static App getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        instance = this;
    }
}
