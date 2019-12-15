package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;

import dev.nadeldrucker.trafficswipe.R;
import org.threeten.bp.ZonedDateTime;

import java.util.TreeMap;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

@Deprecated
// See Issue 15
public class Walk extends Vehicle {
    /**
     * @param targetDestination  where to stop the walk
     * @param scheduledDeparture scheduled start of walk
     */
    public Walk(RequestQueue queue, @NonNull TreeMap<Station, DepartureTime> targetDestination, @NonNull ZonedDateTime scheduledDeparture) {
        super(queue, "Walk", "Walking", "walkingId", targetDestination);
    }

    @Override
    public int getIconDrawableResource() {
        return -1;
    }
}
