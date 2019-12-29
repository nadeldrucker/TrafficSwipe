package dev.nadeldrucker.trafficswipe.data.db.util;

import dev.nadeldrucker.trafficswipe.data.db.entities.Station;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class StationCSVReaderTest {

    @Test
    public void readFromCsvFile() {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/stations.csv");

        final List<Station> stationList = new StationCSVReader().readFromStream(inputStream);

        assertEquals("number of entries is not correct!", 8, stationList.size());

        final Station s = stationList.get(0);
        assertEquals("de:14612:1", s.id);
        assertEquals("Dresden Bahnhof Mitte", s.fullName);
        assertEquals("Bahnhof Mitte", s.shortName);
        assertEquals("Dresden", s.city);
        assertEquals(13.723395, s.latitude, 0.0);
        assertEquals(51.055642, s.longitude, 0.0);
    }

}
