package dev.nadeldrucker.trafficswipe.animation;

import android.graphics.Canvas;

/**
 * Class representing a renderable object
 */
public abstract class Renderable {
    public abstract void render(Canvas canvas);
    public abstract void update();
}
