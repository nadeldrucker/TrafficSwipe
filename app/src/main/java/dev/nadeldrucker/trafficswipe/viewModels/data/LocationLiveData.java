package dev.nadeldrucker.trafficswipe.viewModels.data;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import com.google.android.gms.location.*;
import dev.nadeldrucker.trafficswipe.App;

public class LocationLiveData extends LiveData<Location> {

    private final FusedLocationProviderClient locationProvider;
    private LocationCallback locationCallback;

    public LocationLiveData() {
        if (ContextCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w(getClass().getName(), "LocationLiveData: Permissions were not granted to access location!");
        }

        locationProvider = LocationServices.getFusedLocationProviderClient(App.getContext());
    }

    @Override
    protected void onActive() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                setValue(locationResult.getLastLocation());
            }
        };

        locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    protected void onInactive() {
        locationProvider.removeLocationUpdates(locationCallback);
    }

}
