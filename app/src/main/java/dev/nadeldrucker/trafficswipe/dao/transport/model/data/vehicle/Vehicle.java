package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.TransportEntity;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Superclass of all public transport data entities.
 */
public abstract class Vehicle extends TransportEntity {

    private String lineId;
    private String entityId;
    private String direction;
    private Map<Station, DepartureTime> stops;

    private final int[] lineColors = new int[]{
            R.color.colorTheme1a,
            R.color.colorTheme1b,
            R.color.colorTheme1c,
            R.color.colorTheme2a,
            R.color.colorTheme2b,
            R.color.colorTheme2c,
            R.color.colorTheme3a,
            R.color.colorTheme3b,
            R.color.colorTheme3c,
            R.color.colorTheme4a,
            R.color.colorTheme4b,
            R.color.colorTheme4c,
            R.color.colorTheme5a,
            R.color.colorTheme5b,
            R.color.colorTheme5c,
            R.color.colorTheme6a,
            R.color.colorTheme6b,
            R.color.colorTheme6c
    };

    /**
     * @param lineId             non-unique identifier for the specified transportation line
     * @param entityId           unique object identifier for later use, if provided by the api
     * @param stops              All stops and departures times from that station
     */
    public Vehicle(RequestQueue queue, @NonNull String lineId, @NonNull String direction, @Nullable String entityId, @NonNull Map<Station, DepartureTime> stops) {
        super(queue);
        this.lineId = lineId;
        this.entityId = entityId;
        this.stops = stops;
        this.direction = direction;
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
     * @return direction of the vehicle, e.g. text on the display
     */
    public String getDirection() {
        return direction;
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
     * Get duration to station
     *
     * @param toStation station where the vehicle is headed to
     * @return time until vehicle departure from the station, null if the stop doesn't exist
     */
    public Duration getTimeToStation(Station toStation) {
        return Duration.between(ZonedDateTime.now(), Objects.requireNonNull(stops.get(toStation)).getDepartureTimeWithDelay());
    }

    /**
     * @return icon resource id for this vehicle
     */
    public abstract int getIconDrawableResource();

    /**
     * returns the icon color for this line.
     * @return  line color resource id
     */
    public int getIconColor(){
        return lineColors[Math.abs(lineId.hashCode()) % lineColors.length];
    }
}
