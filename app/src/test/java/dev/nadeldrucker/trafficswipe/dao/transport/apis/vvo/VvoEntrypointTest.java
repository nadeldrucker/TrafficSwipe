package dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;
import dev.nadeldrucker.trafficswipe.util.api.AbstractVolleyMockApiTest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VvoEntrypointTest extends AbstractVolleyMockApiTest {

    @Before
    public void before() throws ExecutionException, InterruptedException {
        VvoEntrypoint vvoEntrypoint = new VvoEntrypoint(requestQueue);
        List<Station> nup = vvoEntrypoint.getStops("nup").get();
    }

    @Test
    public void getStops() {
        // TODO use file provided in resources
    }
}