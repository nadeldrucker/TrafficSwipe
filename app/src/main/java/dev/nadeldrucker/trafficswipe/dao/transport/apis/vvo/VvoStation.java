package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import dev.nadeldrucker.jvvo.Models.Stop;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Location;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

public class VvoStation extends Station {

    public VvoStation(String name, Location location) {
        super(name, location);
    }

    /**
     * Creates new {@link VvoStation} from jVVO {@link Stop}
     * @param stop stop to use
     * @return station
     */
    public static VvoStation fromJVVOStop(Stop stop){
        return new VvoStation(stop.getName(), new Location(stop.getLocation().getLatitude(), stop.getLocation().getLongitude()));
    }
}
