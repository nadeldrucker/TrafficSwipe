package dev.nadeldrucker.trafficswipe.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.data.db.AppDatabase;
import dev.nadeldrucker.trafficswipe.data.db.entities.Station;

import java.util.List;

public class MapViewModel extends ViewModel {

    private MutableLiveData<LatLngBounds> boundingBox = new MutableLiveData<>();
    private LiveData<List<Station>> stations;

    public MapViewModel() {
        stations = Transformations.switchMap(boundingBox, b -> {
            double minLon = Math.min(b.getLonEast(), b.getLonWest()),
                    maxLon = Math.max(b.getLonEast(), b.getLonWest()),
                    minLat = Math.min(b.getLatSouth(), b.getLatNorth()),
                    maxLat = Math.max(b.getLatSouth(), b.getLatNorth());

            return AppDatabase.getInstance(App.getContext())
                    .stationDAO()
                    .queryWithinBounds(minLat, maxLat, minLon, maxLon);
        });
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }

    public MutableLiveData<LatLngBounds> getBoundingBox() {
        return boundingBox;
    }
}
