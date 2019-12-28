package dev.nadeldrucker.trafficswipe.util.api;

import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import dev.nadeldrucker.trafficswipe.util.api.mockfiles.MockFile;
import dev.nadeldrucker.trafficswipe.util.api.mockfiles.MockFileReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Mocked http stack providing responses predefined in json files.
 */
public class MockHttpStack extends BaseHttpStack {

    private List<MockFile> nextResponses = new ArrayList<>();

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) {
        if (request instanceof JsonObjectRequest) {
            MockFile mockFile = nextResponses.get(nextResponses.size() - 1);
            return createValidResponse(mockFile.response.body, mockFile.response.statusCode);
        } else {
            throw new UnsupportedOperationException("Only JsonRequests are supported atm!");
        }
    }

    /**
     * Queues a new request using a provided mock file.
     *
     * @param mockFilePath path to mockfile
     */
    public void queueNextResponse(String mockFilePath) {
        MockFile mockFile = MockFileReader.getInstance().readApiMockFile(mockFilePath);
        nextResponses.add(mockFile);
    }

    private HttpResponse createValidResponse(JsonObject responseBody, int statusCode) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd mmm yyyy HH:mm:ss zzz");
        List<Header> headers = Arrays.asList(new Header("Access-Control-Allow-Origin", "*"),
                new Header("Content-Type", "application/json"),
                new Header("Date", dateFormat.format(new Date())));

        byte[] bodyBytes = responseBody.toString().getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(bodyBytes);

        return new HttpResponse(statusCode, headers, bodyBytes.length, stream);
    }
}
