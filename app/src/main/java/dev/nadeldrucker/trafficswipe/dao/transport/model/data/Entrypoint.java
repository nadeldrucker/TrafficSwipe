package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import com.android.volley.RequestQueue;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Entrypoint for api, exposing operations.
 */
public abstract class Entrypoint {

    protected RequestQueue queue;

    public Entrypoint(RequestQueue queue){
        this.queue = queue;
    }

    /**
     * Queries stations by name.
     * @param name name to query
     * @return future throwing exception if an error occurs
     */
    public abstract CompletableFuture<List<Station>> getStops(String name);

    /**
     *
     * @param start start station
     * @param destination destination station
     * @return future throwing an exception if an error occurs
     */
    public abstract CompletableFuture<Route> getRoute(Station start, Station destination);

    /**
     * Provide routes from online service
     *
     * @param from start point for route
     * @param to   target for route
     * @return future of a list of routes, null if no route is available
     */
    public abstract CompletableFuture<List<Route>> getRoutes(Station from, Station to);

    /**
     * Get all departures from a stop
     *
     * @param from stop
     * @return future of departures from stop
     */
    public abstract CompletableFuture<List<Vehicle>> getDepartures(Station from);

    /**
     * Get all departures from a stop that also stops at a specific point / has the direction to
     *
     * @param from start point
     * @param over vehicle must stop at least here
     * @return future of list of all vehicles that match the query
     */
    public abstract CompletableFuture<List<Vehicle>> getDepartures(Station from, Station over);


}
