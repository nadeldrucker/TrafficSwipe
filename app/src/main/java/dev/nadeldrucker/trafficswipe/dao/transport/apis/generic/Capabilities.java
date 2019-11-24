package dev.nadeldrucker.trafficswipe.dao.transport.apis.generic;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Route;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;

public interface Capabilities {


    /**
     * Queries stations by name.
     *
     * @param name name to query
     * @return future throwing exception if an error occurs
     */
    CompletableFuture<List<Station>> getStops(String name);

    /**
     * @param start       start station
     * @param destination destination station
     * @return future throwing an exception if an error occurs
     */
    CompletableFuture<Route> getRoute(Station start, Station destination);

    /**
     * Provide routes from online service
     *
     * @param from start point for route
     * @param to   target for route
     * @return future of a list of routes, null if no route is available
     */
    CompletableFuture<List<Route>> getRoutes(Station from, Station to);

    /**
     * Get all departures from a stop
     *
     * @param from stop
     * @return future of departures from stop
     */
    CompletableFuture<List<Vehicle>> getDepartures(Station from);

    /**
     * Get all departures from a stop that also stops at a specific point / has the direction to
     *
     * @param from start point
     * @param over vehicle must stop at least here
     * @return future of list of all vehicles that match the query
     */
    CompletableFuture<List<Vehicle>> getDepartures(Station from, Station over);


}