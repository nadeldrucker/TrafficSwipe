package dev.nadeldrucker.trafficswipe.viewModels;

import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Station;
import dev.nadeldrucker.trafficswipe.viewModels.data.LocationLiveData;

import java.util.List;

public class LocationViewModel extends ViewModel {

    private LiveData<Location> location = new LocationLiveData();
    private LiveData<List<Station>> stations;

    public LocationViewModel() {

    }

    public LiveData<Location> getLocation() {
        return location;
    }
}
