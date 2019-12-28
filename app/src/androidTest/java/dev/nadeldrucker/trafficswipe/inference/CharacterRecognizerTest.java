package dev.nadeldrucker.trafficswipe.inference;

import dev.nadeldrucker.trafficswipe.data.gestures.TouchCoordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CharacterRecognizerTest {

    private CharacterRecognizer characterRecognizer;

    private static List<List<TouchCoordinate>> testCharacter = Collections.singletonList(
            Arrays.asList(
                    new TouchCoordinate(1, 10),
                    new TouchCoordinate(1, 0),
                    new TouchCoordinate(9, 10),
                    new TouchCoordinate(9, 0)
            )
    );

    @Before
    public void before() {
        characterRecognizer = CharacterRecognizer.getInstance();
    }

    @Test
    public void singletonIsSame() {
        CharacterRecognizer characterRecognizer1 = CharacterRecognizer.getInstance();
        CharacterRecognizer characterRecognizer2 = CharacterRecognizer.getInstance();

        assertSame(characterRecognizer1, characterRecognizer2);
    }

    @Test
    public void inferCharacter() {
        char c = characterRecognizer.infer(testCharacter);
        assertEquals('N', c);
    }

}