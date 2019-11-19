package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;

import java.util.TreeMap;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

public class SBahn extends AbstractVehicle {
    /**
     * @param lineId             non-unique identifier for the specified transportation line
     * @param entityId           unique object identifier for later use, if provided by the api
     * @param targetDestination  destination as on the sign of the transportation
     */
    public SBahn(RequestQueue queue, @NonNull String lineId, @Nullable String entityId, @NonNull TreeMap<DepartureTime, Station> targetDestination) {
        super(queue, lineId, entityId, targetDestination, null);
    }

    @Override
    public Drawable getIcon() {
        return uiElement.adjustColor(uiElement.getCIRCLE());
    }
}
