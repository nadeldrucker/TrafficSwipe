package dev.nadeldrucker.trafficswipe.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.data.db.AppDatabase;
import dev.nadeldrucker.trafficswipe.data.db.daos.AbbreviationDAO;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;

/**
 * View model for searching stations in db
 */
public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> query = new MutableLiveData<>();
    private LiveData<Abbreviation[]> abbreviations;

    public SearchViewModel() {
        final AbbreviationDAO dao = AppDatabase.getInstance(App.getContext()).abbreviationDAO();

        abbreviations = Transformations.switchMap(query, dao::queryForName);
    }

    public LiveData<Abbreviation[]> getAbbreviations() {
        return abbreviations;
    }

    public MutableLiveData<String> getQuery() {
        return query;
    }
}
