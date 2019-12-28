package dev.nadeldrucker.trafficswipe.data.transport.apis.vvo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;

import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.nadeldrucker.jvvo.Models.Departure;
import dev.nadeldrucker.jvvo.Models.Mode;
import dev.nadeldrucker.jvvo.Models.Stop;
import dev.nadeldrucker.trafficswipe.data.transport.apis.generic.DataWrapper;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.Location;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.vehicle.Bus;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.vehicle.SBahn;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.vehicle.Tram;
import dev.nadeldrucker.trafficswipe.data.transport.model.data.vehicle.Vehicle;

public class VvoStation extends Station {

    private String stopId;
    private static final ZoneId VVO_ZONE = ZoneId.of("Europe/Berlin");

    public VvoStation(@NonNull RequestQueue queue, @NonNull String name, @Nullable Location location, @Nullable String stopId) {
        super(queue, name, location);
        this.stopId = stopId;
    }

    /**
     * Creates new {@link VvoStation} from jVVO {@link Stop}
     *
     * @param stop stop to use
     * @return station
     */
    public static VvoStation fromJVVOStop(RequestQueue queue, Stop stop) {
        if (stop == null || stop.getLocation() == null) return null;
        return new VvoStation(queue, stop.getName(),
                new Location(stop.getLocation().getLatitude(), stop.getLocation().getLongitude()),
                stop.getId());
    }

    @Override
    public LiveData<DataWrapper<Map<Vehicle, DepartureTime>>> getDepartures() {
        MutableLiveData<DataWrapper<Map<Vehicle, DepartureTime>>> liveData = new MutableLiveData<>();

        dev.nadeldrucker.jvvo.Models.Departure.monitor(stopId, new Date(), Departure.DateType.departure, Mode.getAll(), true, getQueue(), response -> {
            if (response.getResponse().isPresent()) {
                HashMap<Vehicle, DepartureTime> vehicleDepartures = new HashMap<>();

                // for every departure create an entry in the vehicle departure map
                response.getResponse().get().getDepartures()
                        .forEach(departure -> {
                            if (departure != null) {
                                ZonedDateTime scheduledDepartureTime = Instant.ofEpochMilli(departure.getScheduledTime().getTime()).atZone(VVO_ZONE);

                                Duration d = null;
                                if (departure.getRealTime() != null) {
                                    ZonedDateTime actualDeparture = Instant.ofEpochMilli(departure.getRealTime().getTime()).atZone(VVO_ZONE);
                                    d = Duration.between(scheduledDepartureTime, actualDeparture);
                                }

                                DepartureTime departureTime = new DepartureTime(scheduledDepartureTime, d);

                                // stations the vehicle stops at
                                Map<Station, DepartureTime> stops = new HashMap<>();

                                // add this station to the vehicles stops
                                stops.put(this, departureTime);

                                // add final station to the vehicles stops (use max time, making it the last station)
                                DepartureTime finalDepartureTime = new DepartureTime(Instant.ofEpochMilli(Long.MAX_VALUE).atZone(VVO_ZONE), Duration.ofSeconds(0));
                                stops.put(new VvoStation(getQueue(), departure.getDirection(), null, null), finalDepartureTime);

                                // create vehicle
                            /*
                            I think if departure.diva.number starts with 1 it's a tram
                                                                         2        bus
                             */
                                Vehicle v;
                                switch (departure.getDiva().getNumber().charAt(0)) {
                                    case '1':
                                        v = new Tram(getQueue(), departure.getLine(), departure.getDirection(), departure.getId(), stops);
                                        break;
                                    case '2':
                                        v = new Bus(getQueue(), departure.getLine(), departure.getDirection(), departure.getId(), stops);
                                        break;
                                    default:
                                        //FIXME for now, the Sbahn Icon is shown whenever we're unable to parse vehicle type
                                        v = new SBahn(getQueue(), departure.getLine(), departure.getDirection(), departure.getId(), stops);

                                }

                                // add vehicle with departure time to vehicle map
                                vehicleDepartures.put(v, departureTime);
                            }
                        });
                liveData.postValue(DataWrapper.createOfData(vehicleDepartures));
            } else if (response.getError().isPresent()) {
                liveData.postValue(DataWrapper.createOfError(DataWrapper.ErrorType.NETWORK_ERROR, "Couldn't complete request! " + response.getError().get().getDescription()));
            }
        });
        return liveData;
    }
}
