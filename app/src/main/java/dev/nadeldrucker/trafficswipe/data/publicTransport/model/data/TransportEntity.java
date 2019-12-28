package dev.nadeldrucker.trafficswipe.data.publicTransport.model.data;

import com.android.volley.RequestQueue;

import java.io.Serializable;

/**
 * Superclass of all public transport data entities.
 */
public abstract class TransportEntity implements Serializable {

    private RequestQueue queue;

    public TransportEntity(RequestQueue queue) {
        this.queue = queue;
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
