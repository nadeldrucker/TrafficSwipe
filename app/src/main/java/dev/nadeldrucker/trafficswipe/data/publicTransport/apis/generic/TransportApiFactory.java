package dev.nadeldrucker.trafficswipe.data.publicTransport.apis.generic;

import android.util.Log;

import com.android.volley.RequestQueue;

import dev.nadeldrucker.trafficswipe.data.publicTransport.apis.vvo.VvoEntrypoint;

public class TransportApiFactory {

    private static final String TAG = TransportApiFactory.class.getName();

    public enum ApiProvider {
        VVO("VVO/DVB");

        private String name;

        ApiProvider(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Creates a new api dao for a specific api provider
     * @param apiProvider api provider
     * @return new api dao
     */
    public static Entrypoint createTransportApiDao(ApiProvider apiProvider, RequestQueue queue){
        switch (apiProvider) {
            case VVO:
                Log.d(TAG, "createTransportApiDao: " + apiProvider.toString());
                return new VvoEntrypoint(queue);
        }

        throw new IllegalArgumentException("ApiProvider has no implementation!");
    }

}
