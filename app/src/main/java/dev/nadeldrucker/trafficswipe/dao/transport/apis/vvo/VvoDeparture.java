package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import com.android.volley.RequestQueue;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Departure;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Date;
import java.util.Optional;

public class VvoDeparture extends Departure {

    public VvoDeparture(RequestQueue queue, Station departureStation, String destinationStation, String lineNumber, ZonedDateTime departureTime) {
        super(queue, departureStation, destinationStation, departureTime, lineNumber);
    }

    public static VvoDeparture fromJVVODeparture(RequestQueue queue, Station station, dev.nadeldrucker.jvvo.Models.Departure departure){
        Date d = Optional.ofNullable(departure.getRealTime()).orElse(departure.getScheduledTime());

        // FIXME maybe use optionals? or builder?
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(d.getTime()), ZoneId.of("UTC"));

        return new VvoDeparture(queue, station, departure.getDirection(), departure.getLine(), dateTime);
    }
}
