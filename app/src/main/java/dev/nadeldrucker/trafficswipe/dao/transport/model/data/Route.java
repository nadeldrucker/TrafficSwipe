package dev.nadeldrucker.trafficswipe.dao.transport.model.data;


import org.jetbrains.annotations.NotNull;
import org.threeten.bp.ZonedDateTime;

import java.sql.Timestamp;
import java.util.SortedSet;

/**
 * Route is a "collection" of RouteStep. It also provides some additional features.
 */
public class Route implements Comparable {

    private SortedSet<RouteStep> route;
    private Timestamp arrival;

    public Route(SortedSet<RouteStep> route, Timestamp arrival) {
        this.route = route;
        this.arrival = arrival;
    }

    public Timestamp getArrival() {
        return arrival;
    }

    public SortedSet<RouteStep> getRoute() {
        return route;
    }

    public ZonedDateTime getStartTime() {
        return route.first().getConnection().getActualDeparture();
    }


    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof Route))
            throw new IllegalArgumentException("You can only compare objects of the class Route");

        return this.getStartTime().compareTo(((Route) o).getStartTime());
    }
}
