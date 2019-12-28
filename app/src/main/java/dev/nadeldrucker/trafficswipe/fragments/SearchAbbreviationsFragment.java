package dev.nadeldrucker.trafficswipe.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.ui.RecyclerSearchAdapter;
import dev.nadeldrucker.trafficswipe.viewModels.DeparturesViewModel;
import dev.nadeldrucker.trafficswipe.viewModels.SearchViewModel;

import java.util.Arrays;
import java.util.Objects;

public class SearchAbbreviationsFragment extends Fragment {

    private RecyclerSearchAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_abbreviations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText etSearch = view.findViewById(R.id.etSearch);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerSearchResult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerSearchAdapter();
        recyclerView.setAdapter(adapter);

        final FragmentActivity activity = Objects.requireNonNull(getActivity());

        final SearchViewModel searchViewModel = new ViewModelProvider(activity).get(SearchViewModel.class);
        searchViewModel.getQuery().postValue("%%");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchViewModel.getQuery().postValue("%" + s.toString() + "%");
            }
        });

        searchViewModel.getAbbreviations().observe(activity, abbreviations -> adapter.setAbbreviationList(Arrays.asList(abbreviations)));

        final DeparturesViewModel departuresViewModel = new ViewModelProvider(activity).get(DeparturesViewModel.class);
        adapter.setItemButtonClickedListener(abbreviation -> {
            departuresViewModel.getUserStationName().postValue(abbreviation.getAbbreviation());
            etSearch.clearFocus();
            Navigation.findNavController(view).navigate(R.id.action_searchAbbreviationsFragment_to_resultFragment);
        });
    }
}
