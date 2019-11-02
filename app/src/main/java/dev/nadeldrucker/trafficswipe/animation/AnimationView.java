package dev.nadeldrucker.trafficswipe.animation;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import dev.nadeldrucker.trafficswipe.R;

import java.util.LinkedList;
import java.util.List;

public class AnimationView extends View {

    private final String TAG = this.getClass().getName();

    private Canvas canvas;

    private Bitmap canvasBitmap;
    private Paint canvasBitmapPaint = new Paint();
    private boolean isInitialized = false;

    private static class AnimationTouchCoordinate {
        float x, y;

        AnimationTouchCoordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private List<AnimationTouchCoordinate> currentMoveCoordinates = new LinkedList<>();

    public AnimationView(Context context) {
        super(context);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        init(w, h);
    }

    private void init(int width, int height) {
        canvas = new Canvas();
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(canvasBitmap);
        setWillNotDraw(false);
        isInitialized = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInitialized) return;

        drawPath();
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasBitmapPaint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {

            if (action == MotionEvent.ACTION_MOVE) {
                for (int i = 0; i < event.getHistorySize(); i++) {
                    currentMoveCoordinates.add(new AnimationTouchCoordinate(event.getHistoricalX(i), event.getHistoricalY(i)));
                }
            } else if (!currentMoveCoordinates.isEmpty()) {
                currentMoveCoordinates.clear();
            }

            if (action == MotionEvent.ACTION_UP) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }

            return true;
        }
        return false;
    }

    /**
     * Draws content onto canvas
     */
    private void drawPath() {
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
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        canvas.drawPath(p, paint);
    }
}
