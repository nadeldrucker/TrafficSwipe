package dev.nadeldrucker.trafficswipe.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.ColorInt;
import dev.nadeldrucker.trafficswipe.animation.renderables.Renderable;

import java.util.List;

public abstract class RenderableView extends SurfaceView implements Runnable {

    private boolean isRunning = false;
    private Thread renderingThread;
    private SurfaceHolder holder;
    private boolean isInitialized = false;

    @ColorInt
    private int bgColor = Color.BLACK;

    public RenderableView(Context context) {
        super(context);
        onInit();
    }

    public RenderableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit();
    }

    /**
     * Returns a list of all renderables that should be updated and rendered.
     * @return list of renderables to use for rendering
     */
    public abstract List<Renderable> getRenderables();

    /**
     * Called once when the view is initialized.
     */
    protected void onInit() {
        if (isInitialized) return;
        isInitialized = true;

        //setZOrderOnTop(true);

        Drawable background = this.getBackground();
        if (background instanceof ColorDrawable) {
            bgColor = ((ColorDrawable) background).getColor();
        }
        this.setBackground(null);

        holder = getHolder();
        holder.setFormat(PixelFormat.OPAQUE);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (isRunning) return;

                isRunning = true;
                renderingThread = new Thread(RenderableView.this);
                renderingThread.start();

                onInitSurface();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isRunning = false;
            }
        });
    }

    public abstract void onInitSurface();

    /**
     * Called when the view is updated
     */
    protected void onUpdate(){

    }

    private static final int MAX_FPS = 60;
    private static final int FRAME_TIME = 1000 / MAX_FPS;
    private static final int MAX_SKIPPED_FRAMES = 5;

    @Override
    public void run() {
        while (isRunning) {
            if (holder.getSurface().isValid()) {
                long beginTime = System.currentTimeMillis();

                List<Renderable> renderableList = getRenderables();

                for (Renderable renderable : renderableList) {
                    renderable.update();
                    onUpdate();
                }

                Canvas c = holder.lockCanvas();
                if (c != null) {
                    c.drawColor(bgColor, PorterDuff.Mode.SRC_OVER);
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

    public void setBackgroundColor(@ColorInt int color){
        bgColor = color;
    }

}
