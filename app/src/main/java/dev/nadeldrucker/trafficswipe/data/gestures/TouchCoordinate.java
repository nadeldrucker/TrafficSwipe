package dev.nadeldrucker.trafficswipe.data.gestures;

import android.view.MotionEvent;

public class TouchCoordinate {
    private float x, y;

    public TouchCoordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates new from motion event using the provided coordinates
     * @param event motion event
     * @return new {@link #TouchCoordinate(float, float)}
     */
    public static TouchCoordinate fromMotionEvent(MotionEvent event){
        return new TouchCoordinate(event.getX(), event.getY());
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TouchCoordinate) {
            TouchCoordinate c = (TouchCoordinate) obj;
            return x == c.getX() && y == c.getY();
        }
        return false;
    }

    @Override
    public String toString() {
        return "X:" + x + " Y:" + y;
    }
}
