package dev.nadeldrucker.trafficswipe.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AnimationView extends SurfaceView implements Runnable {

    private final String TAG = this.getClass().getName();

    private boolean isRunning = false;
    private Thread renderingThread;
    private SurfaceHolder holder;

    private Renderable[] renderableList;
    private TouchPath touchPath;

    public AnimationView(Context context) {
        super(context);
        init();
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private boolean isInitialized = false;

    private void init() {
        if (isInitialized) return;
        isInitialized = true;

        setZOrderOnTop(true);

        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (isRunning) return;

                isRunning = true;
                renderingThread = new Thread(AnimationView.this);
                renderingThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isRunning = false;
            }
        });

        touchPath = new TouchPath();


        renderableList = new Renderable[]{
                touchPath
        };
    }

    public void pause(){

    }

    public void resume(){

    }

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

    private static final int MAX_FPS = 60;
    private static final int FRAME_TIME = 1000 / MAX_FPS;
    private static final int MAX_SKIPPED_FRAMES = 5;

    @Override
    public void run() {
        while (isRunning) {
            if (holder.getSurface().isValid()) {
                long beginTime = System.currentTimeMillis();

                for (Renderable renderable : renderableList) {
                    renderable.update();
                }

                Canvas c = holder.lockCanvas();
                if (c != null) {
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    for (Renderable renderable : renderableList) {
                        renderable.render(c);
                    }
                    holder.unlockCanvasAndPost(c);
                }

                long deltaTime = (System.currentTimeMillis() - beginTime);

                long sleepTime = FRAME_TIME - deltaTime;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (sleepTime < 0) {
                    int skipped = 0;
                    while (sleepTime < 0 && skipped < MAX_SKIPPED_FRAMES) {
                        sleepTime += FRAME_TIME;
                        skipped++;
                    }
                }
            }
        }
    }
}
