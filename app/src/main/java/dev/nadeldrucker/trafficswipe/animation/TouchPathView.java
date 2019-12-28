package dev.nadeldrucker.trafficswipe.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import dev.nadeldrucker.trafficswipe.animation.renderables.Renderable;
import dev.nadeldrucker.trafficswipe.animation.renderables.TouchPath;
import dev.nadeldrucker.trafficswipe.data.gestures.TouchCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TouchPathView extends RenderableView {

    private final List<TouchPath> touchPaths = new ArrayList<>();

    public TouchPathView(Context context) {
        super(context);
    }

    public TouchPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                startTouchPath(TouchCoordinate.fromMotionEvent(event));
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                appendTouchPath(TouchCoordinate.fromMotionEvent(event));
                return true;
        }

        return false;
    }

    private void startTouchPath(TouchCoordinate coordinate) {
        touchPaths.add(new TouchPath());
        appendTouchPath(coordinate);
    }

    private void appendTouchPath (TouchCoordinate coordinate) {
        touchPaths.get(touchPaths.size() - 1).getTouchPath().add(coordinate);
    }

    public List<List<TouchCoordinate>> getTouchPaths() {
        return touchPaths.stream().map(TouchPath::getTouchPath).collect(Collectors.toList());
    }

    public void clearTouchPaths(){
        touchPaths.clear();
    }
}
