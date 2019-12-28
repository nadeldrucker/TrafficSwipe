package dev.nadeldrucker.trafficswipe.data.db.util;

import dev.nadeldrucker.trafficswipe.data.db.entities.Abbreviation;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbbreviationCSVReaderTest {

    @Test
    public void readFromCsvFile() {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/abbreviations.csv");

        final List<Abbreviation> abbreviationList = AbbreviationCSVReader.readAbbreviationsFromStream(inputStream);

        assertEquals("number of entries is not correct!", 11, abbreviationList.size());

        final Abbreviation firstEntry = abbreviationList.get(0);
        assertEquals("Aachener Straße", firstEntry.getName());
        assertEquals("AAC", firstEntry.getAbbreviation());

        final Abbreviation lastEntry = abbreviationList.get(10);
        assertEquals("Altdobritz", lastEntry.getName());
        assertEquals("ADB", lastEntry.getAbbreviation());
    }

}