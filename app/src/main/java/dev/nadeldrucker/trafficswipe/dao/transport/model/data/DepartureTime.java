package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.Serializable;


/**
 * Class to bundle scheduled departure together with a delay
 */
public class DepartureTime implements Serializable, Comparable {

    private ZonedDateTime departure;
    private long delay;

    /**
     * use this constructor only if the api doesn't provide a delay
     *
     * @param departure scheduled departure
     */
    public DepartureTime(ZonedDateTime departure) {
        this(departure, 0);
    }

    /**
     * preferred constructor
     *
     * @param departure scheduled departure
     * @param delay     delay in seconds
     */
    public DepartureTime(ZonedDateTime departure, long delay) {

        this.departure = departure;
        this.delay = delay;
    }

    public ZonedDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(ZonedDateTime departure) {
        this.departure = departure;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
     * get expected departure
     *
     * @return departure including delay
     */
    public ZonedDateTime getActualDeparture() {
        return departure.plusSeconds(delay);
    }

    /**
     * Get the time between now and the actual departure
     *
     * @return formatted as readable String
     */
    public String getRemainingTime() {
        return getActualDeparture().minus(ZonedDateTime.now().toEpochSecond(), ChronoUnit.SECONDS).format(DateTimeFormatter.ofPattern("[HH:]mm:ss"));

    }

    /**
     * Required for sorted structures
     *
     * @param o must be DepartureTime
     */
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof DepartureTime)) throw new IllegalArgumentException("Can not compare");
        return getActualDeparture().compareTo(((DepartureTime) o).getActualDeparture());
    }
}
