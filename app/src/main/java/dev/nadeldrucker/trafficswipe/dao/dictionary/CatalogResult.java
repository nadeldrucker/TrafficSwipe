package dev.nadeldrucker.trafficswipe.dao.dictionary;



/*
less than zero means error
 */
public enum CatalogResult {
    SUCCESS(0),
    ABBREVIATION_ALREADY_EXISTS(-1),
    STATION_ALREADY_EXISTS_WITH_DIFFERENT_NAME(-2),
    REDUNDANT_OPERATION_SKIPPED(1)
    ;

    private final int value;

    CatalogResult(int value) {
        this.value=value;
    }

    public int getValue(){
        return value;
    }
}
