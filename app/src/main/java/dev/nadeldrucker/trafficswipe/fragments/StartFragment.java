package dev.nadeldrucker.trafficswipe.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;
import dev.nadeldrucker.trafficswipe.inference.CharacterRecognizer;
import dev.nadeldrucker.trafficswipe.ui.CharacterDrawView;
import dev.nadeldrucker.trafficswipe.viewModels.DeparturesViewModel;
import dev.nadeldrucker.trafficswipe.viewModels.SearchViewModel;

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";
    private SearchViewModel searchViewModel;
    private String lastQuery;

    public StartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        final FloatingActionButton fabHelp = view.findViewById(R.id.startFragment_helpFab);
        fabHelp.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_startFragment_to_helpSheet));

        final EditText etSearch = view.findViewById(R.id.etSearch);
        searchViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(SearchViewModel.class);
        etSearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String input = etSearch.getText().toString().trim();


                previewString(input, view.findViewById(R.id.tvChar1), view.findViewById(R.id.tvChar2), view.findViewById(R.id.tvChar3));
                if (input.length() >= 3) {
                    searchViewModel.getQuery().postValue(input);
                    lastQuery = input.toUpperCase();

                    Log.d(TAG, "edit text is done! " + s);
                    setSoftKeyboardState(false, etSearch);

                    etSearch.clearFocus();
                    etSearch.getText().clear();



                }
            }
        });

        searchViewModel.getAbbreviations().observe(Objects.requireNonNull(getActivity()), this::handleResult);

        final FloatingActionButton fabSearch = view.findViewById(R.id.startFragment_searchFab);
        fabSearch.setOnClickListener(v -> {
            //etSearch.requestFocus();
            //setSoftKeyboardState(true, etSearch);
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_startFragment_to_searchAbbreviationsFragment);
        });
        previewString(etSearch.getText().toString().trim(), view.findViewById(R.id.tvChar1), view.findViewById(R.id.tvChar2), view.findViewById(R.id.tvChar3));

        CharacterDrawView drawView = view.findViewById(R.id.drawView);
        drawView.setListener(l -> {
            Log.d(TAG, "onViewCreated: accepted values");
            char inferredChar = CharacterRecognizer.getInstance().infer(l);
            drawView.clearTouchPaths();
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> etSearch.getText().append(inferredChar));
        });
    }


    /**
     * Sets the soft keyboard state.
     * @param isShown true if soft keyboard should be shown
     * @param view view
     */
    private void setSoftKeyboardState(boolean isShown, View view){
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (isShown) {
                inputMethodManager.showSoftInput(view, 0);
            } else {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Displays the departure table using the specified query request.
     * Or show an error if result is null
     * @param result Abbreviation to query
     */
    private void handleResult(Abbreviation... result) {
        if (getView() == null) return; //if activity isn't active anymore, do nothing
        if (result.length > 0) {
            new ViewModelProvider(Objects.requireNonNull(getActivity())).get(DeparturesViewModel.class).getUserStationName().postValue(result[0].getName());
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_startFragment_to_resultFragment);
        } else {
            ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.tvInfo)).setText(lastQuery + " doesn't exist. Try again!");
            ((EditText) Objects.requireNonNull(getView()).findViewById(R.id.etSearch)).getText().clear();

        }
    }

    /**
     * Shows a String char by char in TextViews filled with '●'
     *
     * @param s   String that should be displayed
     * @param tvs TextViews where each should have enough space for one char
     */

    private void previewString(String s, TextView... tvs) {
        char[] preview = new char[tvs.length];
        for (int i = 0; i < preview.length; i++) preview[i] = '●';
        if (s != null) {
            s = s.trim().toUpperCase();
            for (int i = 0; i < preview.length && i < s.length(); i++)
                preview[i] = s.toCharArray()[i];
        }
        for (int i = 0; i < tvs.length; i++) tvs[i].setText(String.valueOf(preview[i]));
    }
}
