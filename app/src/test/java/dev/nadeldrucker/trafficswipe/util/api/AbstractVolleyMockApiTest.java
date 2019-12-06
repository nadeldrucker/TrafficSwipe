package dev.nadeldrucker.trafficswipe.util.api;

import com.android.volley.ExecutorDelivery;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.NoCache;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.ExecutionException;

/**
 * Test support for testing api implementations using Volley.<br>
 * <ul>
 *     <li>RequestQueue to use for apis</li>
 *     <li>Responses in the queue can be mocked using mock files</li>
 *     <li>Queue executes requests instantly, doesn't need android dependency (for handler in executor)</li>
 * </ul>
 */
public abstract class AbstractVolleyMockApiTest {

    public RequestQueue requestQueue;

    @Before
    public void before() throws ExecutionException, InterruptedException {
        requestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new MockHttpStack()), 1, new ExecutorDelivery(new InstantExecutor()));
        requestQueue.start();
    }

    @After
    public void after() {
        requestQueue.stop();
    }

}
