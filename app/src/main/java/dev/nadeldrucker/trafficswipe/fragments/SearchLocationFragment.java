package dev.nadeldrucker.trafficswipe.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.viewModels.LocationViewModel;

import java.util.Objects;

public class SearchLocationFragment extends Fragment {

    private static final int PERMISSION_GRANTED_CALLBACK = 42;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requestLocationPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    /**
     * Init method called when permissions have been granted.
     */
    private void initModel() {
        final FragmentActivity activity = Objects.requireNonNull(getActivity());
        final LocationViewModel locationViewModel = new ViewModelProvider(activity).get(LocationViewModel.class);
        locationViewModel.getLocation().observe(this, location -> {
            Log.d("LOCATION", location.toString());
        });
    }

    /**
     * Requests all permissions if not already granted.
     * @param permissions permissions to request
     */
    private void requestLocationPermissions(String... permissions) {
        boolean allPermissionsGranted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(App.getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
            }
        }

        if (!allPermissionsGranted) {
            requestPermissions(permissions, PERMISSION_GRANTED_CALLBACK);
        } else {
            initModel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_GRANTED_CALLBACK) {
            if (grantResults.length < 2) {
                Toast.makeText(getContext(), R.string.location_permission_denied, Toast.LENGTH_LONG).show();
                Navigation.findNavController(Objects.requireNonNull(getView())).popBackStack();
            } else {
                initModel();
            }
        }
    }
}
