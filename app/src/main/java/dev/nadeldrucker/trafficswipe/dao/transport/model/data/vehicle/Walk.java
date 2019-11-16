package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class Walk extends AbstractVehicle {
    /**
     * @param targetDestination  where to stop the walk
     * @param scheduledDeparture scheduled start of walk
     */
    public Walk(@NonNull String targetDestination, @NonNull Timestamp scheduledDeparture) {
        super("Walk", null, targetDestination, scheduledDeparture, null);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }
}
