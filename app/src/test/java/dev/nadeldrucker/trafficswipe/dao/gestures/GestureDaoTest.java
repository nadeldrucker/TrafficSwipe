package dev.nadeldrucker.trafficswipe.dao.gestures;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class GestureDaoTest {

    /**
     * tests method for correct return
     *
     * minX: -5 maxX: 5
     * minY: 0 maxY: 10
     */
    @Test
    public void normalizeTouchPath() {
        List<TouchCoordinate> touchCoordinates = Arrays.asList(new TouchCoordinate(-5, 10),
                new TouchCoordinate(-1, 5),
                new TouchCoordinate(-1, 0),
                new TouchCoordinate(5, 0));

        List<TouchCoordinate> expectedCoords = Arrays.asList(new TouchCoordinate(0, 1),
                new TouchCoordinate(.4f, .5f),
                new TouchCoordinate(.4f, 0),
                new TouchCoordinate(1, 0));

        List<List<TouchCoordinate>> normalized = GestureDao.normalizeTouchPaths(Collections.singletonList(touchCoordinates));
        List<TouchCoordinate> collect = normalized.stream().flatMap(Collection::stream).collect(Collectors.toList());

        assertEquals(expectedCoords, collect);
    }

    @Test
    public void normalizeTouchPaths() {
        List<TouchCoordinate> tc1 = Arrays.asList(new TouchCoordinate(-5, 5),
                new TouchCoordinate(5, -5));

        List<TouchCoordinate> tc2 = Arrays.asList(new TouchCoordinate(-2, 2),
                new TouchCoordinate(-4, 4));

        List<TouchCoordinate> expected1 = Arrays.asList(new TouchCoordinate(0, 1),
                new TouchCoordinate(1, 0));

        List<TouchCoordinate> expected2 = Arrays.asList(new TouchCoordinate(.3f, .7f),
                new TouchCoordinate(.1f, .9f));

        assertEquals(Arrays.asList(expected1, expected2), GestureDao.normalizeTouchPaths(Arrays.asList(tc1, tc2)));
    }

}
