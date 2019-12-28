package dev.nadeldrucker.trafficswipe.data.gestures;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GestureDao {

    private static final String TAG = "GestureDao";

    private final Gson gson = new Gson();
    private final RequestQueue queue;
    private final String url;

    public GestureDao(Context context, String host){
        queue = Volley.newRequestQueue(context);
        this.url = host;
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
     * Sends data to training server, return next needed char if successful.
     * @param paths touch paths
     * @param character character that was drawn
     * @param userId unique id identifying the user
     */
    public CompletableFuture<Character> sendData(Character character, List<List<TouchCoordinate>> paths, String userId) {
        String json = gson.toJson(new GestureTrainingEntity(character, paths, userId));

        CompletableFuture<Character> future = new CompletableFuture<>();

        StringRequest req = new StringRequest(Request.Method.POST, url + "/data",
                response -> future.complete(getNextCharFromJSON(response)),
                future::completeExceptionally)
        {
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

    /**
     * Get the character which is needed by the training server.
     * @return character which is needed
     */
    public CompletableFuture<Character> getMostNeededCharacter(){
        CompletableFuture<Character> future = new CompletableFuture<>();

        StringRequest req = new StringRequest(Request.Method.GET, url + "/nextChar",
                response -> future.complete(getNextCharFromJSON(response)),
                future::completeExceptionally);

        queue.add(req);

        return future;
    }

    private static char getNextCharFromJSON(String s){
        return JsonParser.parseString(s).getAsJsonObject().get("nextChar").getAsString().charAt(0);
    }

}
