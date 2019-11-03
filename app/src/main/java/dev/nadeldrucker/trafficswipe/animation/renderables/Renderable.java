package dev.nadeldrucker.trafficswipe.animation.renderables;

import android.graphics.Canvas;

/**
 * Class representing a renderable object
 */
public abstract class Renderable {
    /**
     * Renders element onto canvas.
     * @param canvas canvas to draw element onto
     */
    public abstract void render(Canvas canvas);

    /**
     * Updates element (eg. position)
     */
    public abstract void update();
}
