package dev.nadeldrucker.trafficswipe.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import dev.nadeldrucker.trafficswipe.animation.renderables.Renderable;
import dev.nadeldrucker.trafficswipe.animation.renderables.TouchPath;

public class AnimationView extends RenderableView {

    private TouchPath touchPath;

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
    }

    @Override
    public Renderable[] getRenderables() {
        return new Renderable[]{
                touchPath
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {

            if (action == MotionEvent.ACTION_MOVE) {
                for (int i = 0; i < event.getHistorySize(); i++) {
                    touchPath.getTouchPath().add(new TouchPath.AnimationTouchCoordinate(event.getHistoricalX(i), event.getHistoricalY(i)));
                }
            } else if (!touchPath.getTouchPath().isEmpty()) {
                touchPath.getTouchPath().clear();
            }

            return true;
        }
        return false;
    }
}
