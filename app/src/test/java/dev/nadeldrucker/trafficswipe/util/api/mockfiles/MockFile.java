package dev.nadeldrucker.trafficswipe.util.api.mockfiles;

import com.google.gson.JsonObject;

public class MockFile {

    public static class MockRequest {
        public String url;
        public JsonObject body;
    }

    public static class MockResponse {
        public int statusCode;
        public JsonObject body;
    }

    public MockRequest request;
    public MockResponse response;
}
