package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import com.android.volley.RequestQueue;

import java.time.ZonedDateTime;

/**
 * A departure from a specific station
 */
@Deprecated
public abstract class Departure extends TransportEntity {

    private Station departureStation;
    private String lineNumber;
    private ZonedDateTime departureTime;
    private String destination;

    public Departure(RequestQueue queue, Station departureStation, String destination, ZonedDateTime departureTime, String lineNumber) {
        super(queue);
        this.departureStation = departureStation;
        this.destination = destination;
        this.lineNumber = lineNumber;
        this.departureTime = departureTime;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(Station departureStation) {
        this.departureStation = departureStation;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ZonedDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}