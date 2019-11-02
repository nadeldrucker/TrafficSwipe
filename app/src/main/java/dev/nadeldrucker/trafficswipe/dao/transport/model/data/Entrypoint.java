package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import com.android.volley.RequestQueue;
import dev.nadeldrucker.jvvo.Models.Stop;

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

}
