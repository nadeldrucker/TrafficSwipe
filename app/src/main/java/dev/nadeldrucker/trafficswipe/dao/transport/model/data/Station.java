package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import com.android.volley.RequestQueue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.AbstractVehicle;

/**
 * Represents a station in the public transport network.
 */
public abstract class Station extends AbstractTransportEntity {
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

    public abstract CompletableFuture<List<AbstractVehicle>> getDepartures();
}
