package dev.nadeldrucker.trafficswipe.dao.gestures;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

        assertEquals(GestureDao.normalizeTouchPath(touchCoordinates), expectedCoords);
    }

}
