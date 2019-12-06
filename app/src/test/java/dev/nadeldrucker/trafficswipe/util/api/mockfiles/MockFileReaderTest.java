package dev.nadeldrucker.trafficswipe.util.api.mockfiles;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MockFileReaderTest {

    private MockFileReader reader;

    @Before
    public void before() {
        reader = MockFileReader.getInstance();
    }

    /**
     * tests that no new instance is created by getting instance.
     */
    @Test
    public void testSingleton() {
        assertSame(MockFileReader.getInstance(), MockFileReader.getInstance());
    }

    /**
     * tests if file is read correctly
     */
    @Test
    public void readTestFile() {
        MockFile file = MockFileReader.getInstance().readApiMockFile("mocks/mock/TestFile.json");

        assertEquals(file.request.url, "http://mycooldomain.com/endpointv1/wow?baum=tree&tree=baum");
        assertEquals(file.request.body.get("testString").getAsString(), "string");

        assertEquals(file.response.statusCode, 200);
        assertEquals(file.response.body.get("data").getAsJsonObject().get("sample").getAsString(), "sampleData");
    }

}
