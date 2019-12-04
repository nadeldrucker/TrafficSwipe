package dev.nadeldrucker.trafficswipe.test.stories;

import android.os.Bundle;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import dev.nadeldrucker.trafficswipe.R;
import dev.nadeldrucker.trafficswipe.fragments.StartFragment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StartFragmentTest {

    private NavController navController;
    private FragmentScenario<StartFragment> startFragmentFragmentScenario;

    @Before
    public void before() {
        navController = Mockito.mock(NavController.class);
        startFragmentFragmentScenario = FragmentScenario.launchInContainer(StartFragment.class, null, R.style.Theme_AppCompat, null);
        startFragmentFragmentScenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navController);
        });
    }

    @Test
    public void helpOpensHelpFragment() {
        onView(ViewMatchers.withId(R.id.startFragment_helpFab)).perform(ViewActions.click());
        Mockito.verify(navController).navigate(R.id.action_startFragment_to_helpSheet);
    }

    @Test
    @Ignore("Cant test invisible EditText. Test gestures instead!")
    public void afterEntering3CharsOpenDeparturesFragment() {
        ViewInteraction view = onView(ViewMatchers.withId(R.id.etSearch));
        String queryString = "NUP";
        view.perform(ViewActions.typeText(queryString));

        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        Mockito.verify(navController).navigate(Mockito.eq(R.id.action_startFragment_to_resultFragment), captor.capture());

        assertEquals(captor.getValue().getString("query"), queryString);
    }
}
