package dev.nadeldrucker.trafficswipe.data.db.entities;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Station {

    @PrimaryKey
    @NonNull
    public String id;
    public String fullName;
    public String shortName;
    public String city;
    public double latitude;
    public double longitude;

    public Station(@NonNull String id, String fullName, String shortName, String city, double latitude, double longitude) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location getLocation() {
        final Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAccuracy(0);
        location.setAltitude(0);
        return location;
    }
}
