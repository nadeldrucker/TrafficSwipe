package dev.nadeldrucker.trafficswipe.data.publicTransport.model.data;


import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;

import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.vehicle.Vehicle;

/**
 * RouteStep is one little step of a route. It contains a start, stop and a vehicle.
 */
public class RouteStep extends TransportEntity {
    private Station start, stop;
    private Vehicle connection;

    public RouteStep(@NonNull RequestQueue queue, @NonNull Station start, @NonNull Station stop, @NonNull Vehicle connection) {
        super(queue);
        this.start = start;
        this.stop = stop;
        this.connection = connection;
    }

    public Station getStart() {
        return start;
    }

    public Station getStop() {
        return stop;
    }

    public Vehicle getConnection() {
        return connection;
    }
}
