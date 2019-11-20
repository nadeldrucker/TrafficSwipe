package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import com.android.volley.RequestQueue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import dev.nadeldrucker.jvvo.Models.Stop;
import dev.nadeldrucker.trafficswipe.dao.transport.model.connection.RequestException;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Location;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

@Deprecated
public class VvoStation extends Station {

    private String stopId;

    public VvoStation(RequestQueue queue, String name, Location location, String stopId) {
        super(queue, name, location);
        this.stopId = stopId;
    }

    /**
     * Creates new {@link VvoStation} from jVVO {@link Stop}
     * @param stop stop to use
     * @return station
     */
    public static VvoStation fromJVVOStop(RequestQueue queue, Stop stop){
        return new VvoStation(queue, stop.getName(),
                new Location(stop.getLocation().getLatitude(), stop.getLocation().getLongitude()),
                stop.getId());
    }

    @Override
    public CompletableFuture<List<Departure>> getDepartures() {
        CompletableFuture<List<Departure>> completableFuture = new CompletableFuture<>();
        dev.nadeldrucker.jvvo.Models.Departure.monitor(stopId, getQueue(), response -> {
            if (response.getResponse().isPresent()) {
                List<Departure> departures = response.getResponse().get().getDepartures()
                        .stream()
                        .map(departure -> VvoDeparture.fromJVVODeparture(getQueue(), this, departure))
                        .collect(Collectors.toList());
                completableFuture.complete(departures);
            } else if (response.getError().isPresent()) {
                completableFuture.completeExceptionally(new RequestException("Couldn't complete request! " + response.getError().get().getDescription()));
            }
        });
        return completableFuture;
    }
}
