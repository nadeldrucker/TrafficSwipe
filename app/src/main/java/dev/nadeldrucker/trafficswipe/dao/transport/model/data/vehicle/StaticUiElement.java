package dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import dev.nadeldrucker.trafficswipe.R;

/**
 * Class for UI stuff that is required to create icons
 */
public final class StaticUiElement {
    private final int[] COLORS;
    private final Context CONTEXT;
    private static StaticUiElement instance;

    /**
     * SINGLETON PATTERN
     * Call this at startup
     *
     * @param context android context to access resources (e.g. drawables)
     */
    public StaticUiElement(Context context) {
        CONTEXT = context;
        if (instance != null) throw new IllegalStateException("Can initialized only once");
        else instance = this;
        COLORS = new int[1];
        COLORS[0] = ContextCompat.getColor(CONTEXT, R.color.colorAccent);

    }

    static int hash(String s){
        int hash=0;
        for (int i=0; i< s.length(); i++){
            hash+=Math.pow(s.charAt(i), i+1);
        }
        return hash;
    }

    /**
     * Change the color of a drawable
     * @param source drawable to recolor
     * @param colorSeed the color used is affiliated to this seed
     *                  Its intended to use the line number here, so that every vehicle
     *                  from the same line has the same icon color.
     * @return recolored drawable
     */
    Drawable adjustColor(@NotNull Drawable source, int colorSeed){
        source.setColorFilter( COLORS[colorSeed%COLORS.length], PorterDuff.Mode.MULTIPLY);
        return source;
    }

    /**
     * SINGLETON PATTERN
     *
     * @return its instance
     */
    @Contract(pure = true)
    /*
    The pure attribute is intended for methods that do not change the state of their objects,
    but just return a new value. If its return value is not used, removing its invocation will not
    affect program state or change the semantics, unless the method call throws an exception
    (exception is not considered to be a side effect).
     */
    static StaticUiElement getInstance() {
        return instance;
    }
}
