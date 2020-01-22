package dev.nadeldrucker.trafficswipe;

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mapbox.mapboxsdk.Mapbox;

public final class App extends Application {

    private static App instance;
    private static RequestQueue requestQueue;
    private static Gson gson;

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

    public static Gson getGson() {
        return gson;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(getApplicationContext());
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_token));
        gson = new Gson();
        instance = this;
    }
}
