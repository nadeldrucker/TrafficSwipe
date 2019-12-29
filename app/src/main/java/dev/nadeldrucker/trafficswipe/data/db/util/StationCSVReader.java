package dev.nadeldrucker.trafficswipe.data.db.util;

import dev.nadeldrucker.trafficswipe.data.db.entities.Station;

public class StationCSVReader extends CSVReader<Station> {

    public StationCSVReader() {
        super(true);
    }

    @Override
    protected Station readCSVLine(String line) {
        final String[] elements = line.split(";");

        String id = elements[0];
        String nameLong = elements[1];
        String nameShort = elements[2];
        String city = elements[3];
        double wgsX = parseDoubleFromString(elements[7]);
        double wgsY = parseDoubleFromString(elements[8]);

        return new Station(id, nameLong, nameShort, city, wgsX, wgsY);
    }

    private double parseDoubleFromString(String s) {
        return Double.parseDouble(s.replace(',', '.'));
    }

}
