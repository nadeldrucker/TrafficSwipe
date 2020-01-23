package dev.nadeldrucker.trafficswipe.ui;

import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import dev.nadeldrucker.trafficswipe.App;
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

    /**
     * Requests all permissions if not already granted.
     * @param permissions permissions to request
     */
    public static void requestLocationPermissions(Runnable onAlreadyGranted, Fragment fragment, int callbackId, String... permissions) {
        boolean allPermissionsGranted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(App.getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
            }
        }

        if (!allPermissionsGranted) {
            fragment.requestPermissions(permissions, callbackId);
        } else {
            onAlreadyGranted.run();
        }
    }
}
