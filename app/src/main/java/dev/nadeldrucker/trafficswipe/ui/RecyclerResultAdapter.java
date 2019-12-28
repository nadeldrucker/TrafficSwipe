package dev.nadeldrucker.trafficswipe.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import java.util.*;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.vehicle.Vehicle;

public class RecyclerResultAdapter extends RecyclerView.Adapter<RecyclerResultAdapter.ViewHolder> {

    private List<DepartureItem> departureItems = new ArrayList<>();
    private Set<ViewHolder> viewHolders = new HashSet<>();
    private Context context;

    public RecyclerResultAdapter(Context context) {
        this.context = context;
    }

    public static class DepartureItem {
        public Vehicle vehicle;
        public DepartureTime departureTime;

        public DepartureItem(Vehicle vehicle, DepartureTime departureTime) {
            this.vehicle = vehicle;
            this.departureTime = departureTime;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lineNumber, destination, departureTime;

        /**
         * Stores the original duration until departure that the item had.
         */
        Duration originalTimeUntilDeparture;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lineNumber = itemView.findViewById(R.id.tvLineNumber);
            destination = itemView.findViewById(R.id.tvDestination);
            departureTime = itemView.findViewById(R.id.tvDepartureTime);
        }

        /**
         * Subtracts the specified amount of time from the original departure time.
         */
        public void subtractTimeFromOriginalDeparture(Duration minusDuration){
            String text = UiUtil.formatDuration(originalTimeUntilDeparture.minus(minusDuration));
            departureTime.setText(text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        viewHolders.add(holder);

        DepartureItem departureItem = departureItems.get(position);

        Vehicle vehicle = departureItem.vehicle;

        holder.lineNumber.setText(vehicle.getLineId());

        Drawable background = context.getDrawable(vehicle.getIconDrawableResource());
        Objects.requireNonNull(background);

        int backgroundColor = context.getColor(vehicle.getIconColor());

        background.setColorFilter(BlendModeColorFilterCompat.createBlendModeColorFilterCompat(backgroundColor, BlendModeCompat.MULTIPLY));
        holder.lineNumber.setBackground(background);

        holder.destination.setText(vehicle.getDirection());
        holder.destination.setSelected(true);

        holder.originalTimeUntilDeparture = departureItem.departureTime.getRemainingTime();

        String text = UiUtil.formatDuration(holder.originalTimeUntilDeparture);
        holder.departureTime.setText(text);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        viewHolders.remove(holder);
    }

    public void setDepartureItems(List<DepartureItem> departureItems) {
        this.departureItems = departureItems;
        notifyDataSetChanged();
    }

    public List<DepartureItem> getDepartureItems(){
        return departureItems;
    }

    public Set<ViewHolder> getViewHolders() {
        return viewHolders;
    }

    @Override
    public int getItemCount() {
        return departureItems.size();
    }

}
