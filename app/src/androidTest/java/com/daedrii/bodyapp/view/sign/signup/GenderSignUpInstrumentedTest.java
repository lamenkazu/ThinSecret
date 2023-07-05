package com.daedrii.bodyapp.view.sign.signup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

@RunWith(AndroidJUnit4.class)
public class GenderSignUpInstrumentedTest {

    @Rule
    public ActivityScenarioRule<GenderSignUp> genderActivityRule = new ActivityScenarioRule<>(GenderSignUp.class);

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
        ActivityScenario<GenderSignUp> genderScenario = genderActivityRule.getScenario();
        genderScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.next);
            assertNotNull(activity.orientationToggle);
            assertNotNull(activity.orientationButton);
            assertNotNull(activity.genderToggle);
            assertNotNull(activity.girlButton);
            assertNotNull(activity.boyButton);
        });
    }

    @Test
    public void ShouldNotGoToNextScreenWhenFieldIsEmpty() {

        ActivityScenario<GenderSignUp> genderScenario = genderActivityRule.getScenario();

        onView(withId(R.id.gender_next)).perform(click());

        genderScenario.onActivity(activity -> {
            // Chame o método que pode lançar a exceção
            assertFalse(activity.decide());
        });
    }

    @Test
    public void ShouldSetGenderAsCisMale(){
        ActivityScenario<GenderSignUp> genderScenario = genderActivityRule.getScenario();

        onView(withId(R.id.toggle_boy)).perform(click());

        onView(withId(R.id.gender_next)).perform(click());

        assertEquals(BodyInfo.Sex.MASCULINO, SignUpController.getNewBodyInfo().getGender());
        assertFalse(SignUpController.getNewBodyInfo().getTransgender());

        intending(hasComponent(ActLevelSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetGenderAsTransMale(){
        ActivityScenario<GenderSignUp> genderScenario = genderActivityRule.getScenario();

        onView(withId(R.id.toggle_orientation_button)).perform(click());

        onView(withId(R.id.toggle_boy)).perform(click());

        onView(withId(R.id.gender_next)).perform(click());

        assertEquals(BodyInfo.Sex.MASCULINO, SignUpController.getNewBodyInfo().getGender());
        assertTrue(SignUpController.getNewBodyInfo().getTransgender());

        intending(hasComponent(ActLevelSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetGenderAsCisFem(){
        ActivityScenario<GenderSignUp> genderScenario = genderActivityRule.getScenario();

        onView(withId(R.id.toggle_girl)).perform(click());

        onView(withId(R.id.gender_next)).perform(click());

        assertEquals(BodyInfo.Sex.FEMININO, SignUpController.getNewBodyInfo().getGender());
        assertFalse(SignUpController.getNewBodyInfo().getTransgender());

        intending(hasComponent(ActLevelSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetGenderAsTransFem(){

        ActivityScenario<GenderSignUp> genderScenario = genderActivityRule.getScenario();

        onView(withId(R.id.toggle_girl)).perform(click());

        onView(withId(R.id.toggle_orientation_button)).perform(click());

        onView(withId(R.id.gender_next)).perform(click());

        assertEquals(BodyInfo.Sex.FEMININO, SignUpController.getNewBodyInfo().getGender());
        assertTrue(SignUpController.getNewBodyInfo().getTransgender());

        intending(hasComponent(ActLevelSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));


    }

}
