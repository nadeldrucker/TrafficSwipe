package dev.nadeldrucker.trafficswipe.data.db.util;

import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for reading csv
 */
public abstract class CSVReader<T> {

    private final boolean ignoreFirstLine;

    public CSVReader(boolean ignoreFirstLine) {
        this.ignoreFirstLine = ignoreFirstLine;
    }

    /**
     * Reads a csv file into an array.
     * @param inputStream {@link InputStream} to read from
     * @return array of {@link Abbreviation}
     */
    public List<T> readFromStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            if (ignoreFirstLine) {
                reader.readLine();
            }

            return reader.lines()
                    .map(this::readCSVLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed while reading csv!\n" + e.toString());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read a single line from the csv
     * @param line line as string
     * @return parsed element
     */
    protected abstract T readCSVLine(String line);

}
