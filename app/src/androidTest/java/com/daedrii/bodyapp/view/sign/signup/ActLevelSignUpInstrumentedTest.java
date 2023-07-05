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
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class ActLevelSignUpInstrumentedTest {
    @Rule
    public ActivityScenarioRule<ActLevelSignUp> ActLvlActivityRule = new ActivityScenarioRule<>(ActLevelSignUp.class);

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
        ActivityScenario<ActLevelSignUp> goalScenario = ActLvlActivityRule.getScenario();
        goalScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.next);
            assertNotNull(activity.radioGroup);
        });

    }


    @Test
    public void ShouldNotGoToNextScreenWhenFieldIsEmpty() {

        ActivityScenario<ActLevelSignUp> goalScenario = ActLvlActivityRule.getScenario();

        onView(withId(R.id.act_next)).perform(click());

        goalScenario.onActivity(activity -> {
            // Chame o método que pode lançar a exceção
            assertFalse(activity.decide());
        });
    }

    @Test
    public void ShouldSetActLvlAsSedentary(){
        ActivityScenario<ActLevelSignUp> goalScenario = ActLvlActivityRule.getScenario();

        onView(withId(R.id.btn_sedentary)).perform(click());

        onView(withId(R.id.act_next)).perform(click());

        assertEquals(BodyInfo.ActLevel.SEDENTARY, SignUpController.getNewBodyInfo().getActLevel());

        intending(hasComponent(BodySignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetActLevelAsLowActive(){
        ActivityScenario<ActLevelSignUp> goalScenario = ActLvlActivityRule.getScenario();

        onView(withId(R.id.btn_low)).perform(click());

        onView(withId(R.id.act_next)).perform(click());

        assertEquals(BodyInfo.ActLevel.LOW_ACTIVE, SignUpController.getNewBodyInfo().getActLevel());

        intending(hasComponent(BodySignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetActLevelAsMidActive(){
        ActivityScenario<ActLevelSignUp> goalScenario = ActLvlActivityRule.getScenario();

        onView(withId(R.id.btn_medium)).perform(click());

        onView(withId(R.id.act_next)).perform(click());

        assertEquals(BodyInfo.ActLevel.ACTIVE, SignUpController.getNewBodyInfo().getActLevel());

        intending(hasComponent(BodySignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetActLevelAsHighActive(){
        ActivityScenario<ActLevelSignUp> goalScenario = ActLvlActivityRule.getScenario();

        onView(withId(R.id.btn_high)).perform(click());

        onView(withId(R.id.act_next)).perform(click());

        assertEquals(BodyInfo.ActLevel.HIGH_ACTIVE, SignUpController.getNewBodyInfo().getActLevel());

        intending(hasComponent(BodySignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetActLevelAsExtraActive(){
        ActivityScenario<ActLevelSignUp> goalScenario = ActLvlActivityRule.getScenario();

        onView(withId(R.id.btn_extreme)).perform(click());

        onView(withId(R.id.act_next)).perform(click());

        assertEquals(BodyInfo.ActLevel.EXTREME_ACTIVE, SignUpController.getNewBodyInfo().getActLevel());

        intending(hasComponent(BodySignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }
}
