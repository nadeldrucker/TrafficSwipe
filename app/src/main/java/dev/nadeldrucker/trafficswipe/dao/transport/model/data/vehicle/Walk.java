package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;

import org.threeten.bp.ZonedDateTime;

import java.util.TreeMap;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

@Deprecated
// See Issue 15
public class Walk extends AbstractVehicle {
    /**
     * @param targetDestination  where to stop the walk
     * @param scheduledDeparture scheduled start of walk
     */
    public Walk(RequestQueue queue, @NonNull TreeMap<DepartureTime, Station> targetDestination, @NonNull ZonedDateTime scheduledDeparture) {
        super(queue, "Walk", null, targetDestination, null);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }
}
