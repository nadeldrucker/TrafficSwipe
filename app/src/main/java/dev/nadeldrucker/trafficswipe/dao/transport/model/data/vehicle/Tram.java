package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;

import java.sql.Timestamp;
import java.time.Duration;

public class Tram extends AbstractVehicle {
    /**
     * @param lineId             non-unique identifier for the specified transportation line
     * @param entityId           unique object identifier for later use, if provided by the api
     * @param targetDestination  destination as on the sign of the transportation
     * @param scheduledDeparture departure without delay
     * @param delay              zero, but not null, if there is no delay
     */
    public Tram(RequestQueue queue, @NonNull String lineId, @Nullable String entityId, @NonNull String targetDestination, @NonNull Timestamp scheduledDeparture, @NonNull Duration delay) {
        super(queue, lineId, entityId, targetDestination, scheduledDeparture, delay);
    }

    @Override
    public Drawable getIcon() {
        return uiElement.adjustColor(uiElement.getCIRCLE(), StaticUiElement.hash(getLineId()));
    }
}
