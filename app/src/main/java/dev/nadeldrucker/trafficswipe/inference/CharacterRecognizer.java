package dev.nadeldrucker.trafficswipe.inference;

import android.content.res.AssetFileDescriptor;
import android.graphics.*;
import android.util.Log;
import dev.nadeldrucker.trafficswipe.App;
import dev.nadeldrucker.trafficswipe.dao.gestures.TouchCoordinate;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for inferring handwritten characters
 */
public class CharacterRecognizer {

    private static CharacterRecognizer instance;
    private static final String MODEL_PATH = "model.tflite";
    private static String TAG = "CharacterRecognizer";

    private final ByteBuffer buffer;
    private final Interpreter tflite;

    private static final int PRESCALE_WIDTH = 128, PRESCALE_HEIGHT = 128;
    private static final int WIDTH = 28, HEIGHT = 28;
    private static final int STROKE_WIDTH = 6;
    private static final float PADDING = 0.15f;
    private static final int LABEL_SIZE = 26;

    private CharacterRecognizer() throws IOException {
        tflite = new Interpreter(loadModelFile(), new Interpreter.Options().setUseNNAPI(true));
        tflite.getInputTensor(0);
        buffer = ByteBuffer.allocateDirect(4 * WIDTH * HEIGHT);
        buffer.order(ByteOrder.nativeOrder());
    }

    /**
     * Memory-map the model file in Assets.
     */
    private static ByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = App.getContext().getAssets().openFd(MODEL_PATH);

        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /**
     * retrieves an instance of the character recognizer
     *
     * @return instance
     */
    public static CharacterRecognizer getInstance() {
        if (instance == null) {
            try {
                instance = new CharacterRecognizer();
            } catch (IOException e) {
                Log.e(TAG, "Error while creating instance!", e);
            }
        }

        return instance;
    }

    /**
     * Puts the bitmap in the byte buffer and converts to grayscale 0-1
     * @param bitmap bitmap to use
     */
    private void putBitmapInBuffer(Bitmap bitmap) {
        int[] imagePixels = new int[WIDTH * HEIGHT];

        bitmap.getPixels(imagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());

        buffer.rewind();

        int pixel = 0;
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                final int val = imagePixels[pixel++];
                buffer.putFloat(convertToGreyScale(val));
            }
        }
    }

    /**
     * Infers a character from a path using a predefined tflite model
     * @param path path to use as input
     * @return character inferred
     */
    public char infer(List<List<TouchCoordinate>> path) {
        Bitmap bitmapFromPaths = createBitmapFromPaths(path);
        putBitmapInBuffer(bitmapFromPaths);

        float[][] outputArray = new float[1][LABEL_SIZE];
        tflite.run(buffer, outputArray);

        float maxIndex = 0;
        float max = Float.MIN_VALUE;
        for (int i = 0; i < outputArray[0].length; i++) {
            if (max < outputArray[0][i]) {
                max = outputArray[0][i];
                maxIndex = i;
            }
        }

        return indexInAlphabetToChar((int) maxIndex);
    }

    /**
     * Creates a scaled bitmap from the given paths. Scales and preprocesses them accordingly.
     * @param rawPaths paths to use as input
     * @return bitmap
     */
    private Bitmap createBitmapFromPaths(List<List<TouchCoordinate>> rawPaths) {
        Bitmap bitmap = Bitmap.createBitmap(PRESCALE_WIDTH, PRESCALE_HEIGHT, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.BLACK);

        List<List<TouchCoordinate>> paths = preprocessPaths(rawPaths, PRESCALE_HEIGHT);

        paths.forEach(path -> {
            Path p = new Path();

            for (int i = 0; i < path.size(); i++) {
                TouchCoordinate coord = path.get(i);

                if (i == 0) {
                    p.moveTo(coord.getX(), coord.getY());
                } else {
                    p.lineTo(coord.getX(), coord.getY());
                }
            }

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(STROKE_WIDTH);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);

            c.drawPath(p, paint);
        });

        return Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
    }

    /**
     * Preprocesses the paths given by adding a padding, making the image square, centering the image.
     * @param paths paths to use as input
     * @param scale width/height to use
     * @return scaled paths
     */
    private List<List<TouchCoordinate>> preprocessPaths(List<List<TouchCoordinate>> paths, float scale) {
        float maxX = paths.stream().flatMap(List::stream).max(Comparator.comparing(TouchCoordinate::getX)).map(TouchCoordinate::getX).orElse(Float.MAX_VALUE);
        float maxY = paths.stream().flatMap(List::stream).max(Comparator.comparing(TouchCoordinate::getY)).map(TouchCoordinate::getY).orElse(Float.MAX_VALUE);

        float minX = paths.stream().flatMap(List::stream).min(Comparator.comparing(TouchCoordinate::getX)).map(TouchCoordinate::getX).orElse(Float.MIN_VALUE);
        float minY = paths.stream().flatMap(List::stream).min(Comparator.comparing(TouchCoordinate::getY)).map(TouchCoordinate::getY).orElse(Float.MIN_VALUE);

        float width = maxX - minX;
        float height = maxY - minY;

        float padW = width * PADDING;
        float padH = height * PADDING;

        maxX += padW;
        minX -= padW;

        maxY += padH;
        minY -= padH;

        float xIncrease = 0;
        float yIncrease = 0;

        if (height > width) {
            xIncrease += height - width;
        } else {
            yIncrease += width - height;
        }

        float finalXIncrease = xIncrease;
        float finalMinX = minX;
        float finalMaxX = maxX;
        float finalYIncrease = yIncrease;
        float finalMinY = minY;
        float finalMaxY = maxY;

        return paths.stream()
                .map(touchCoordinates -> touchCoordinates
                        .stream()
                        .map(touchCoordinate -> {
                            float normalizedX = normalize(touchCoordinate.getX() + finalXIncrease / 2, finalMinX, finalMaxX + finalXIncrease) * scale;
                            float normalizedY = normalize(touchCoordinate.getY() + finalYIncrease / 2, finalMinY, finalMaxY + finalYIncrease) * scale;
                            return new TouchCoordinate(normalizedX, normalizedY);
                        })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private static float normalize(float val, float min, float max) {
        return (val - min) / (max - min);
    }

    private static float convertToGreyScale(int color) {
        float r = ((color >> 16) & 0xFF);
        float g = ((color >> 8) & 0xFF);
        float b = ((color) & 0xFF);

        int grayscaleValue = (int) (0.299f * r + 0.587f * g + 0.114f * b);
        return grayscaleValue / 255.0f;
    }

    /**
     * Calculates the character based on the index in the alphabet.
     * @param index index of char in alphabet
     * @return character
     */
    private static char indexInAlphabetToChar(int index) {
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.charAt(index);
    }
}
