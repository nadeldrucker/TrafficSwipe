package dev.nadeldrucker.trafficswipe.ui;

import org.threeten.bp.Duration;

/**
 * Common ui operations
 */
public class UiUtil {

    /**
     * Formats a duration to a human readable string
     *
     * @param duration input duration
     * @return formatted duration
     */
    public static String formatDuration(Duration duration) {
        long absSec = Math.abs(duration.getSeconds());

        String formatted = String.format("%d:%02d:%02d", absSec / 3600, (absSec % 3600) / 60, (absSec % 60));

        if (duration.getSeconds() < 0) formatted = "-" + formatted;

        return formatted;
    }

}
