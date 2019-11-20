package dev.nadeldrucker.trafficswipe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;

import java.util.*;
import java.util.stream.Collectors;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.dao.transport.TransportApiFactory;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Entrypoint;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;
import dev.nadeldrucker.trafficswipe.ui.RecyclerResultAdapter;

public class ResultFragment extends Fragment {

    private static final String TAG = ResultFragment.class.getName();

    private RecyclerResultAdapter recyclerAdapter;
    private TextView tvName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerResult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new RecyclerResultAdapter();
        recyclerView.setAdapter(recyclerAdapter);

        tvName = view.findViewById(R.id.tvResult);

        String query = Objects.requireNonNull(getArguments()).getString(StartFragment.BUNDLE_QUERY);
        tvName.setText(query);

        queryData(query);
    }

    private void onStationNameChanged(String name) {
        tvName.setText(name);
    }

    /**
     * Queries data from api
     *
     * @param query query string
     */
    private void queryData(String query) {
        Entrypoint api = TransportApiFactory.createTransportApiDao(TransportApiFactory.ApiProvider.VVO,
                Volley.newRequestQueue(Objects.requireNonNull(getContext())));

        Objects.requireNonNull(api);

        api.getStops(query).thenCompose(stations -> {
            if (stations.size() > 0) {
                Station station = stations.get(0);
                onStationNameChanged(station.getName());
                return station.getDepartures();
            } else {
                throw new RuntimeException("No stations found!");
            }
        }).exceptionally(throwable -> {
            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            return Collections.emptyMap();
        }).thenAccept(this::onDeparturesChanged);
    }

    private void onDeparturesChanged(Map<Vehicle, DepartureTime> vehicleDepartureTimeMap) {
        recyclerAdapter.setDepartureItems(vehicleDepartureTimeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> new RecyclerResultAdapter.DepartureItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
        recyclerAdapter.notifyDataSetChanged();
    }


}
