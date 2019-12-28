package dev.nadeldrucker.trafficswipe.data.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import dev.nadeldrucker.trafficswipe.data.db.daos.AbbreviationDAO;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;
import dev.nadeldrucker.trafficswipe.data.db.util.AbbreviationCSVReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;

@Database(entities = {
        Abbreviation.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AbbreviationDAO abbreviationDAO();

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
    }

    public static final String[] abbreviationCsvAssetPaths = new String[]{
            "kuerzel_dresden.csv",
            "kuerzel_umland.csv"
    };

    private void populateAbbreviations(final Context context) {
        runInTransaction(() -> Arrays.stream(abbreviationCsvAssetPaths).forEach(path -> {
            try {
                final Abbreviation[] abbreviations = AbbreviationCSVReader.readAbbreviationsFromStream(context.getAssets().open(path));
                abbreviationDAO().insertAll(abbreviations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
