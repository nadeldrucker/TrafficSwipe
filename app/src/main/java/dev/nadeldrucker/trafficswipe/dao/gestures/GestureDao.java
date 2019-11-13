package dev.nadeldrucker.trafficswipe.dao.gestures;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
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
     * Normalizes touch paths, so that all values range from 0 to 1
     * @param sourcePaths source paths
     * @return normalized touch paths
     */
    public static List<List<TouchCoordinate>> normalizeTouchPaths(List<List<TouchCoordinate>> sourcePaths){
        float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;

        List<TouchCoordinate> touchCoordinates = sourcePaths
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        for (TouchCoordinate c : touchCoordinates) {
            minX = Math.min(minX, c.getX());
            maxX = Math.max(maxX, c.getX());
            minY = Math.min(minY, c.getY());
            maxY = Math.max(maxY, c.getY());
        }

        final float finalMaxX = maxX;
        final float finalMinX = minX;
        final float finalMaxY = maxY;
        final float finalMinY = minY;

        return sourcePaths
                .stream()
                .map(path -> path
                        .parallelStream()
                        .map(c -> new TouchCoordinate(normalize(c.getX(), finalMinX, finalMaxX), normalize(c.getY(), finalMinY, finalMaxY)))
                        .collect(Collectors.toList()))
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
        List<List<TouchCoordinate>> normalizedList = normalizeTouchPaths(paths);

        String json = gson.toJson(new GestureTrainingEntity(character, normalizedList));

        CompletableFuture<String> future = new CompletableFuture<>();

        StringRequest req = new StringRequest(Request.Method.POST, "http://" + host + "/data", response -> {
            Log.d(TAG, "Received response!");
            try {
                future.complete("Success " + new JSONObject(response).getString("nextChar"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

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