package dev.nadeldrucker.trafficswipe.animation.renderables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import dev.nadeldrucker.trafficswipe.dao.gestures.TouchCoordinate;

import java.util.LinkedList;
import java.util.List;

public class TouchPath implements Renderable {

    private final List<TouchCoordinate> currentMoveCoordinates;

    public TouchPath() {
        currentMoveCoordinates = new LinkedList<>();
    }

    public List<TouchCoordinate> getTouchPath(){
        return currentMoveCoordinates;
    }

    @Override
    public void render(Canvas canvas) {
        if (currentMoveCoordinates.isEmpty()) return;

        Path p = new Path();

        for (int i = 0; i < currentMoveCoordinates.size(); i++) {
            TouchCoordinate coord = currentMoveCoordinates.get(i);

            if (i == 0) {
                p.moveTo(coord.getX(), coord.getY());
            } else {
                p.lineTo(coord.getX(), coord.getY());
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
