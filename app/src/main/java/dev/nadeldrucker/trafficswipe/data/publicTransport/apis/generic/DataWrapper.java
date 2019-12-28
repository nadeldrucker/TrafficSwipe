package dev.nadeldrucker.trafficswipe.data.publicTransport.apis.generic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Wrapper wrapping data objects including a optional error state.
 * @param <T> type of the data object
 */
public final class DataWrapper<T> {

    public enum ErrorType {
        NETWORK_ERROR,
        NULL_ERROR
    }

    public static class DataError {
        private final String errorMessage;
        private final ErrorType errorType;

        public DataError(ErrorType errorType, String errorMessage) {
            this.errorMessage = errorMessage;
            this.errorType = errorType;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public ErrorType getErrorType() {
            return errorType;
        }

        @Override
        public String toString() {
            return "DataError{" +
                    "errorMessage='" + errorMessage + '\'' +
                    ", errorType=" + errorType +
                    '}';
        }
    }

    private final T dataObject;
    private final DataError error;

    private DataWrapper() {
        dataObject = null;
        error = null;
    }

    private DataWrapper(@NotNull T dataObject) {
        this.dataObject = dataObject;
        error = null;
    }

    private DataWrapper(@NotNull ErrorType errorType, @NotNull String errorMessage) {
        error = new DataError(errorType, errorMessage);
        dataObject = null;
    }

    /**
     * Creates a new data wrapper containing data and no error.
     * @param data data to wrap
     */
    public static <G> DataWrapper<G> createOfData(@NotNull G data) {
        return new DataWrapper<>(data);
    }

    /**
     * Creates a new data object representing an error.
     * @param errorType type of the error
     * @param errorMessage extra message
     */
    public static <G> DataWrapper<G> createOfError(@NotNull ErrorType errorType, @NotNull String errorMessage) {
        return new DataWrapper<>(errorType, errorMessage);
    }

    /**
     * Creates a data wrapper object from a {@link DataError}
     * @param error data error
     * @param <G> type of wrapper
     * @return new {@link DataWrapper}
     */
    public static <G> DataWrapper<G> createOfError(DataError error){
        return createOfError(error.getErrorType(), error.getErrorMessage());
    }

    /**
     * Creates a data wrapper representing a loading state.
     * @param <G> type of wrapper
     * @return empty data wrapper, aka. loading
     */
    public static <G> DataWrapper<G> createLoading() {
        return new DataWrapper<>();
    }

    /**
     * @return optional containing {@link T} data if present, else {@link #getError()} is present.
     */
    public Optional<T> getData() {
        return Optional.ofNullable(dataObject);
    }

    /**
     * @return optional containing {@link DataError} if present, else {@link #getData()} is present.
     */
    public Optional<DataError> getError() {
        return Optional.ofNullable(error);
    }

    /**
     * Evaluates the data wrapper, calling a {@link Consumer} for both cases.<br>
     * <b>Can only be used if the data is ready, can be checked using {@link #isLoading()}</b>
     * @param dataConsumer consumer called when data is present
     * @param errorConsumer consumer called when error is present
     * @param loadingRunnable runnable executed when the wrapper is in the loading state
     */
    public void evaluate(Consumer<T> dataConsumer, Consumer<DataError> errorConsumer, @Nullable Runnable loadingRunnable) {
        if (getData().isPresent()) {
            dataConsumer.accept(getData().get());
        } else if (getError().isPresent()) {
            errorConsumer.accept(getError().get());
        } else {
            if (loadingRunnable != null) {
                loadingRunnable.run();
            }
        }
    }

    /**
     * @return true if neither data nor error are present.
     */
    public boolean isLoading() {
        return !getError().isPresent() && !getData().isPresent();
    }

}
