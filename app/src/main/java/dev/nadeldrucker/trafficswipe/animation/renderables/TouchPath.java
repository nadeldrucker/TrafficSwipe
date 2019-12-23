package dev.nadeldrucker.trafficswipe.animation.renderables;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import dev.nadeldrucker.trafficswipe.data.gestures.TouchCoordinate;


import androidx.annotation.ColorInt;
import java.util.LinkedList;
import java.util.List;

public class TouchPath implements Renderable {

    private final List<TouchCoordinate> currentMoveCoordinates;
    private final Paint paint = new Paint();

    public TouchPath(@ColorInt int color) {
        currentMoveCoordinates = new LinkedList<>();
        paint.setColor(color);
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

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);


        canvas.drawPath(p, paint);
    }

    @Override
    public void update() {

    }

}
