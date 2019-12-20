package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.DepartureTime;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Tram;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.Vehicle;
import dev.nadeldrucker.trafficswipe.util.api.AbstractVolleyMockApiTest;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VvoStationTest extends AbstractVolleyMockApiTest {

    private VvoStation vvoStation;

    @Before
    public void before() throws ExecutionException, InterruptedException {
        super.before();
        vvoStation = new VvoStation(requestQueue, "Hauptbahnhof", null, "33000028");
    }

    /**
     * Tests if the api returns the correctly transformed response on searching for stations.
     */
    @Test
    public void getStops() throws ExecutionException, InterruptedException {
        mockHttpStack.queueNextResponse("mocks/vvo/GETDepartures.json");

        waitForWrappedLiveData(vvoStation.getDepartures(), vehicleDepartureTimeMap -> {
            List<Map.Entry<Vehicle, DepartureTime>> list = vehicleDepartureTimeMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toList());

            // check 8 direction gorbitz (first entry)
            Map.Entry<Vehicle, DepartureTime> gorbitz8 = list.get(0);
            assertEquals("voe:11008: :H:j19", gorbitz8.getKey().getEntityId());
            assertEquals(LocalDateTime.of(2019, 12, 12, 21, 38, 0), gorbitz8.getValue().getDepartureTimeWithDelay().toLocalDateTime());
            assertEquals(Duration.of(0, ChronoUnit.SECONDS), gorbitz8.getValue().getDelay());
            assertTrue(gorbitz8.getKey() instanceof Tram);
            assertEquals("8", gorbitz8.getKey().getLineId());

            // check a departure that is delayed
            Map.Entry<Vehicle, DepartureTime> wilderMann3 = list.stream()
                    .filter(entry -> entry.getKey().getEntityId().equals("voe:11003: :H:j19"))
                    .findAny()
                    .orElseThrow(RuntimeException::new);
            assertEquals("voe:11003: :H:j19", wilderMann3.getKey().getEntityId());
            assertEquals("3", wilderMann3.getKey().getLineId());
            assertTrue(wilderMann3.getKey() instanceof Tram);
            assertEquals(LocalDateTime.of(2019, 12, 12, 21, 44, 0), wilderMann3.getValue().getDeparture().toLocalDateTime());
            assertEquals(Duration.of(1, ChronoUnit.MINUTES), wilderMann3.getValue().getDelay());
            assertEquals(LocalDateTime.of(2019, 12, 12, 21, 45, 0), wilderMann3.getValue().getDepartureTimeWithDelay().toLocalDateTime());
        });
    }


}