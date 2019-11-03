package dev.nadeldrucker.trafficswipe.animation.renderables;

/**
 * Renderable that has a position and velocity.
 */
public abstract class MobileRenderable implements Renderable {

    private float x, y;
    private float dX = 0, dY = 0;
    private boolean isVisible = true;

    public MobileRenderable(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        x += dX;
        y += dY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDX() {
        return dX;
    }

    public void setDX(float dX) {
        this.dX = dX;
    }

    public float getDY() {
        return dY;
    }

    public void setDY(float dY) {
        this.dY = dY;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
