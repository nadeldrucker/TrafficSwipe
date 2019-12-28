package dev.nadeldrucker.trafficswipe.viewModels;

import androidx.lifecycle.*;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.data.publicTransport.apis.generic.DataWrapper;
import dev.nadeldrucker.trafficswipe.data.publicTransport.apis.generic.Entrypoint;
import dev.nadeldrucker.trafficswipe.data.publicTransport.apis.generic.TransportApiFactory;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Station;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.vehicle.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * View model for departures table.<br>
 * <p>
 * Update the station name or the refreshTrigger to refresh departures.
 */
public class DeparturesViewModel extends ViewModel {

    private MutableLiveData<String> stationName = new MutableLiveData<>();
    private LiveData<DataWrapper<List<Station>>> stations;
    private LiveData<DataWrapper<Map<Vehicle, DepartureTime>>> departures;
    private MutableLiveData<Boolean> refreshMutable = new MutableLiveData<>();

    public DeparturesViewModel() {
        Entrypoint dao = TransportApiFactory.createTransportApiDao(TransportApiFactory.ApiProvider.VVO, App.getRequestQueue());

        stations = Transformations.switchMap(stationName, dao::getStops);

        MediatorLiveData<DataWrapper<List<Station>>> refreshMediator = new MediatorLiveData<>();
        refreshMediator.addSource(stations, refreshMediator::setValue);
        refreshMediator.addSource(refreshMutable, b -> refreshMediator.setValue(stations.getValue()));

        departures = Transformations.switchMap(refreshMediator, wrappedStationList -> {
            AtomicReference<LiveData<DataWrapper<Map<Vehicle, DepartureTime>>>> lambdaDepartures = new AtomicReference<>(new MutableLiveData<>());

            wrappedStationList.evaluate(
                    data -> lambdaDepartures.set(data.get(0).getDepartures()),
                    error -> lambdaDepartures.set(new MutableLiveData<>(DataWrapper.createOfError(error))),
                    () -> lambdaDepartures.set(new MutableLiveData<>(DataWrapper.createLoading()))
            );

            return lambdaDepartures.get();
        });
    }

    public LiveData<DataWrapper<List<Station>>> getStations() {
        return stations;
    }

    public MutableLiveData<String> getUserStationName() {
        return stationName;
    }

    public LiveData<DataWrapper<Map<Vehicle, DepartureTime>>> getDepartures() {
        return departures;
    }

    public void refresh(){
        refreshMutable.setValue(refreshMutable.getValue());
    }
}
