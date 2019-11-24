package dev.nadeldrucker.trafficswipe.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;

public class RecyclerResultAdapter extends RecyclerView.Adapter<RecyclerResultAdapter.ViewHolder> {

    private List<DepartureItem> departureItems = new ArrayList<>();

    public static class DepartureItem {
        Vehicle vehicle;
        DepartureTime departureTime;

        public DepartureItem(Vehicle vehicle, DepartureTime departureTime) {
            this.vehicle = vehicle;
            this.departureTime = departureTime;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DepartureItem departureItem = departureItems.get(position);
        Vehicle vehicle = departureItem.vehicle;
        holder.lineNumber.setText(vehicle.getLineId());
        holder.destination.setText(vehicle.getFinalDestination().getName());


        String text = UiUtil.formatDuration(departureItem.departureTime.getRemainingTime());
        holder.departureTime.setText(text);
        holder.lineNumber.setBackground(vehicle.getIcon());
        holder.destination.setSelected(true);

    }

    public void setDepartureItems(List<DepartureItem> departureItems) {
        this.departureItems = departureItems;
    }

    @Override
    public int getItemCount() {
        return departureItems.size();
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
