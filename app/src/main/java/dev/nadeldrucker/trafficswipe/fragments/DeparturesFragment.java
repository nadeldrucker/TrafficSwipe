package dev.nadeldrucker.trafficswipe.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Station;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.vehicle.Vehicle;
import dev.nadeldrucker.trafficswipe.ui.RecyclerResultAdapter;
import dev.nadeldrucker.trafficswipe.ui.UiUtil;
import dev.nadeldrucker.trafficswipe.viewModels.DeparturesViewModel;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DeparturesFragment extends Fragment {
    private RecyclerResultAdapter recyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final Handler handler = new Handler();

    private Runnable secondLoop;
    private long lastUpdateTime;

    private boolean isViewDestroyed = false;
    private Consumer<List<Station>> stationsConsumer;
    private Runnable onLoadingListener;
    private Consumer<Map<Vehicle, DepartureTime>> departuresChangedListener;
    private String query;
    private Consumer<Long> timeSinceLastRefreshChangedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        secondLoop = () -> {
            if (isViewDestroyed) return;

            // calculate time passed since last update
            if (lastUpdateTime != 0) {
                long updateDelta = System.currentTimeMillis() - lastUpdateTime;
                recyclerAdapter.getViewHolders().forEach(viewHolder -> viewHolder.subtractTimeFromOriginalDeparture(Duration.of(updateDelta, ChronoUnit.MILLIS)));
                if (timeSinceLastRefreshChangedListener != null) timeSinceLastRefreshChangedListener.accept(updateDelta);
            }

            handler.postDelayed(this.secondLoop, 1000);
        };
        handler.post(secondLoop);

        return inflater.inflate(R.layout.fragment_departures, container, false);
    }

    /**
     * Called when the stations change
     * @param stationsConsumer listener
     */
    public void setOnStationsChangedListener(Consumer<List<Station>> stationsConsumer){
        this.stationsConsumer = stationsConsumer;
    }

    /**
     * Called when the departures change
     * @param departuresChangedListener listener
     */
    public void setOnDeparturesChangedListener(Consumer<Map<Vehicle, DepartureTime>> departuresChangedListener){
        this.departuresChangedListener = departuresChangedListener;
    }

    /**
     * Called when data is beginning to load
     * @param onLoadingListener listener
     */
    public void setOnLoadingListener(Runnable onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
    }

    /**
     * Gets called every second with time in millis since last refresh happened.
     * @param timeSinceLastRefreshChangedListener listener
     */
    public void setOnTimeSinceLastRefreshChangedListener(Consumer<Long> timeSinceLastRefreshChangedListener){
        this.timeSinceLastRefreshChangedListener = timeSinceLastRefreshChangedListener;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerResult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new RecyclerResultAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);

        swipeRefreshLayout = view.findViewById(R.id.refreshLayoutResult);

        DeparturesViewModel viewModel = new ViewModelProvider(this).get(DeparturesViewModel.class);
        if (query != null) viewModel.getUserStationName().setValue(query);


        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        viewModel.getStations().observe(getViewLifecycleOwner(), listDataWrapper -> listDataWrapper.evaluate(
                stations -> {
                    if (stationsConsumer != null) stationsConsumer.accept(stations);
                },
                error -> Toast.makeText(requireActivity(), error.getErrorMessage(), Toast.LENGTH_LONG).show(),
                () -> {
                    if (onLoadingListener != null) onLoadingListener.run();
                    progressBar.setVisibility(View.VISIBLE);
                })
        );

        viewModel.getDepartures().observe(getViewLifecycleOwner(), mapDataWrapper -> mapDataWrapper.evaluate(
                vehicleDepartureTimeMap -> {
                    onDeparturesChanged(vehicleDepartureTimeMap);
                    progressBar.setVisibility(View.GONE);
                },
                error -> Toast.makeText(requireActivity(), error.getErrorMessage(), Toast.LENGTH_LONG).show(),
                () -> {
                    recyclerAdapter.updateDepartureItems(Collections.emptyList());
                    progressBar.setVisibility(View.VISIBLE);
                })
        );

        swipeRefreshLayout.setOnRefreshListener(viewModel::refresh);
    }

    @Override
    public void onDestroyView() {
        isViewDestroyed = true;
        super.onDestroyView();
    }

    /**
     * Called when departures have been queried, and are ready to be displayed
     *
     * @param vehicleDepartureTimeMap departure table map
     */
    private void onDeparturesChanged(Map<Vehicle, DepartureTime> vehicleDepartureTimeMap) {
        if (departuresChangedListener != null) departuresChangedListener.accept(vehicleDepartureTimeMap);
        if (timeSinceLastRefreshChangedListener != null) timeSinceLastRefreshChangedListener.accept(0L);

        recyclerAdapter.updateDepartureItems(vehicleDepartureTimeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> new RecyclerResultAdapter.DepartureItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));

        swipeRefreshLayout.setRefreshing(false);

        lastUpdateTime = System.currentTimeMillis();
        if (timeSinceLastRefreshChangedListener != null) timeSinceLastRefreshChangedListener.accept(0L);
    }

}
