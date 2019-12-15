package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import androidx.lifecycle.LiveData;
import dev.nadeldrucker.trafficswipe.dao.transport.apis.generic.DataWrapper;
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
    public void getStops() {
        mockHttpStack.queueNextResponse("mocks/vvo/GETFindStations.json");
        LiveData<DataWrapper<List<Station>>> liveStations = vvoEntrypoint.getStops("NUP");
        waitForLiveData(liveStations, stations -> stations.evaluate(data -> {
                    Assert.assertEquals("Nürnberger Platz", data.get(0).getName());
                    Assert.assertEquals("Bautzner Straße / Rothenburger Straße", data.get(1).getName());
                }, error -> Assert.fail())
        );
    }
}