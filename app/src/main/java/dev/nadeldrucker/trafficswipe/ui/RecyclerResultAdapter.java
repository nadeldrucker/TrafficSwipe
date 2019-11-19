package dev.nadeldrucker.trafficswipe.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Departure;
import org.threeten.bp.Instant;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

public class RecyclerResultAdapter extends RecyclerView.Adapter<RecyclerResultAdapter.ViewHolder> {

    private List<Departure> departures;

    public RecyclerResultAdapter(List<Departure> departures){
        this.departures = departures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Departure departure = departures.get(position);
        holder.lineNumber.setText(departure.getLineNumber());
        holder.destination.setText(departure.getDestination());

        ZonedDateTime timeUntilDeparture = departure.getDepartureTime().minus(ZonedDateTime.now().toEpochSecond(), ChronoUnit.SECONDS);
        holder.departureTime.setText(timeUntilDeparture.format(DateTimeFormatter.ofPattern("[HH:]mm:ss")));
    }

    @Override
    public int getItemCount() {
        return departures.size();
    }

    public List<Departure> getDepartures() {
        return departures;
    }

    public void setDepartures(List<Departure> departures) {
        this.departures = departures;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lineNumber, destination, departureTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lineNumber = itemView.findViewById(R.id.tvLineNumber);
            destination = itemView.findViewById(R.id.tvDestination);
            departureTime = itemView.findViewById(R.id.tvDepartureTime);
        }
    }
}
