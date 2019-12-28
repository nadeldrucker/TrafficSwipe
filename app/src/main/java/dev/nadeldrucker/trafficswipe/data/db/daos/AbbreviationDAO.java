package dev.nadeldrucker.trafficswipe.data.db.daos;

import androidx.room.*;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;

@Dao
public interface AbbreviationDAO {

    @Query("SELECT * FROM abbreviation WHERE abbreviation = (:abbreviation)")
    Abbreviation getAbbreviation(String abbreviation);

    @Query("SELECT COUNT(*) FROM abbreviation")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Abbreviation... abbreviations);

    @Delete
    void deleteAll(Abbreviation... abbreviation);

}
