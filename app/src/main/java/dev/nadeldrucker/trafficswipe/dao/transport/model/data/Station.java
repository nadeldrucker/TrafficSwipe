package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

/**
 * Represents a station in the public transport network.
 */
public abstract class Station extends AbstractTransportEntity {
    private String name;
    private Location location;

    public Station(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
