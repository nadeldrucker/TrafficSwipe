package dev.nadeldrucker.trafficswipe.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import dev.nadeldrucker.trafficswipe.animation.renderables.AnimatedCursor;
import dev.nadeldrucker.trafficswipe.animation.renderables.Renderable;
import dev.nadeldrucker.trafficswipe.animation.renderables.TouchPath;
import dev.nadeldrucker.trafficswipe.data.gestures.TouchCoordinate;

import java.util.Arrays;
import java.util.List;

public class AnimationView extends RenderableView {

    private TouchPath touchPath;
    private AnimatedCursor cursor;

    public AnimationView(Context context) {
        super(context);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInit() {
        super.onInit();

        touchPath = new TouchPath();

        Paint p = new Paint();
        p.setColor(Color.GREEN);
        cursor = new AnimatedCursor(0, 0, p, 20);
    }

    @Override
    public void onInitSurface() {
        cursor.setPosition((float) getWidth() / 2, (float) getWidth() / 2);
        cursor.startAnimation();
    }

    @Override
    public List<Renderable> getRenderables() {
        return Arrays.asList(touchPath, cursor);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        cursor.setPosition(event.getX(), event.getY());

        if (action == MotionEvent.ACTION_MOVE) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                touchPath.getTouchPath().add(new TouchCoordinate(event.getHistoricalX(i), event.getHistoricalY(i)));
            }

            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            touchPath.getTouchPath().clear();
            cursor.startAnimation();
            return true;
        } else if (action == MotionEvent.ACTION_DOWN) {
            cursor.stopAnimation();
            return true;
        }

        return false;
    }
}
