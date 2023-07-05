package com.daedrii.bodyapp.view.sign.signup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import android.app.Instrumentation;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.user.BodyInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DietTypeSigUpInstrumentedTest {
    @Rule
    public ActivityScenarioRule<DietTypeSignUp> dietActivityRule = new ActivityScenarioRule<>(DietTypeSignUp.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void ShouldInitComponentsSuccessfully() {

        ActivityScenario<DietTypeSignUp> initScenario = dietActivityRule.getScenario();
        initScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.next);
            assertNotNull(activity.radioGroup);
            assertNotNull(activity.lowCarb1);
            assertNotNull(activity.lowCarb2);
            assertNotNull(activity.midCarb1);
            assertNotNull(activity.midCarb2);
            assertNotNull(activity.highCarb1);
            assertNotNull(activity.highCarb2);
        });
    }

    @Test
    public void ShouldNotGoToNextScreenWhenFieldIsEmpty() {

        ActivityScenario<DietTypeSignUp> goalScenario = dietActivityRule.getScenario();

        onView(withId(R.id.diet_next)).perform(click());

//        // Verifique a exceção dentro do contexto do ActivityScenario
//        goalActivityRule.getScenario().onActivity(activity -> {
//            assertThrows(EmptyFieldException.class, () -> {
//                activity.decide();
//            });
//        });

        goalScenario.onActivity(activity -> {
            // Chame o método que pode lançar a exceção
            assertFalse(activity.decide());
        });
    }

    @Test
    public void ShouldSetDietAsLowCarb(){
        ActivityScenario<DietTypeSignUp> dietScenario = dietActivityRule.getScenario();

        onView(withId(R.id.btn_lowcarb)).perform(click());

        dietScenario.onActivity(activity -> {
            assertEquals(View.VISIBLE, activity.lowCarb1.getVisibility());
            assertEquals(View.VISIBLE, activity.lowCarb2.getVisibility());

            assertEquals(View.GONE, activity.midCarb1.getVisibility());
            assertEquals(View.GONE, activity.midCarb2.getVisibility());
            assertEquals(View.GONE, activity.highCarb1.getVisibility());
            assertEquals(View.GONE, activity.highCarb2.getVisibility());
        });

        onView(withId(R.id.diet_next)).perform(click());

        assertEquals(BodyInfo.DietType.LowCarb, SignUpController.getNewBodyInfo().getDiet());

        intending(hasComponent(GenderSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));
    }

    @Test
    public void ShouldSetDietAsMidCarb(){
        ActivityScenario<DietTypeSignUp> dietScenario = dietActivityRule.getScenario();

        onView(withId(R.id.btn_midcarb)).perform(click());

        dietScenario.onActivity(activity -> {
            assertEquals(View.GONE, activity.lowCarb1.getVisibility());
            assertEquals(View.GONE, activity.lowCarb2.getVisibility());

            assertEquals(View.VISIBLE, activity.midCarb1.getVisibility());
            assertEquals(View.VISIBLE, activity.midCarb2.getVisibility());

            assertEquals(View.GONE, activity.highCarb1.getVisibility());
            assertEquals(View.GONE, activity.highCarb2.getVisibility());
        });

        onView(withId(R.id.diet_next)).perform(click());

        assertEquals(BodyInfo.DietType.MidCarb, SignUpController.getNewBodyInfo().getDiet());

        intending(hasComponent(GenderSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));
    }

    @Test
    public void ShouldSetDietAsHighCarb(){
        ActivityScenario<DietTypeSignUp> dietScenario = dietActivityRule.getScenario();

        onView(withId(R.id.btn_highcarb)).perform(click());

        dietScenario.onActivity(activity -> {
            assertEquals(View.GONE, activity.lowCarb1.getVisibility());
            assertEquals(View.GONE, activity.lowCarb2.getVisibility());
            assertEquals(View.GONE, activity.midCarb1.getVisibility());
            assertEquals(View.GONE, activity.midCarb2.getVisibility());

            assertEquals(View.VISIBLE, activity.highCarb1.getVisibility());
            assertEquals(View.VISIBLE, activity.highCarb2.getVisibility());
        });

        onView(withId(R.id.diet_next)).perform(click());

        assertEquals(BodyInfo.DietType.HighCarb, SignUpController.getNewBodyInfo().getDiet());

        intending(hasComponent(GenderSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));
    }

}
