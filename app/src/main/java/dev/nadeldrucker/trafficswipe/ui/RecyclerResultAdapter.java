package dev.nadeldrucker.trafficswipe.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.AbstractVehicle;

public class RecyclerResultAdapter extends RecyclerView.Adapter<RecyclerResultAdapter.ViewHolder> {

    private List<AbstractVehicle> vehicles;

    public RecyclerResultAdapter(List<AbstractVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AbstractVehicle vehicle = vehicles.get(position);
        holder.lineNumber.setText(vehicle.getLineId());
        holder.destination.setText(vehicle.getDestinationString());
        holder.lineNumber.setBackground(vehicle.getIcon());
        holder.departureTime.setText(vehicle.getRemainingTime(null)); //Todo pass Location
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public List<AbstractVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<AbstractVehicle> vehicles) {
        this.vehicles = vehicles;
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
