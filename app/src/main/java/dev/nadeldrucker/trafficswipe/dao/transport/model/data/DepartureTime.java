package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;


/**
 * Class to bundle scheduled departure together with a delay
 */
public class DepartureTime implements Comparable<DepartureTime> {

    private ZonedDateTime departure;
    private Duration delay;

    /**
     * preferred constructor
     *
     * @param departure scheduled departure
     * @param delay     delay in seconds
     */
    public DepartureTime(ZonedDateTime departure, @Nullable Duration delay) {
        this.departure = departure;
        this.delay = delay;
    }

    public ZonedDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(ZonedDateTime departure) {
        this.departure = departure;
    }

    public Duration getDelay() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    /**
     * get expected departure
     *
     * @return departure including delay
     */
    public ZonedDateTime getActualDeparture() {
        ZonedDateTime dateTime = departure;
        if (delay != null) {
            dateTime.plus(delay);
        }

        return dateTime;
    }

    /**
     * Get the time between now and the actual departure
     *
     * @return duration until departure
     */
    public Duration getRemainingTime() {
        return Duration.between(ZonedDateTime.now(), getActualDeparture());
    }

    @Override
    public int compareTo(DepartureTime o) {
        return getActualDeparture().compareTo(o.getActualDeparture());
    }
}
