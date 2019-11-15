package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class StaticUiElementTest {
    private String[] lines = {"360", "66E", "66", "63", "36", "E66"};

    @Test
    public void hashDifferent() {
        ArrayList<Integer> result = new ArrayList<>();
        for (String line : lines)
            result.add(StaticUiElement.hash(line));
        Set<Integer> resultSet = new HashSet<>(result);
        assertEquals("There should not be duplicate hashes", resultSet.size(), result.size());
        System.out.println("Hash result: " + Arrays.toString(result.toArray()));
    }

    @SuppressWarnings({"StringEquality", "SimplifiableJUnitAssertion", "StringOperationCanBeSimplified"})
    @Test
    public void hashEqual() {
        /*
         __          __              _
         \ \        / /             (_)
          \ \  /\  / /_ _ _ __ _ __  _ _ __   __ _
           \ \/  \/ / _` | '__| '_ \| | '_ \ / _` |
            \  /\  / (_| | |  | | | | | | | | (_| |
             \/  \/ \__,_|_|  |_| |_|_|_| |_|\__, |
                                              __/ |
                                             |___/
         Do not change or simplify this test, unless you're not familiar with Java's literal pool!
         */
        String line1 = new String("360");
        String line2 = new String("360");
        assertFalse("You simplified this junit test in an illegal way!", line1 == line2);
        assertEquals("Both hashes should be the same", StaticUiElement.hash(line1), StaticUiElement.hash(line2));

    }

}