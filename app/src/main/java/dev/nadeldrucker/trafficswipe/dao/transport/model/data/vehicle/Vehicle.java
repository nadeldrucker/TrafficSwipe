package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import java.util.Map;
import java.util.Optional;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.TransportEntity;
import dev.nadeldrucker.trafficswipe.logic.VehicleUiSupport;


/**
 * Superclass of all public transport data entities.
 */
public abstract class Vehicle extends TransportEntity {

    private String lineId;
    private String entityId;
    final VehicleUiSupport uiElement = VehicleUiSupport.getInstance();
    private Map<Station, DepartureTime> stops;

    /**
     * @param lineId             non-unique identifier for the specified transportation line
     * @param entityId           unique object identifier for later use, if provided by the api
     * @param stops              All stops and departures times from that station
     */
    public Vehicle(RequestQueue queue, @NonNull String lineId, @Nullable String entityId, @NonNull Map<Station, DepartureTime> stops) {
        super(queue);
        this.lineId = lineId;
        this.entityId = entityId;
        this.stops = stops;
    }

    /**
     * @return non-unique identifier for the specified transportation line
     */
    public String getLineId() {
        return lineId;
    }

    /**
     * Currently not in use
     *
     * @return unique value provided by the api to track or identify this vehicle
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Get the last stop as Station object.
     *
     * @return last Stop as Station
     */
    public Station getFinalDestination() {
        Optional<Map.Entry<Station, DepartureTime>> reduced = stops.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .reduce((a, b) -> b);

        return reduced.map(Map.Entry::getKey).orElse(null);
    }

    /**
     * Get a formatted String to show waiting time on gui
     *
     * @param toStation station where the vehicle is headed to
     * @return time until vehicle departure from the station
     */
    public Duration getTimeToStation(Station toStation) {
        return Duration.between(ZonedDateTime.now(), stops.get(toStation).getActualDeparture());
    }

    /**
     * @return icon for this vehicle
     */
    public abstract Drawable getIcon();

}
