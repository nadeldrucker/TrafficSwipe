package dev.nadeldrucker.trafficswipe.util.api;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import com.android.volley.ExecutorDelivery;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.NoCache;
import dev.nadeldrucker.trafficswipe.dao.transport.apis.generic.DataWrapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

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
    public MockHttpStack mockHttpStack;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void before() throws ExecutionException, InterruptedException {
        mockHttpStack = new MockHttpStack();
        requestQueue = new RequestQueue(new NoCache(), new BasicNetwork(mockHttpStack), 1, new ExecutorDelivery(new InstantExecutor()));
        requestQueue.start();
    }

    @After
    public void after() {
        requestQueue.stop();
    }

    /**
     * Helper method waiting for a live data object to change state.
     * @param liveData live data object to observe
     * @param resultConsumer consumer called when {@link LiveData} notifies observers
     * @param <T> type of live data
     */
    public <T> void waitForLiveData(LiveData<T> liveData, Consumer<T> resultConsumer) {
        CountDownLatch latch = new CountDownLatch(1);

        liveData.observeForever(value -> {
            latch.countDown();
            resultConsumer.accept(value);
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method waiting for live data object to change, fails on error.
     * @param liveData live data object to observe
     * @param dataConsumer consumer called when {@link LiveData} notifies observers, <b>triggered only on success</b>
     * @param <T> type of live data
     */
    public <T> void waitForWrappedLiveData(LiveData<DataWrapper<T>> liveData, Consumer<T> dataConsumer) {
        waitForLiveData(liveData, tDataWrapper -> tDataWrapper.evaluate(
                dataConsumer,
                error -> Assert.fail()
        ));
    }

}
