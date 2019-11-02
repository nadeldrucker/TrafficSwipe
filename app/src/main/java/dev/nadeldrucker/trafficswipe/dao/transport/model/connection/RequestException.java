package dev.nadeldrucker.trafficswipe.dao.transport.model.connection;

public class RequestException extends Exception {

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
