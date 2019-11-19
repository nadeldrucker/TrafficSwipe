package dev.nadeldrucker.trafficswipe.dao.transport.model.data;

import com.android.volley.RequestQueue;

import java.io.Serializable;

/**
 * Superclass of all public transport data entities.
 */
public abstract class AbstractTransportEntity implements Serializable {

    private RequestQueue queue;

    public AbstractTransportEntity(RequestQueue queue) {
        this.queue = queue;
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
