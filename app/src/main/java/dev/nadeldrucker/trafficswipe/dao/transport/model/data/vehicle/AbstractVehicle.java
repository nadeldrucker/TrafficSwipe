package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.sql.Timestamp;
import java.time.Duration;

import com.android.volley.RequestQueue;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.AbstractTransportEntity;


/**
 * Superclass of all public transport data entities.
 */
public abstract class AbstractVehicle extends AbstractTransportEntity {

    private String lineId;
    private String entityId;
    private String targetDestination;
    private Timestamp scheduledDeparture;
    private Duration delay;
    final StaticUiElement uiElement=StaticUiElement.getInstance();

    /**
     * @param lineId             non-unique identifier for the specified transportation line
     * @param entityId           unique object identifier for later use, if provided by the api
     * @param targetDestination  destination as on the sign of the transportation
     * @param scheduledDeparture departure without delay
     * @param delay              zero, but not null, if there is no delay; null if api doesn't provide delay
     */
    public AbstractVehicle(RequestQueue queue,  @NonNull String lineId, @Nullable String entityId, @NonNull String targetDestination, @NonNull Timestamp scheduledDeparture, @Nullable Duration delay) {
        super(queue);
        this.lineId = lineId;
        this.entityId = entityId;
        this.targetDestination = targetDestination;
        this.scheduledDeparture = scheduledDeparture;
        this.delay = delay;
    }

    public String getLineId() {
        return lineId;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getTargetDestination() {
        return targetDestination;
    }

    public Timestamp getScheduledDeparture() {
        return scheduledDeparture;
    }

    public Timestamp getActualDeparture() {
        return new Timestamp(scheduledDeparture.getTime() + delay.toMillis()); // STOPSHIP: 16.11.2019 API version incompatible
    }


    public Duration getDelay() {
        return delay;
    }

    /**
     * @return icon for this line
     */
    public abstract Drawable getIcon();

    //imageView.setBackground(StaticUiElement.getInstance().adjustColor(ContextCompat.getDrawable(this,R.drawable.circle)));
    //imageView.setBackground(StaticUiElement.getInstance().adjustColor(ContextCompat.getDrawable(this,R.drawable.square)));

}
