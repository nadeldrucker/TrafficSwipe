package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import com.android.volley.RequestQueue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a station in the public transport network.
 */
public abstract class Station extends AbstractTransportEntity {
    private String name;
    private Location location;

    public Station(RequestQueue queue, String name, Location location) {
        super(queue);
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public abstract CompletableFuture<List<Departure>> getDepartures();
}
