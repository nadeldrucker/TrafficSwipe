package dev.nadeldrucker.trafficswipe.dao.transport.apis;

import java.util.ArrayList;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Route;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.AbstractVehicle;

public interface InterfaceAPI {
    /**
     * Provide routes from online service
     *
     * @param from start point for route
     * @param to   target for route
     * @return list of routes, null if no route is available
     */
    ArrayList<Route> getRoutes(Station from, Station to);

    /**
     * Get all departures from a stop
     *
     * @param from stop
     * @return departures from stop
     */
    ArrayList<AbstractVehicle> getDepartures(Station from);

    /**
     * Get all departures from a stop that also stops at a specific point
     *
     * @param from start point
     * @param over vehicle must stop at least here
     * @return list of all vehicles that match the query
     */
    ArrayList<AbstractVehicle> getDepartures(Station from, Station over);

}
