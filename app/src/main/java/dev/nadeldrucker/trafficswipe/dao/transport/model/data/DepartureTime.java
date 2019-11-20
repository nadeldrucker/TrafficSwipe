package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import com.android.volley.RequestQueue;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.Serializable;


/**
 * Class to bundle scheduled departure together with a delay
 */
public class DepartureTime extends AbstractTransportEntity implements Comparable<DepartureTime> {

    private ZonedDateTime departure;
    private long delay;

    /**
     * preferred constructor
     *
     * @param departure scheduled departure
     * @param delay     delay in seconds
     */
    public DepartureTime(RequestQueue queue, ZonedDateTime departure, long delay) {
        super(queue);
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

    @Override
    public int compareTo(DepartureTime o) {
        return getActualDeparture().compareTo(o.getActualDeparture());
    }
}
