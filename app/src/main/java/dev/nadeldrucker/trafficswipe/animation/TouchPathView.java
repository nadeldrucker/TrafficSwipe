package dev.nadeldrucker.trafficswipe.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;
import dev.nadeldrucker.trafficswipe.animation.renderables.Renderable;
import dev.nadeldrucker.trafficswipe.animation.renderables.TouchPath;
import dev.nadeldrucker.trafficswipe.animation.renderables.TouchPath.AnimationTouchCoordinate;

import java.util.ArrayList;
import java.util.List;

public class TouchPathView extends RenderableView {

    private final List<TouchPath> touchPaths = new ArrayList<>();

    private long lastDrawTime = -1;
    private static final long drawTimeout = 1500;

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

    @Override
    protected void onUpdate() {
        if (lastDrawTime != -1 && System.currentTimeMillis() > lastDrawTime + drawTimeout) {
            clearTouchPaths();
            lastDrawTime = -1;

            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(getContext(), "Path End", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouchPath(AnimationTouchCoordinate.fromMotionEvent(event));
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                appendTouchPath(AnimationTouchCoordinate.fromMotionEvent(event));
                return true;
        }

        return false;
    }

    private void startTouchPath(AnimationTouchCoordinate coordinate) {
        touchPaths.add(new TouchPath());
        appendTouchPath(coordinate);
    }

    private void appendTouchPath (AnimationTouchCoordinate coordinate) {
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
