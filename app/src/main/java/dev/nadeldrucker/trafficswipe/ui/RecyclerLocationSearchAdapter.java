package dev.nadeldrucker.trafficswipe.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RecyclerLocationSearchAdapter extends RecyclerView.Adapter<RecyclerLocationSearchAdapter.ViewHolder>{

    private List<Station> stationList = new ArrayList<>();
    private Consumer<Abbreviation> listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button btnAbbreviation;
        final TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAbbreviation = itemView.findViewById(R.id.tvAbbreviation);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public void setStationList(List<Station> stationList) {
        this.stationList = stationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_search, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }


}
