package dev.nadeldrucker.trafficswipe.ui;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

/**
 * Common ui operations
 */
class UiUtil {

    /**
     * Formats a duration to a human readable string
     *
     * @param duration input duration
     * @return formatted duration
     */
    static String formatDuration(Duration duration) {
        long absSec = Math.abs(duration.getSeconds());

        String formatted = String.format("%d:%02d:%02d", absSec / 3600, (absSec % 3600) / 60, (absSec % 60));

        if (duration.getSeconds() < 0) formatted = "-" + formatted;

        return formatted;
    }


    /**
     * Formats a timestamp for the footer where the last refresh timestamp is shown
     *
     * @param timestamp input
     * @return String for result footer
     */
    static String formatTimestamp(ZonedDateTime timestamp) {

        return "Realtime data from " + (timestamp.getSecond() - ZonedDateTime.now().getSecond()) + " seconds ago";

    }
}
