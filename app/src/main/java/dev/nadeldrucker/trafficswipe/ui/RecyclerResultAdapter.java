package dev.nadeldrucker.trafficswipe.ui;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;

public class RecyclerResultAdapter extends RecyclerView.Adapter<RecyclerResultAdapter.ViewHolder> {

    private List<DepartureItem> departureItems = new ArrayList<>();
    private ArrayList<TimeEventContainer> timeEvents = new ArrayList<>();
    private Handler handler = new Handler();
    private TextView tvRefresh;
    private ZonedDateTime lastRefresh = ZonedDateTime.now();

    private ZonedDateTime getLastRefresh() {
        return lastRefresh;
    }


    public static class DepartureItem {
        Vehicle vehicle;
        DepartureTime departureTime;

        public DepartureItem(Vehicle vehicle, DepartureTime departureTime) {
            this.vehicle = vehicle;
            this.departureTime = departureTime;
        }
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            timeEvents.forEach(TimeEventContainer::execute); //Update all timers
            handler.postDelayed(this, 1000); //Wait 1s
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_result, parent, false);
        handler.post(runnableCode);
        tvRefresh = v.findViewById(R.id.tvTimestamp);
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
        //add event to Timer
        timeEvents.add(new TimeEventContainer<>(holder.departureTime::setText, UiUtil::formatDuration, departureItem.departureTime::getRemainingTime));

    }

    public void setDepartureItems(List<DepartureItem> departureItems) {
        this.departureItems = departureItems;
        lastRefresh = ZonedDateTime.now();
        //remove all old events
        timeEvents = new ArrayList<>();
        //add event for last refresh timer
        timeEvents.add(new TimeEventContainer<>(tvRefresh::setText, UiUtil::formatTimestamp, this::getLastRefresh));
    }

    /**
     * Class to bundle all methods together that needs to be called to refresh the timer
     * @author Philipp
     */
    private class TimeEventContainer<SourceType> {
        private final Consumer<String> displayMethod;
        private final Function<SourceType, String> formatterMethod;
        private final Supplier<SourceType> getterMethod;

        TimeEventContainer(Consumer<String> displayMethod, Function<SourceType, String> formatterMethod, Supplier<SourceType> getterMethod) {
            this.displayMethod = displayMethod;
            this.formatterMethod = formatterMethod;
            this.getterMethod = getterMethod;
        }

        void execute() {
            displayMethod.accept(formatterMethod.apply(getterMethod.get()));
        }
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
