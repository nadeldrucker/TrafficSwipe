package dev.nadeldrucker.trafficswipe.util.api.mockfiles;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/**
 * Utility class for reading {@link MockFile} from a resources path.
 */
public class MockFileReader {

    private static MockFileReader instance;

    private MockFileReader() {

    }

    /**
     * Reads a mock file from a specific path in the test resources folder.
     * @param path path to the file
     * @return a new {@link MockFile}
     */
    public MockFile readApiMockFile(@NotNull String path) {
        InputStream streamFromPath = getStreamFromPath(path);
        Objects.requireNonNull(streamFromPath, "Stream is null! File not found!");
        Reader reader = new InputStreamReader(streamFromPath);
        return new Gson().fromJson(reader, MockFile.class);
    }

    private InputStream getStreamFromPath(@NotNull String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    /**
     * Retrieves a mock file reader instance.
     * @return mock file reader
     */
    public static MockFileReader getInstance(){
        if (instance == null) {
            instance = new MockFileReader();
        }

        return instance;
    }

}
