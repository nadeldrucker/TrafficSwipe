package dev.nadeldrucker.trafficswipe;

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mapbox.mapboxsdk.Mapbox;

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
        AndroidThreeTen.init(getApplicationContext());
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_token));
        instance = this;
    }
}
