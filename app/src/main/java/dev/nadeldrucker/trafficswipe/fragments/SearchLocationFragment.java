package dev.nadeldrucker.trafficswipe.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.gson.Gson;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
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
import dev.nadeldrucker.trafficswipe.data.db.entities.Station;
import dev.nadeldrucker.trafficswipe.viewModels.DeparturesViewModel;
import dev.nadeldrucker.trafficswipe.viewModels.MapViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchLocationFragment extends Fragment {

    private static final int PERMISSION_GRANTED_CALLBACK = 42;
    public final String TAG = this.getClass().getSimpleName();

    private MapView mapView;

    private static final double ZOOM_THRESHOLD = 12.5D;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        requestLocationPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    /**
     * Init method called when everything was set up.
     */
    private void onInit() {
        final FragmentActivity activity = Objects.requireNonNull(getActivity());
        final MapViewModel mapViewModel = new ViewModelProvider(activity).get(MapViewModel.class);

        mapView.getMapAsync(mapboxMap -> {
            // add user location to map
            mapboxMap.setStyle(Style.DARK, style -> {
                style.addImage("marker-icon", getResources().getDrawable(R.drawable.ic_place_black_24dp, null));

                final SymbolManager symbolManager;
                symbolManager = new SymbolManager(mapView, mapboxMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setIconIgnorePlacement(true);
                symbolManager.setTextOptional(true);

                symbolManager.addClickListener(symbol -> {
                    final Station station = App.getGson().fromJson(symbol.getData(), Station.class);

                    final DeparturesViewModel departuresViewModel = new ViewModelProvider(activity).get(DeparturesViewModel.class);
                    departuresViewModel.getUserStationName().setValue(station.id);
                    Navigation.findNavController(mapView.getRootView()).navigate(R.id.action_searchLocationFragment_to_resultFragment);
                });

                final LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(Objects.requireNonNull(getContext()), style).build());
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setRenderMode(RenderMode.COMPASS);
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.zoomWhileTracking(ZOOM_THRESHOLD + .1D);

                final List<Symbol> currentlyAddedSymbols = new ArrayList<>();

                final Observer<List<Station>> stationsObserver = stations -> {
                    // delete symbols that are now outside of view
                    final List<Symbol> outsideViewSymbols = currentlyAddedSymbols.stream()
                            .filter(symbol -> !mapboxMap.getProjection().getVisibleRegion().latLngBounds.contains(symbol.getLatLng()))
                            .collect(Collectors.toList());
                    symbolManager.delete(outsideViewSymbols);

                    final List<Symbol> insideViewSymbols = new ArrayList<>(currentlyAddedSymbols);
                    insideViewSymbols.removeAll(outsideViewSymbols);

                    currentlyAddedSymbols.clear();

                    // filter to only add symbols not already present in view
                    currentlyAddedSymbols.addAll(
                            symbolManager.create(stations.stream()
                                    .filter(station -> insideViewSymbols.stream()
                                            .noneMatch(symbol -> symbol.getLatLng().equals(new LatLng(station.latitude, station.longitude))))
                                    .map(bean -> new SymbolOptions()
                                            .withLatLng(new LatLng(bean.latitude, bean.longitude))
                                            .withIconImage("marker-icon")
                                            .withIconSize(1.2f)
                                            .withTextSize(10f)
                                            .withTextOffset(new Float[]{0f, -2f})
                                            .withTextField(bean.shortName)
                                            .withTextColor("white")
                                            .withData(App.getGson().toJsonTree(bean)))
                                    .collect(Collectors.toList()))
                    );
                };
                mapViewModel.getStations().observe(getViewLifecycleOwner(), stationsObserver);

                final Runnable updatePosition = () -> {
                    if (mapboxMap.getCameraPosition().zoom >= ZOOM_THRESHOLD) {
                        final LatLngBounds bounds = mapboxMap.getProjection().getVisibleRegion().latLngBounds;
                        mapViewModel.getBoundingBox().postValue(bounds);
                    } else {
                        symbolManager.deleteAll();
                        currentlyAddedSymbols.clear();
                        Toast.makeText(getContext(), "Zoom in to see stations!", Toast.LENGTH_SHORT).show();
                    }
                };

                new Handler().postDelayed(updatePosition, 200);

                mapboxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
                    @Override
                    public void onMoveBegin(@NonNull MoveGestureDetector detector) {

                    }

                    @Override
                    public void onMove(@NonNull MoveGestureDetector detector) {

                    }

                    @Override
                    public void onMoveEnd(@NonNull MoveGestureDetector detector) {
                        updatePosition.run();
                    }
                });
            });
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
            onInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_GRANTED_CALLBACK) {
            if (grantResults.length < 2) {
                Toast.makeText(getContext(), R.string.location_permission_denied, Toast.LENGTH_LONG).show();
                Navigation.findNavController(Objects.requireNonNull(getView())).popBackStack();
            } else {
                onInit();
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
