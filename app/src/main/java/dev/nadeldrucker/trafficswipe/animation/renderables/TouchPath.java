package dev.nadeldrucker.trafficswipe.animation.renderables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

public class TouchPath implements Renderable {

    public static class AnimationTouchCoordinate {
        float x, y;

        public AnimationTouchCoordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Creates new from motion event using the provided coordinates
         * @param event motion event
         * @return new {@link #AnimationTouchCoordinate(float, float)}
         */
        public static AnimationTouchCoordinate fromMotionEvent(MotionEvent event){
            return new AnimationTouchCoordinate(event.getX(), event.getY());
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
    public void render(Canvas canvas) {
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
    public void update() {

    }

}
