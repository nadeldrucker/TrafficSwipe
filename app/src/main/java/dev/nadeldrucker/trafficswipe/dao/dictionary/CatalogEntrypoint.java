package dev.nadeldrucker.trafficswipe.dao.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import dev.nadeldrucker.trafficswipe.dao.transport.apis.generic.TransportApiFactory;
import dev.nadeldrucker.trafficswipe.dao.transport.apis.vvo.VvoStation;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

public class CatalogEntrypoint {

    private HashMap<TransportApiFactory.ApiProvider, Catalog> apiProviderCatalogs;


    /**
     *  Returns a Catalog or creates one and returns it then
     * @param provider Api Provider
     * @return Catalog
     */
    public Catalog getCatalog(TransportApiFactory.ApiProvider provider){
        return apiProviderCatalogs.put(provider,apiProviderCatalogs.getOrDefault(provider,new Catalog(getDataSource(provider))));
    }

    /**
     * Returns a data source that creates all stations
     * @param provider ApiProvider
     * @return Method that generates all stations
     */
    private Supplier<ArrayList<Station>> getDataSource(TransportApiFactory.ApiProvider provider){
        switch (provider) {
            case VVO:
                return ArrayList::new; //TODO implement real data source
        }
        return null;
    }

}
