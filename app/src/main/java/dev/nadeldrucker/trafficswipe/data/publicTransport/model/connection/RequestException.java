package dev.nadeldrucker.trafficswipe.data.publicTransport.model.connection;

public class RequestException extends Exception {

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
