package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;

/**
 * Represents a station in the public transport network.
 */
public abstract class Station extends TransportEntity {
    private String name;
    private String abbreviation;
    private Location location;

    public Station(RequestQueue queue, String name, Location location, String abbreviation) {
        super(queue);
        this.name = name;
        this.abbreviation = abbreviation;
        this.location = location;
    }

    public Station(RequestQueue queue, String name, Location location) {
        super(queue);
        this.name = name;
        this.location = location;
    }

    public char[] getAbbreviation() {
        return abbreviation;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public abstract CompletableFuture<Map<Vehicle, DepartureTime>> getDepartures();

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Station) {
            if (((Station) obj).abbreviation != null || abbreviation != null)
                return abbreviation.equals(((Station) obj).abbreviation);
            return ((Station) obj).name.equals(name);
        } else if (obj instanceof Location)
            return location.getLatitude() == ((Location) obj).getLatitude() && location.getLongitude() == ((Location) obj).getLongitude();


        return false;
    }
}
