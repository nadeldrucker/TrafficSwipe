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
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.viewModels.LocationViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchLocationFragment extends Fragment {

    private static final int PERMISSION_GRANTED_CALLBACK = 42;
    public final String TAG = this.getClass().getSimpleName();

    private MapboxMap mapboxMap;
    private MapView mapView;
    private SymbolManager symbolManager;
    private List<Symbol> currentlyAddedSymbols = new ArrayList<>();

    private boolean isInitPermission = false;
    private boolean isInitMap = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestLocationPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map -> {
            map.setStyle(Style.DARK, style -> {
                Log.d(TAG, "onViewCreated: Map style set!");

                symbolManager = new SymbolManager(mapView, map, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setIconIgnorePlacement(true);

                isInitMap = true;
                mapboxMap = map;
                checkInitCompleted();
            });
        });
    }

    private void checkInitCompleted() {
        if (isInitPermission && isInitMap) onInit();
    }


    /**
     * Init method called when everything was set up.
     */
    private void onInit() {
        // add user location to map
        mapboxMap.getStyle(style -> {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(Objects.requireNonNull(getContext()), style).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        });


        final FragmentActivity activity = Objects.requireNonNull(getActivity());
        final LocationViewModel locationViewModel = new ViewModelProvider(activity).get(LocationViewModel.class);

        locationViewModel.getLocationStationBeans().observe(getViewLifecycleOwner(), stationLocationBeans -> {
            symbolManager.delete(currentlyAddedSymbols);
            currentlyAddedSymbols = symbolManager.create(stationLocationBeans.stream()
                    .map(bean -> new SymbolOptions()
                            .withLatLng(new LatLng(bean.station.latitude, bean.station.longitude))
                            .withIconImage("marker-15")
                            .withIconSize(2.5f)).collect(Collectors.toList()));
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
            // all permissions have been granted
            isInitPermission = true;
            checkInitCompleted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_GRANTED_CALLBACK) {
            if (grantResults.length < 2) {
                Toast.makeText(getContext(), R.string.location_permission_denied, Toast.LENGTH_LONG).show();
                Navigation.findNavController(Objects.requireNonNull(getView())).popBackStack();
            } else {
                isInitPermission = true;
                checkInitCompleted();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
