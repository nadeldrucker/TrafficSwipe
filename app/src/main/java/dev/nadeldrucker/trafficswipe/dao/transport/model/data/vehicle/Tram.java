package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

import java.util.Map;

public class Tram extends Vehicle {
    /**
     * @param lineId             non-unique identifier for the specified transportation line
     * @param entityId           unique object identifier for later use, if provided by the api
     * @param targetDestination  destination as on the sign of the transportation
     */
    public Tram(RequestQueue queue, @NonNull String lineId, @Nullable String entityId, @NonNull Map<Station, DepartureTime> targetDestination) {
        super(queue, lineId, entityId, targetDestination);
    }

    @Override
    public int getIconDrawableResource() {
        return R.drawable.circle;
    }
}
