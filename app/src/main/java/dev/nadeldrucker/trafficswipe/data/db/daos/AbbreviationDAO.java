package dev.nadeldrucker.trafficswipe.data.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;

import java.util.List;

@Dao
public interface AbbreviationDAO {

    @Query("SELECT * FROM abbreviation WHERE name LIKE (:query)")
    LiveData<Abbreviation[]> queryForName(String query);

    @Query("SELECT * FROM abbreviation WHERE abbreviation = (:abbreviation)")
    Abbreviation getAbbreviation(String abbreviation);

    @Query("SELECT COUNT(*) FROM abbreviation")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Abbreviation> abbreviations);

}
