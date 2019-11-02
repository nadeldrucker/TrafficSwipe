package dev.nadeldrucker.trafficswipe.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.core.content.ContextCompat;
import dev.nadeldrucker.trafficswipe.R;

import java.util.LinkedList;
import java.util.List;

public class TouchPath extends Renderable {

    public static class AnimationTouchCoordinate {
        float x, y;

        AnimationTouchCoordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private final List<AnimationTouchCoordinate> currentMoveCoordinates;

    public TouchPath() {
        currentMoveCoordinates = new LinkedList<>();
    }

    public List<AnimationTouchCoordinate> getTouchPath(){
        return currentMoveCoordinates;
    }

    @Override
    public synchronized void render(Canvas canvas) {
        if (currentMoveCoordinates.isEmpty()) return;

        Path p = new Path();

        for (int i = 0; i < currentMoveCoordinates.size(); i++) {
            AnimationTouchCoordinate coord = currentMoveCoordinates.get(i);

            if (i == 0) {
                p.moveTo(coord.x, coord.y);
            } else {
                p.lineTo(coord.x, coord.y);
            }
        }

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);

        canvas.drawPath(p, paint);
    }

    @Override
    public synchronized void update() {

    }

}
