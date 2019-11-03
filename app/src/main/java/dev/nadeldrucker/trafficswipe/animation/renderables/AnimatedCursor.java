package dev.nadeldrucker.trafficswipe.animation.renderables;

import android.graphics.Canvas;
import android.graphics.Paint;

public class AnimatedCursor extends MobileRenderable {

    private final Paint cursorPaint;
    private final float cursorRadius;

    public AnimatedCursor(float x, float y, Paint cursorPaint, float cursorRadius) {
        super(x, y);
        this.cursorPaint = cursorPaint;
        this.cursorRadius = cursorRadius;
    }

    @Override
    public void render(Canvas canvas) {
        if (!isVisible()) return;

        canvas.drawCircle(getX(), getY(), cursorRadius, cursorPaint);
    }
}
