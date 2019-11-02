package dev.nadeldrucker.trafficswipe.dao.transport.model.connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dev.nadeldrucker.trafficswipe.dao.transport.model.data.AbstractTransportEntity;

import java.util.Optional;

public class Response<T extends AbstractTransportEntity> {

    public enum ResponseStatus {
        SUCCESS,
        ERROR
    }

    public Response(ResponseStatus status) {
        this.status = status;
    }

    private ResponseStatus status;
    private T result = null;
    private Error error = null;

    public void setResult(@Nullable T result) {
        this.result = result;
    }

    public void setError(@Nullable Error error) {
        this.error = error;
    }

    @NonNull
    public ResponseStatus getStatus() {
        return status;
    }

    @NonNull
    public Optional<T> getResult() {
        return Optional.ofNullable(result);
    }

    @NonNull
    public Optional<Error> getError() {
        return Optional.ofNullable(error);
    }
}
