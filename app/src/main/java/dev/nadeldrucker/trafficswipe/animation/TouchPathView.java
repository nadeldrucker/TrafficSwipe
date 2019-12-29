package dev.nadeldrucker.trafficswipe.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.ColorInt;
import dev.nadeldrucker.trafficswipe.animation.renderables.Renderable;
import dev.nadeldrucker.trafficswipe.animation.renderables.TouchPath;
import dev.nadeldrucker.trafficswipe.data.gestures.TouchCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TouchPathView extends RenderableView {

    private final List<TouchPath> touchPaths = new ArrayList<>();

    @ColorInt
    private int color;

    public TouchPathView(Context context, int color) {
        super(context);
        this.color = color;
    }

    public TouchPathView(Context context, AttributeSet attrs, int color) {
        super(context, attrs);
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public List<Renderable> getRenderables() {
        return new ArrayList<>(touchPaths);
    }

    @Override
    public void onInitSurface() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouchPath(TouchCoordinate.fromMotionEvent(event), color);
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                appendTouchPath(TouchCoordinate.fromMotionEvent(event));
                return true;
        }

        return false;
    }

    private void startTouchPath(TouchCoordinate coordinate, int color) {
        touchPaths.add(new TouchPath(color));
        appendTouchPath(coordinate);
    }

    private void appendTouchPath(TouchCoordinate coordinate) {
        touchPaths.get(touchPaths.size() - 1).getTouchPath().add(coordinate);
    }

    public List<List<TouchCoordinate>> getTouchPaths() {
        return touchPaths.stream().map(TouchPath::getTouchPath).collect(Collectors.toList());
    }

    public void clearTouchPaths() {
        touchPaths.clear();
    }
}
