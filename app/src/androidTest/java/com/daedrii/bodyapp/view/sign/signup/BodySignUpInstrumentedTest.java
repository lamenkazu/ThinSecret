package com.daedrii.bodyapp.view.sign.signup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.model.user.BodyInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

public class BodySignUpInstrumentedTest {
    @Captor
    ArgumentCaptor<Consumer<Boolean>> successCaptor;
    @Rule
    public ActivityScenarioRule<BodySignUp> bodyActivityRule = new ActivityScenarioRule<>(BodySignUp.class);


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
        ActivityScenario<BodySignUp> goalScenario = bodyActivityRule.getScenario();
        goalScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.next);
            assertNotNull(activity.height);
            assertNotNull(activity.weight);
        });

    }


    @Test
    public void ShouldNotGoToNextScreenWhenFieldIsEmpty() {

        ActivityScenario<BodySignUp> goalScenario = bodyActivityRule.getScenario();

        onView(withId(R.id.body_next)).perform(click());

        goalScenario.onActivity(activity -> {
            // Chame o método que pode lançar a exceção
            assertFalse(activity.decide(activity.height.getText().toString(), activity.weight.getText().toString()));
        });
    }

    @Test
    public void ShouldGoToNextScreenWhenFieldIsFilledSuccessfully(){
        ActivityScenario<BodySignUp> bodyScenario = bodyActivityRule.getScenario();

        bodyScenario.onActivity(activity -> {
            activity.height.setText("165");
            activity.weight.setText("50");
        });

        onView(withId(R.id.body_next)).perform(click());

        intending(hasComponent(AgeSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }
}
