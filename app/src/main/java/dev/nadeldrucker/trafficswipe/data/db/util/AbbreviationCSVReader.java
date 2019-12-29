package dev.nadeldrucker.trafficswipe.data.db.util;

import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AbbreviationCSVReader extends CSVReader<Abbreviation>{

    public AbbreviationCSVReader() {
        super(false);
    }

    @Override
    protected Abbreviation readCSVLine(String line) {
        final String[] contents = line.split(";");

        if (contents.length != 2) throw new IllegalStateException("Line with less than 2 entries!");

        return new Abbreviation(contents[1], contents[0]);
    }

}
