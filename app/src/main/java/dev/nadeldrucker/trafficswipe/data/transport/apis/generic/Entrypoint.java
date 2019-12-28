package dev.nadeldrucker.trafficswipe.data.transport.apis.generic;

import com.android.volley.RequestQueue;

/**
 * Entrypoint for api, exposing operations.
 */
public abstract class Entrypoint implements Capabilities {

    protected RequestQueue queue;
    public Entrypoint(RequestQueue queue){
        this.queue = queue;
    }

}
