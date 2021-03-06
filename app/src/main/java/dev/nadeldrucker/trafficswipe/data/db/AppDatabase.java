package dev.nadeldrucker.trafficswipe.data.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import dev.nadeldrucker.trafficswipe.data.db.daos.AbbreviationDAO;
import dev.nadeldrucker.trafficswipe.data.db.daos.StationDAO;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;
import dev.nadeldrucker.trafficswipe.data.db.entities.Station;
import dev.nadeldrucker.trafficswipe.data.db.util.AbbreviationCSVReader;
import dev.nadeldrucker.trafficswipe.data.db.util.StationCSVReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {
        Abbreviation.class,
        Station.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AbbreviationDAO abbreviationDAO();
    public abstract StationDAO stationDAO();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tsw-db")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            Executors.newSingleThreadExecutor().execute(() -> {
                                instance.onInstanceCreated(context);
                            });
                        }
                    })
                    .fallbackToDestructiveMigrationFrom(1)
                    .build();
        }

        return instance;
    }

    /**
     * Called on instantiation of the db
     */
    private void onInstanceCreated(Context context) {
        if (abbreviationDAO().count() == 0) {
            populateAbbreviations(context);
        }
        if (stationDAO().count() == 0) {
            populateStations(context);
        }
    }

    public static final String[] abbreviationCsvAssetPaths = new String[]{
            "kuerzel_dresden.csv",
            "kuerzel_umland.csv"
    };

    private void populateAbbreviations(final Context context) {
        runInTransaction(() -> Arrays.stream(abbreviationCsvAssetPaths).forEach(path -> {
            try {
                final List<Abbreviation> abbreviations = new AbbreviationCSVReader().readFromStream(context.getAssets().open(path));
                abbreviationDAO().insertAll(abbreviations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private void populateStations(final Context context) {
        runInTransaction(() -> {
            try {
                final List<Station> stations = new StationCSVReader().readFromStream(context.getAssets().open("stations.csv"));
                stationDAO().insertAll(stations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
