package dev.nadeldrucker.trafficswipe.logic;

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
public final class VehicleUiSupport {
    private static VehicleUiSupport instance;
    private final int[] COLORS;
    private final Context CONTEXT;
    private final Drawable SQUARE;
    private final Drawable CIRCLE;
    /**
     * SINGLETON PATTERN
     * Call this at startup
     *
     * @param context android context to access resources (e.g. drawables)
     */
    public VehicleUiSupport(Context context) {
        CONTEXT = context;
        CIRCLE = ContextCompat.getDrawable(context, R.drawable.circle);
        SQUARE = ContextCompat.getDrawable(context, R.drawable.square);

        if (instance != null) throw new IllegalStateException("Can be initialized only once");
        else instance = this;

        //Initialize colors
        COLORS = new int[18];
        COLORS[0] = ContextCompat.getColor(CONTEXT, R.color.colorTheme1a);
        COLORS[1] = ContextCompat.getColor(CONTEXT, R.color.colorTheme1b);
        COLORS[2] = ContextCompat.getColor(CONTEXT, R.color.colorTheme1c);
        COLORS[3] = ContextCompat.getColor(CONTEXT, R.color.colorTheme2a);
        COLORS[4] = ContextCompat.getColor(CONTEXT, R.color.colorTheme2b);
        COLORS[5] = ContextCompat.getColor(CONTEXT, R.color.colorTheme2c);
        COLORS[6] = ContextCompat.getColor(CONTEXT, R.color.colorTheme3a);
        COLORS[7] = ContextCompat.getColor(CONTEXT, R.color.colorTheme3b);
        COLORS[8] = ContextCompat.getColor(CONTEXT, R.color.colorTheme3c);
        COLORS[9] = ContextCompat.getColor(CONTEXT, R.color.colorTheme4a);
        COLORS[10] = ContextCompat.getColor(CONTEXT, R.color.colorTheme4b);
        COLORS[11] = ContextCompat.getColor(CONTEXT, R.color.colorTheme4c);
        COLORS[12] = ContextCompat.getColor(CONTEXT, R.color.colorTheme5a);
        COLORS[13] = ContextCompat.getColor(CONTEXT, R.color.colorTheme5b);
        COLORS[14] = ContextCompat.getColor(CONTEXT, R.color.colorTheme5c);
        COLORS[15] = ContextCompat.getColor(CONTEXT, R.color.colorTheme6a);
        COLORS[16] = ContextCompat.getColor(CONTEXT, R.color.colorTheme6b);
        COLORS[17] = ContextCompat.getColor(CONTEXT, R.color.colorTheme6c);


    }

    /**
     * used to convert a string into an integer to calculate the icon color
     *
     * @param s String to hash
     * @return simple hash
     */
    public static int hash(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash += Math.pow(s.charAt(i), i + 1);
        }
        return hash;
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
    public static VehicleUiSupport getInstance() {
        return instance;
    }

    public Drawable getSQUARE() {
        return SQUARE;
    }

    public Drawable getCIRCLE() {
        return CIRCLE;
    }

    /**
     * Change the color of a drawable
     *
     * @param source    drawable to recolor
     * @param colorSeed the color used is affiliated to this seed
     *                  Its intended to use the line number here, so that every vehicle
     *                  from the same line has the same icon color.
     * @return recolored drawable
     */
    public Drawable adjustColor(@NotNull Drawable source, int colorSeed) {
        source.setColorFilter(COLORS[colorSeed % COLORS.length], PorterDuff.Mode.MULTIPLY);
        return source;
    }

    /**
     * Change the color of a drawable to "S-Bahn grün"
     *
     * @param source drawable to recolor
     * @return recolored drawable
     */
    public Drawable adjustColor(@NotNull Drawable source) {
        source.setColorFilter(ContextCompat.getColor(CONTEXT, R.color.colorSBahn), PorterDuff.Mode.MULTIPLY);
        return source;
    }
}
