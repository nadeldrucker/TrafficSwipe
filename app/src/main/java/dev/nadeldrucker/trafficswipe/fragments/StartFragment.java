package dev.nadeldrucker.trafficswipe.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import dev.nadeldrucker.trafficswipe.R;

import java.util.Objects;

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";
    public static final String BUNDLE_QUERY = "query";

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
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d(TAG, "edit text is done! " + etSearch.getText().toString());

                setSoftKeyboardState(false, etSearch);

                showDepartureTable(etSearch.getText().toString());
                etSearch.clearFocus();
                etSearch.getText().clear();
                return true;
            }
            return false;
        });

        final FloatingActionButton fabSearch = view.findViewById(R.id.startFragment_searchFab);
        fabSearch.setOnClickListener(v -> {
            etSearch.requestFocus();

            setSoftKeyboardState(true, etSearch);
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
     * @param query name to query
     */
    protected void showDepartureTable(String query){
        Bundle b = new Bundle();
        b.putString(BUNDLE_QUERY, query);
        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_startFragment_to_resultFragment, b);
    }
}
