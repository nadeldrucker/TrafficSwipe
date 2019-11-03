package dev.nadeldrucker.trafficswipe.animation.renderables;

import android.graphics.Canvas;

/**
 * Class representing a renderable object
 */
public interface Renderable {
    /**
     * Renders element onto canvas.
     * @param canvas canvas to draw element onto
     */
    void render(Canvas canvas);

    /**
     * Updates element (eg. position)
     */
    void update();


}
