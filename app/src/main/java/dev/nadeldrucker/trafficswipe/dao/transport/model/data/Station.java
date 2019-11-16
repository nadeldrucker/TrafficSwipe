package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

/**
 * Represents a station in the public transport network.
 */
public class Station extends AbstractTransportEntity {
    private String name;
    private String shortage;
    private Location location;

    public Station(String name, Location location, String shortage) {
        this.name = name;
        this.shortage = shortage;
        this.location = location;
    }

    public Station(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getShortage() {
        return shortage;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }


}
