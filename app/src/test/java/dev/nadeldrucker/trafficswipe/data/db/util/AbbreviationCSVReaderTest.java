package dev.nadeldrucker.trafficswipe.data.db.util;

import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class AbbreviationCSVReaderTest {

    @Test
    public void readFromCsvFile() {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/abbreviations.csv");

        final Abbreviation[] abbreviationList = AbbreviationCSVReader.readAbbreviationsFromStream(inputStream);

        assertEquals("number of entries is not correct!", 11, abbreviationList.length);

        final Abbreviation firstEntry = abbreviationList[0];
        assertEquals("Aachener Straße", firstEntry.getName());
        assertEquals("AAC", firstEntry.getAbbreviation());

        final Abbreviation lastEntry = abbreviationList[10];
        assertEquals("Altdobritz", lastEntry.getName());
        assertEquals("ADB", lastEntry.getAbbreviation());
    }

}