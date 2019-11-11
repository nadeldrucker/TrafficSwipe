package dev.nadeldrucker.trafficswipe.dao.gestures;

import java.util.List;
import java.util.stream.Collectors;

public class GestureDao {

    /**
     * Normalizes touch path, so that all values range from 0 to 1
     * @param sourcePath source
     * @return normalized touch path
     */
    public static List<TouchCoordinate> normalizeTouchPath(List<TouchCoordinate> sourcePath) {
        float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;

        for (TouchCoordinate c : sourcePath) {
            minX = Math.min(minX, c.getX());
            maxX = Math.max(maxX, c.getX());
            minY = Math.min(minY, c.getY());
            maxY = Math.max(maxY, c.getY());
        }

        final float finalMaxX = maxX;
        final float finalMinX = minX;
        final float finalMaxY = maxY;
        final float finalMinY = minY;

        return sourcePath.stream()
                .map(c -> new TouchCoordinate(normalize(c.getX(), finalMinX, finalMaxX), normalize(c.getY(), finalMinY, finalMaxY)))
                .collect(Collectors.toList());
    }

    /**
     * Normalizes value
     * @param val value to normalize
     * @param min global minimum
     * @param max global maximum
     * @return normalized value ranging from 0 to 1
     */
    private static float normalize(float val, float min, float max) {
        if (min >= max) throw new IllegalArgumentException("min cant be greater than / equal to max!");

        return (val - min) / (max - min);
    }

}
