package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;

import java.util.SortedMap;
import java.util.TreeMap;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.TransportEntity;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Location;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;


/**
 * Superclass of all public transport data entities.
 */
public abstract class Vehicle extends TransportEntity {

    private String lineId;
    private String entityId;
    private String targetDestination;
    final StaticUiElement uiElement=StaticUiElement.getInstance();
    private SortedMap<DepartureTime, Station> stops;

    /**
     * @param lineId             non-unique identifier for the specified transportation line
     * @param entityId           unique object identifier for later use, if provided by the api
     * @param stops              All stops
     * @param targetDestination  destination as on the sign of the transportation, if null, last stop will be taken
     */
    public Vehicle(RequestQueue queue, @NonNull String lineId, @Nullable String entityId, @NonNull TreeMap<DepartureTime, Station> stops, String targetDestination) {
        super(queue);
        this.lineId = lineId;
        this.entityId = entityId;
        this.stops = stops;
        this.targetDestination = targetDestination;



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
     * Do not create a String out of this to show the user the destination
     * use getDestinationSting() instead
     *
     * @return last Stop as Station
     */
    public Station getTargetDestination() {
        return stops.get(stops.lastKey());
    }

    /**
     * Get a formatted String to show waiting time on gui
     *
     * @param location where the user wants to board
     * @return time until vehicle departures from location
     */
    public String getRemainingTime(Location location) {
        return "TODO implement me"; //Todo

    }

    /**
     * Get either an explicit DestinationString or the name of the last stop
     *
     * @return Name of Destination
     */
    public String getDestinationString() {
        if (targetDestination != null) return targetDestination;
        return stops.get(stops.lastKey()).getName();
    }


    /**
     * @return icon for this vehicle
     */
    public abstract Drawable getIcon();

}
