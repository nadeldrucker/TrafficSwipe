package dev.nadeldrucker.trafficswipe.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Objects;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.inference.CharacterRecognizer;
import dev.nadeldrucker.trafficswipe.ui.CharacterDrawView;
import dev.nadeldrucker.trafficswipe.ui.UiUtil;
import dev.nadeldrucker.trafficswipe.viewModels.DeparturesViewModel;

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";
    private static final int REQUEST_PERMISSIONS_CODE = 43256;
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private FrameLayout bottomSheet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        final FloatingActionButton fabHelp = view.findViewById(R.id.startFragment_helpFab);
        fabHelp.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_startFragment_to_helpSheet));

        final FloatingActionButton fabSearch = view.findViewById(R.id.startFragment_searchFab);
        fabSearch.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_startFragment_to_searchAbbreviationsFragment));

        // Setup character drawing
        final EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                previewString(etSearch.getText().toString(), view.findViewById(R.id.tvChar1), view.findViewById(R.id.tvChar2), view.findViewById(R.id.tvChar3));
                if (etSearch.getText().toString().length() >= 3) {
                    Log.d(TAG, "edit text is done! " + etSearch.getText().toString());
                    setSoftKeyboardState(false, etSearch);
                    showDepartureTable(etSearch.getText().toString());
                    etSearch.clearFocus();
                    etSearch.getText().clear();
                }
            }
        });
        previewString(etSearch.getText().toString(), view.findViewById(R.id.tvChar1), view.findViewById(R.id.tvChar2), view.findViewById(R.id.tvChar3));

        CharacterDrawView drawView = view.findViewById(R.id.drawView);
        drawView.setColor(getNextColor(etSearch.getText().toString().trim().length()));
        drawView.setListener(l -> {
            char inferredChar = CharacterRecognizer.getInstance().infer(l);
            drawView.clearTouchPaths();
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                etSearch.getText().append(inferredChar);
                drawView.setColor(getNextColor(etSearch.getText().toString().trim().length()));
            });
        });

        // Setup bottom fab
        bottomSheet = view.findViewById(R.id.bottomSheet);

        // Request permissions for location...
        UiUtil.requestLocationPermissions(this::initBottomSheet, this, REQUEST_PERMISSIONS_CODE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    private void initBottomSheet() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (Arrays.stream(grantResults).filter(x -> x == PackageManager.PERMISSION_DENIED).count() > 0) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HIDDEN);
                Snackbar.make(Objects.requireNonNull(getView()),
                        "No location permissions given, nearby stations are not visible!",
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            } else {
                // all permissions have been granted, continue
                initBottomSheet();
            }
        }
    }

    /**
     * Get the color for the next Char
     *
     * @param position number of already drawn chars
     * @return Color as integer for the next char
     */
    private @ColorInt
    int getNextColor(int position) {
        int[] colorSet = {
                ResourcesCompat.getColor(getResources(), R.color.colorAccent, null),
                ResourcesCompat.getColor(getResources(), R.color.colorAccent2, null),
                ResourcesCompat.getColor(getResources(), R.color.colorAccent3, null),
                ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)
        };
        return colorSet[position];
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
        Bundle bundle = new Bundle();
        bundle.putString(ResultFragment.ARG_QUERY, query);
        Navigation.findNavController(requireView()).navigate(R.id.action_startFragment_to_resultFragment, bundle);
    }

    /**
     * Shows a String char by char in TextViews filled with '●'
     *
     * @param s   String that should be displayed
     * @param tvs TextViews where each should have enough space for one char
     */

    private void previewString(String s, TextView... tvs) {
        char[] preview = new char[tvs.length];
        Arrays.fill(preview, '●');

        if (s != null) {
            s = s.trim().toUpperCase();
            for (int i = 0; i < preview.length && i < s.length(); i++)
                preview[i] = s.toCharArray()[i];
        }

        for (int i = 0; i < tvs.length; i++) tvs[i].setText(String.valueOf(preview[i]));
    }
}
