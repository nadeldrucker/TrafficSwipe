package dev.nadeldrucker.trafficswipe.data.db.daos;

import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import dev.nadeldrucker.trafficswipe.data.db.entities.Station;

import java.util.List;

@Dao
public interface StationDAO {

    @Query("SELECT * FROM station WHERE shortName LIKE (:shortName)")
    LiveData<Station[]> queryForShortName(String shortName);

    /**
     * Queries the nearest stations based on their WGS84 coordinates
     * @param lat latitude
     * @param lon longitude
     * @param limit how many items to query
     * @return stations sorted ascending
     */
    @Query("SELECT * FROM station ORDER BY ((latitude - (:lat)) * (latitude - (:lat)) + (longitude - (:lon)) * (longitude - (:lon))) ASC LIMIT 0, (:limit)")
    LiveData<Station[]> queryNearest(double lat, double lon, int limit);

    @Insert
    void insertAll(List<Station> stations);
}
