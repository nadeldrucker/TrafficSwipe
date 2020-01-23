package dev.nadeldrucker.trafficswipe.viewModels;

import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.data.db.AppDatabase;
import dev.nadeldrucker.trafficswipe.data.db.entities.Station;
import dev.nadeldrucker.trafficswipe.viewModels.data.LocationLiveData;

import java.util.List;

public class LocationViewModel extends ViewModel {

    private LiveData<Location> location = new LocationLiveData();
    private LiveData<List<Station>> stations;

    public LocationViewModel() {
        stations = Transformations.switchMap(location, location ->
                AppDatabase.getInstance(App.getContext())
                        .stationDAO()
                        .queryNearest(location.getLatitude(), location.getLongitude(), 10));
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }
}
