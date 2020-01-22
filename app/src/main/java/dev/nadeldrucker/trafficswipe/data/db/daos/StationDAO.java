package dev.nadeldrucker.trafficswipe.data.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import dev.nadeldrucker.trafficswipe.data.db.entities.Station;

import java.util.List;

@Dao
public interface StationDAO {

    @Query("SELECT * FROM station WHERE shortName LIKE (:shortName)")
    LiveData<Station[]> queryForShortName(String shortName);

    @Query("SELECT * FROM station WHERE (latitude BETWEEN :minLat AND :maxLat) AND (longitude BETWEEN :minLon AND :maxLon)")
    LiveData<List<Station>> queryWithinBounds(double minLat, double maxLat, double minLon, double maxLon);

    @Query("SELECT * FROM station")
    LiveData<List<Station>> getAll();

    /**
     * Queries the nearest stations based on their WGS84 coordinates
     * @param lat latitude
     * @param lon longitude
     * @param limit how many items to query
     * @return stations sorted ascending
     */
    @Query("SELECT * FROM station ORDER BY ((latitude - (:lat)) * (latitude - (:lat)) + (longitude - (:lon)) * (longitude - (:lon))) ASC LIMIT 0, (:limit)")
    LiveData<List<Station>> queryNearest(double lat, double lon, int limit);

    @Query("SELECT COUNT(*) FROM station")
    int count();

    @Insert
    void insertAll(List<Station> stations);
}
