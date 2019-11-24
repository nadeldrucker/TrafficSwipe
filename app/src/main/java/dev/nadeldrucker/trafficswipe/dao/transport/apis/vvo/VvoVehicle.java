package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;

import java.util.Map;

public class VvoVehicle extends Vehicle {

    /**
     * @param queue
     * @param lineId   non-unique identifier for the specified transportation line
     * @param entityId unique object identifier for later use, if provided by the api
     * @param stops    All stops and departures times from that station
     */
    public VvoVehicle(RequestQueue queue, @NonNull String lineId, @Nullable String entityId, @NonNull Map<Station, DepartureTime> stops) {
        super(queue, lineId, entityId, stops);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }
}
