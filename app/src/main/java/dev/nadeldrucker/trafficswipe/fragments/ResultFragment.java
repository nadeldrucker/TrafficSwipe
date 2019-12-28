package dev.nadeldrucker.trafficswipe.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.vehicle.Vehicle;
import dev.nadeldrucker.trafficswipe.ui.RecyclerResultAdapter;
import dev.nadeldrucker.trafficswipe.ui.UiUtil;
import dev.nadeldrucker.trafficswipe.viewModels.DeparturesViewModel;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

public class ResultFragment extends Fragment {

    private static final String TAG = ResultFragment.class.getName();

    private RecyclerResultAdapter recyclerAdapter;
    private TextView tvResult;
    private TextView tvLastFetch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final Handler handler = new Handler();

    private Runnable secondLoop;
    private long lastUpdateTime;

    private boolean isViewDestroyed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        secondLoop = () -> {
            if (isViewDestroyed) return;

            // calculate time passed since last update
            if (lastUpdateTime != 0) {
                long updateDelta = System.currentTimeMillis() - lastUpdateTime;
                recyclerAdapter.getViewHolders().forEach(viewHolder -> viewHolder.subtractTimeFromOriginalDeparture(Duration.of(updateDelta, ChronoUnit.MILLIS)));
                updateLastFetchTime(updateDelta);
            }

            handler.postDelayed(this.secondLoop, 1000);
        };
        handler.post(secondLoop);

        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerResult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new RecyclerResultAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);

        tvResult = view.findViewById(R.id.tvResult);

        tvLastFetch = view.findViewById(R.id.tvLastFetch);

        swipeRefreshLayout = view.findViewById(R.id.refreshLayoutResult);

        FragmentActivity activity = Objects.requireNonNull(getActivity());

        DeparturesViewModel viewModel = new ViewModelProvider(activity).get(DeparturesViewModel.class);

        viewModel.getStations().observe(activity, listDataWrapper -> listDataWrapper.evaluate(
                stations -> tvResult.setText(stations.get(0).getName()),
                error -> Toast.makeText(activity, error.getErrorMessage(), Toast.LENGTH_LONG).show())
        );

        viewModel.getDepartures().observe(activity, mapDataWrapper -> mapDataWrapper.evaluate(
                this::onDeparturesChanged,
                error -> Toast.makeText(activity, error.getErrorMessage(), Toast.LENGTH_LONG).show())
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
        recyclerAdapter.setDepartureItems(vehicleDepartureTimeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> new RecyclerResultAdapter.DepartureItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));

        swipeRefreshLayout.setRefreshing(false);

        lastUpdateTime = System.currentTimeMillis();
        updateLastFetchTime(0);
    }

    /**
     * Updated textView with last updated and formatted time
     *
     * @param updateDeltaMillis time since last update in millis
     */
    private void updateLastFetchTime(long updateDeltaMillis) {
        Duration duration = Duration.of(updateDeltaMillis, ChronoUnit.MILLIS);
        String formatted = UiUtil.formatDuration(duration);
        tvLastFetch.setText(String.format("Time since last refresh: %s", formatted));
    }
}
