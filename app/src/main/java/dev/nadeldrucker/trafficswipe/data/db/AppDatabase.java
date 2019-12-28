package dev.nadeldrucker.trafficswipe.data.db;

import android.content.Context;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import dev.nadeldrucker.trafficswipe.data.db.daos.AbbreviationDAO;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;

@Database(entities = {
        Abbreviation.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AbbreviationDAO abbreviationDAO();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tsw-db").build();
            instance.populateInitialData();
        }

        return instance;
    }

    private void populateInitialData() {
        if (abbreviationDAO().count() == 0) {
            runInTransaction(() -> {
                // TODO insert data from csv
            });
        }
    }
}
