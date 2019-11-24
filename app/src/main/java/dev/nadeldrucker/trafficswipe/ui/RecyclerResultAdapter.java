package dev.nadeldrucker.trafficswipe.ui;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

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
    private CountDownTimer mCountDownTimer;
    private ArrayList<TimeEventContainer> timeEvents = new ArrayList<>();
    private Handler handler = new Handler();


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
        timeEvents.add(new TimeEventContainer(holder.departureTime::setText, UiUtil::formatDuration, departureItem.departureTime::getRemainingTime));

    }

    /**
     * Class to bundle all methods together that needs to be called to refresh the timer
     */
    private class TimeEventContainer {
        private final Consumer<String> displayMethod;
        private final Function<Duration, String> formatterMethod;
        private final Supplier<Duration> getterMethod;

        public TimeEventContainer(Consumer<String> displayMethod, Function<Duration, String> formatterMethod, Supplier<Duration> getterMethod) {
            this.displayMethod = displayMethod;
            this.formatterMethod = formatterMethod;
            this.getterMethod = getterMethod;
        }

        public void execute() {
            displayMethod.accept(formatterMethod.apply(getterMethod.get()));
        }
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
