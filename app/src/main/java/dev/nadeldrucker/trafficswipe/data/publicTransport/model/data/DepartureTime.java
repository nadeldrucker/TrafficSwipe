package dev.nadeldrucker.trafficswipe.data.publicTransport.model.data;

import androidx.annotation.Nullable;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;


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
     * get expected departure inclufing delay
     *
     * @return departure including delay
     */
    public ZonedDateTime getDepartureTimeWithDelay() {
        ZonedDateTime dateTime = departure;
        if (delay != null) {
            dateTime = dateTime.plus(delay);
        }

        return dateTime;
    }

    /**
     * Get the time between now and the actual departure
     *
     * @return duration until departure
     */
    public Duration getRemainingTime() {
        return Duration.between(ZonedDateTime.now(), getDepartureTimeWithDelay());
    }

    @Override
    public int compareTo(DepartureTime o) {
        return getDepartureTimeWithDelay().compareTo(o.getDepartureTimeWithDelay());
    }
}
