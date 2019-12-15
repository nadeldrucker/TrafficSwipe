package dev.nadeldrucker.trafficswipe;

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jakewharton.threetenabp.AndroidThreeTen;

public final class App extends Application {

    private static App instance;
    private static RequestQueue requestQueue;

    public static App getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(instance);
        }

        return requestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        instance = this;
    }
}
