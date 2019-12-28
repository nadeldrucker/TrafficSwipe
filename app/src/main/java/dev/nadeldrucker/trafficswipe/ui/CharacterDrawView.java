package dev.nadeldrucker.trafficswipe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import dev.nadeldrucker.trafficswipe.animation.TouchPathView;
import dev.nadeldrucker.trafficswipe.data.gestures.TouchCoordinate;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class CharacterDrawView extends TouchPathView {

    public static final int DELAY = 500;
    private boolean hasDrawnSinceLastTimer = true;
    private Consumer<List<List<TouchCoordinate>>> listener;
    private TimerTask lastTask;


    public CharacterDrawView(Context context) {
        super(context);
    }

    public CharacterDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            hasDrawnSinceLastTimer = false;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (listener != null && !hasDrawnSinceLastTimer) listener.accept(getTouchPaths());
                }
            };
            lastTask = task;
            new Timer().schedule(task, DELAY);
        } else {
            if (lastTask != null) lastTask.cancel();
            hasDrawnSinceLastTimer = true;
        }
        return true;

    }

    public void setListener(Consumer<List<List<TouchCoordinate>>> listener) {
        this.listener = listener;
    }
}
