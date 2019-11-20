package dev.nadeldrucker.trafficswipe.dao.transport.model.data;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.AbstractVehicle;

/**
 * RouteStep is one little step of a route. It contains a start, stop and a vehicle.
 */
public class RouteStep implements Comparable<RouteStep> {
    private Station start, stop;
    private AbstractVehicle connection;

    public RouteStep(@NonNull Station start, @NonNull Station stop, @NonNull AbstractVehicle connection) {
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

    public AbstractVehicle getConnection() {
        return connection;
    }


    @Override
    public int compareTo(@NotNull RouteStep o) {
        return this.connection.getRemainingTime(start).compareTo(o.connection.getScheduledDeparture());
    }
}
