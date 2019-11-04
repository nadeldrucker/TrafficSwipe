package dev.nadeldrucker.trafficswipe.animation.renderables;

import android.graphics.Canvas;
import android.graphics.Paint;

public class AnimatedCursor extends MobileRenderable {

    private final Paint cursorPaint;
    private final float cursorRadius;

    private static final float ACCELERATION = .2f;
    private static final float MAX_ACCELERATION = 5f;

    public AnimatedCursor(float x, float y, Paint cursorPaint, float cursorRadius) {
        super(x, y);
        this.cursorPaint = cursorPaint;
        this.cursorRadius = cursorRadius;
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawCircle(getX(), getY(), cursorRadius, cursorPaint);
    }

    @Override
    public void update() {
        super.update();

        if (Math.abs(getDY()) > MAX_ACCELERATION) {
            setDY(Math.signum(getDY()) * -1 * ACCELERATION);
        } else {
            setDY(Math.signum(getDY()) * ACCELERATION + getDY());
        }
    }

    public void startAnimation(){
        setDY(ACCELERATION);
    }

    public void stopAnimation(){
        setDY(0);
    }
}
