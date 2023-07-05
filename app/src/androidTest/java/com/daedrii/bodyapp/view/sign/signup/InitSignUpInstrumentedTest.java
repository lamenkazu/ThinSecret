package com.daedrii.bodyapp.view.sign.signup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.view.sign.SignInActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

@RunWith(AndroidJUnit4.class)
public class InitSignUpInstrumentedTest {

    @Mock
    SignUpController mockSignUpController;

    @Captor
    ArgumentCaptor<Consumer<Boolean>> successCaptor;
    @Rule
    public ActivityScenarioRule<InitSignUp> initActivityRule = new ActivityScenarioRule<>(InitSignUp.class);

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void ShouldInitComponentsSuccessfully() {
        ActivityScenario<InitSignUp> initScenario = initActivityRule.getScenario();
        initScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.btnNext);
        });


    }

    @Test
    public void ShouldGoToGoalSignUpWhenButtonIsClicked() {

        onView(withId(R.id.init_next)).perform(click());

        intending(hasComponent(GoalSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

}
