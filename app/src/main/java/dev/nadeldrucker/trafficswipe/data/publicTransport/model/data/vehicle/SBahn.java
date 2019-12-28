package dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.vehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;

import java.util.Map;

import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.data.publicTransport.model.data.Station;

public class SBahn extends Vehicle {

    /**
     * @param lineId            non-unique identifier for the specified transportation line
     * @param direction         direction of vehicle as seen on the display
     * @param entityId          unique object identifier for later use, if provided by the api
     * @param targetDestination destination as on the sign of the transportation
     */
    public SBahn(RequestQueue queue, @NonNull String lineId, @NonNull String direction, @Nullable String entityId, @NonNull Map<Station, DepartureTime> targetDestination) {
        super(queue, lineId, direction, entityId, targetDestination);
    }

    @Override
    public int getIconDrawableResource() {
        return R.drawable.circle;
    }
}
