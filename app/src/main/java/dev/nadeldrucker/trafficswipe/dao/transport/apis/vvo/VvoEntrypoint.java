package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import com.android.volley.RequestQueue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import dev.nadeldrucker.jvvo.Models.Stop;
import dev.nadeldrucker.trafficswipe.dao.transport.model.connection.RequestException;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Entrypoint;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Route;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

@Deprecated
public class VvoEntrypoint extends Entrypoint {

    public VvoEntrypoint(RequestQueue queue) {
        super(queue);
    }

    @Override
    public CompletableFuture<List<Station>> getStops(String name) {
        CompletableFuture<List<Station>> future = new CompletableFuture<>();

        Stop.find(name, queue, response -> {
            if (response.getResponse().isPresent()) {
                List<Station> stations = response.getResponse().get().getStops()
                        .stream()
                        .map(stop -> VvoStation.fromJVVOStop(queue, stop))
                        .collect(Collectors.toList());
                future.complete(stations);
            } else if (response.getError().isPresent()) {
                future.completeExceptionally(new RequestException("Couldn't complete getStops request! " + response.getError().get().getDescription()));
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Route> getRoute(Station start, Station destination) {
        CompletableFuture<Route> future = new CompletableFuture<>();
        return future;
    }


}
