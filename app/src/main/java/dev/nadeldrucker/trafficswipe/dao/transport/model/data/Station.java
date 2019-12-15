package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import androidx.annotation.Nullable;

import androidx.lifecycle.LiveData;
import com.android.volley.RequestQueue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import dev.nadeldrucker.trafficswipe.dao.transport.apis.generic.DataWrapper;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;

/**
 * Represents a station in the public transport network.
 */
public abstract class Station extends TransportEntity {
    private String name;
    private String shortage;
    private Location location;

    public Station(RequestQueue queue, String name, Location location, String shortage) {
        super(queue);
        this.name = name;
        this.shortage = shortage;
        this.location = location;
    }

    public Station(RequestQueue queue, String name, Location location) {
        super(queue);
        this.name = name;
        this.location = location;
    }

    public String getShortage() {
        return shortage;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public abstract LiveData<DataWrapper<Map<Vehicle, DepartureTime>>> getDepartures();

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Station) {
            if (((Station) obj).shortage != null || shortage != null)
                return shortage.equals(((Station) obj).shortage);
            return ((Station) obj).name.equals(name);
        } else if (obj instanceof Location)
            return location.getLatitude() == ((Location) obj).getLatitude() && location.getLongitude() == ((Location) obj).getLongitude();


        return false;
    }
}
