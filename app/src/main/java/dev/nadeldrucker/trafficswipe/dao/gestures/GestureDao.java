package dev.nadeldrucker.trafficswipe.dao.gestures;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GestureDao {

    private static final String TAG = "GestureDao";

    private final Gson gson = new Gson();
    private final RequestQueue queue;
    private final String host;

    public GestureDao(Context context, String host){
        queue = Volley.newRequestQueue(context);
        this.host = host;
    }

    /**
     * Normalizes touch path, so that all values range from 0 to 1
     * @param sourcePath source
     * @return normalized touch path
     */
    public static List<TouchCoordinate> normalizeTouchPath(List<TouchCoordinate> sourcePath) {
        float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;

        for (TouchCoordinate c : sourcePath) {
            minX = Math.min(minX, c.getX());
            maxX = Math.max(maxX, c.getX());
            minY = Math.min(minY, c.getY());
            maxY = Math.max(maxY, c.getY());
        }

        final float finalMaxX = maxX;
        final float finalMinX = minX;
        final float finalMaxY = maxY;
        final float finalMinY = minY;

        return sourcePath.stream()
                .map(c -> new TouchCoordinate(normalize(c.getX(), finalMinX, finalMaxX), normalize(c.getY(), finalMinY, finalMaxY)))
                .collect(Collectors.toList());
    }

    /**
     * Normalizes value
     * @param val value to normalize
     * @param min global minimum
     * @param max global maximum
     * @return normalized value ranging from 0 to 1
     */
    private static float normalize(float val, float min, float max) {
        if (min >= max) throw new IllegalArgumentException("min cant be greater than / equal to max!");

        return (val - min) / (max - min);
    }

    /**
     * Sends data to training server.
     * @param paths touch paths
     * @param character character that was drawn
     */
    public CompletableFuture<String> sendData(Character character, List<List<TouchCoordinate>> paths) {
        List<List<TouchCoordinate>> normalizedList = paths.stream().map(GestureDao::normalizeTouchPath).collect(Collectors.toList());

        String json = gson.toJson(new GestureTrainingEntity(character, normalizedList));

        CompletableFuture<String> future = new CompletableFuture<>();

        StringRequest req = new StringRequest(Request.Method.POST, "http://" + host + "/data", response -> {
            Log.d(TAG, "Received response!");
            future.complete("Success!");

        }, error -> {
            Log.e(TAG, "Received error: " + error.toString());
            future.complete(error.toString());
        }) {
            @Override
            public byte[] getBody() {
                return json.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(req);

        return future;
    }

}
