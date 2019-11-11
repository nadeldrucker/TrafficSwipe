package dev.nadeldrucker.trafficswipe.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import dev.nadeldrucker.trafficswipe.animation.renderables.Renderable;
import dev.nadeldrucker.trafficswipe.animation.renderables.TouchPath;
import dev.nadeldrucker.trafficswipe.dao.gestures.TouchCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TouchPathView extends RenderableView {

    private final List<TouchPath> touchPaths = new ArrayList<>();

    private long lastDrawTime = -1;
    private static final long drawTimeout = 1500;

    private Consumer<List<TouchPath>> touchPathFinishedListener;

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

    public void setTouchPathFinishedListener(Consumer<List<TouchPath>> consumer){
        this.touchPathFinishedListener = consumer;
    }

    @Override
    protected void onUpdate() {
        if (lastDrawTime != -1 && System.currentTimeMillis() > lastDrawTime + drawTimeout) {
            if (touchPathFinishedListener != null) {
                touchPathFinishedListener.accept(getTouchPaths());
            }

            clearTouchPaths();
            lastDrawTime = -1;
        }
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
        lastDrawTime = System.currentTimeMillis();
        touchPaths.get(touchPaths.size() - 1).getTouchPath().add(coordinate);
    }

    public List<TouchPath> getTouchPaths() {
        return touchPaths;
    }

    public void clearTouchPaths(){
        touchPaths.clear();
    }
}
