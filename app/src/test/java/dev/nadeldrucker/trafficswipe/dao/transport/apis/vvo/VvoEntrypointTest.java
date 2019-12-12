package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.util.api.AbstractVolleyMockApiTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VvoEntrypointTest extends AbstractVolleyMockApiTest {

    private VvoEntrypoint vvoEntrypoint;

    @Before
    public void before() throws ExecutionException, InterruptedException {
        super.before();
        vvoEntrypoint = new VvoEntrypoint(requestQueue);
    }

    /**
     * Tests if the api returns the correctly transformed response on searching for stations.
     */
    @Test
    public void getStops() throws ExecutionException, InterruptedException {
        mockHttpStack.queueNextResponse("mocks/vvo/GETFindStations.json");
        List<Station> stations = vvoEntrypoint.getStops("NUP").get();
        Assert.assertEquals("Nürnberger Platz", stations.get(0).getName());
        Assert.assertEquals("Bautzner Straße / Rothenburger Straße", stations.get(1).getName());
    }
}