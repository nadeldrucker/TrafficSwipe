package dev.nadeldrucker.trafficswipe.data.publicTransport.apis.vvo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import dev.nadeldrucker.jvvo.Models.Stop;
import dev.nadeldrucker.trafficswipe.data.publicTransport.apis.generic.DataWrapper;
import dev.nadeldrucker.trafficswipe.data.publicTransport.apis.generic.Entrypoint;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Route;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Station;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.vehicle.Vehicle;

public class VvoEntrypoint extends Entrypoint {

    public VvoEntrypoint(RequestQueue queue) {
        super(queue);
    }

    @Override
    public LiveData<DataWrapper<List<Station>>> getStops(String name) {
        MutableLiveData<DataWrapper<List<Station>>> liveData = new MutableLiveData<>();

        Stop.find(name, queue, response -> {
            if (response.getResponse().isPresent() && response.getResponse().get().getStops() != null) {
                List<Station> stations = Objects.requireNonNull(response.getResponse().get().getStops())
                        .stream()
                        .map(stop -> VvoStation.fromJVVOStop(queue, stop))
                        .collect(Collectors.toList());
                liveData.postValue(DataWrapper.createOfData(stations));
            } else if (response.getError().isPresent()) {
                liveData.postValue(DataWrapper.createOfError(DataWrapper.ErrorType.NETWORK_ERROR, "Couldn't complete getStops request! " + response.getError().get().getDescription()));
            } else if (response.getResponse().get().getStops() == null) {
                liveData.postValue(DataWrapper.createOfError(DataWrapper.ErrorType.NULL_ERROR, "API returned null"));
            }
        });

        return liveData;
    }

    @Override
    public CompletableFuture<Route> getRoute(Station start, Station destination) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public CompletableFuture<List<Route>> getRoutes(Station from, Station to) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public CompletableFuture<List<Vehicle>> getDepartures(Station from) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public CompletableFuture<List<Vehicle>> getDepartures(Station from, Station over) {
        throw new UnsupportedOperationException("Not implemented");
    }


}
