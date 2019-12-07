package dev.nadeldrucker.trafficswipe.dao.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Location;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.Station;

public class Catalog<T extends Station> implements Serializable {
    final int ABBREVIATION_LENGTH = 3;
    private HashMap<char[],T> abbreviationDictionary;
    private HashMap<String,T> fullNameDictionary;
    private HashMap<Location,T> geoDictionary;


    /**
     * Adds all stations to list
     * @param dataSource data provider
     */
    Catalog(Supplier<ArrayList<T>> dataSource) {
        dataSource.get().forEach(this::add);
    }


    /**
     * Get the Station that fits to the abbreviation
     * @param abbreviation array with length matching to const ABBREVIATION_LENGTH
     * @return matching station or null
     */
    public T getStation(char[] abbreviation){
        if (abbreviation.length!=ABBREVIATION_LENGTH) throw new IllegalArgumentException("Abbreviation length must be exactly "+ABBREVIATION_LENGTH);
        return abbreviationDictionary.get(abbreviation);
    }

    /**
     * Get the Station with an equal name
     * @param fullName name
     * @return station
     */
    public T getStation(String fullName){
        Objects.requireNonNull(fullName);
        return fullNameDictionary.get(fullName);
    }

    /**
     * Get the Station with an equal location
     * @param location location
     * @return station
     */
    public T getStation(Location location){
        Objects.requireNonNull(location);
        return geoDictionary.get(location);
    }


    /**
     * Find out if an Abbreviation is user-generated
     * @param station station to check
     * @return true if the user created an abbreviation, else false
     */
    public boolean isUserGeneratedAbbreviation(T station){
        for (Map.Entry<char[], T> tEntry : abbreviationDictionary.entrySet()) {
            if (tEntry.getValue().equals(station))
                return tEntry.getKey() == tEntry.getValue().getAbbreviation();
        }
        return false;
    }

    /**
     * adds an entry to the catalog. It's important to keep the data consistent.
     * @param station station to add
     * @return error code
     */
    public CatalogResult add(T station){
        //TODO Implement checks

        //T sameAbbreviation = abbreviationDictionary.get(station.getAbbreviation());
        //if (sameAbbreviation !=null)

        fullNameDictionary.put(station.getName(),station);
        geoDictionary.put(station.getLocation(),station);
        abbreviationDictionary.put(station.getAbbreviation(), station);
        return CatalogResult.SUCCESS;

    }





}
