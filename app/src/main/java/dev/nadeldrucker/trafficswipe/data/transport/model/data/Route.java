package dev.nadeldrucker.trafficswipe.data.transport.model.data;


import com.android.volley.RequestQueue;
import org.threeten.bp.ZonedDateTime;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.CompletableFuture;

/**
 * Route is a "collection" of RouteStep. It also provides some additional features.
 */
public class Route extends TransportEntity {

    private SortedSet<RouteStep> route;
    private Timestamp arrival;

    public Route(RequestQueue queue, SortedSet<RouteStep> route, Timestamp arrival) {
        super(queue);
        this.route = route;
        this.arrival = arrival;
    }

    public Timestamp getArrival() {
        return arrival;
    }

    public SortedSet<RouteStep> getRoute() {
        return route;
    }

    public CompletableFuture<ZonedDateTime> getStartTime() {
        Station start = route.first().getStart();
        // TODO fix this
        // return start.getDepartures().thenApply(departures -> departures.get(route.first().getConnection()).getDepartureTimeWithDelay());
        return null;
    }
}
