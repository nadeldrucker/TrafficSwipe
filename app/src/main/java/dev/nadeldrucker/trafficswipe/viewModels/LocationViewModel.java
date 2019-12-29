package dev.nadeldrucker.trafficswipe.viewModels;

import android.location.Location;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.data.db.AppDatabase;
import dev.nadeldrucker.trafficswipe.data.db.entities.Station;
import dev.nadeldrucker.trafficswipe.ui.RecyclerLocationSearchAdapter;
import dev.nadeldrucker.trafficswipe.viewModels.data.LocationLiveData;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LocationViewModel extends ViewModel {

    private LiveData<Location> location = new LocationLiveData();
    private LiveData<List<Station>> stations;
    private LiveData<List<RecyclerLocationSearchAdapter.StationLocationBean>> locationStationBeans;

    public LocationViewModel() {
        stations = Transformations.switchMap(location, location ->
                AppDatabase.getInstance(App.getContext())
                        .stationDAO()
                        .queryNearest(location.getLatitude(), location.getLongitude(), 20));

        locationStationBeans = Transformations.switchMap(stations, s -> {
            final MutableLiveData<List<RecyclerLocationSearchAdapter.StationLocationBean>> liveData = new MutableLiveData<>();
            liveData.setValue(Collections.emptyList());

            AsyncTask.execute(() -> liveData.postValue(s.stream()
                    .map(station ->
                            new RecyclerLocationSearchAdapter.StationLocationBean(station,
                                    Objects.requireNonNull(location.getValue()).distanceTo(station.getLocation()),
                                    // TODO get abbreviation from db :)
                                    "XXX"))
                    .collect(Collectors.toList())));

            return liveData;
        });
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }

    public LiveData<List<RecyclerLocationSearchAdapter.StationLocationBean>> getLocationStationBeans() {
        return locationStationBeans;
    }
}
