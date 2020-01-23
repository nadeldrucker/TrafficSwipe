package dev.nadeldrucker.trafficswipe.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Station;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.vehicle.Vehicle;
import dev.nadeldrucker.trafficswipe.ui.RecyclerResultAdapter;
import dev.nadeldrucker.trafficswipe.ui.UiUtil;
import dev.nadeldrucker.trafficswipe.viewModels.DeparturesViewModel;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

public class ResultFragment extends Fragment {

    public static final String ARG_QUERY = "query";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getArguments());
        final String query = getArguments().getString(ARG_QUERY);

        final DeparturesFragment departuresFragment = new DeparturesFragment();
        departuresFragment.setQuery(query);

        TextView tvTime = view.findViewById(R.id.tvLastFetch);
        departuresFragment.setOnTimeSinceLastRefreshChangedListener(delta -> {
            tvTime.setText(UiUtil.formatDuration(Duration.of(delta, ChronoUnit.MILLIS)));
        });

        TextView tvStation = view.findViewById(R.id.tvResult);
        departuresFragment.setOnStationsChangedListener(stations -> {
            if (!stations.isEmpty()) {
                final Station station = stations.get(0);
                tvStation.setText(station.getName());
            }
        });

        final FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.containerResult, departuresFragment);
        transaction.commit();
    }

}
