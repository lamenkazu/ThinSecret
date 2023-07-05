package com.daedrii.bodyapp.view.sign.signup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
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

public class GoalSignUpInstrumentedTest {
    @Mock
    SignUpController mockSignUpController;
    @Captor
    ArgumentCaptor<Consumer<Boolean>> successCaptor;
    @Rule
    public ActivityScenarioRule<GoalSignUp> goalActivityRule = new ActivityScenarioRule<>(GoalSignUp.class);


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
        ActivityScenario<GoalSignUp> goalScenario = goalActivityRule.getScenario();
        goalScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.next);
            assertNotNull(activity.radioGroup);
        });

    }


    @Test
    public void ShouldNotGoToNextScreenWhenFieldIsEmpty() {

        ActivityScenario<GoalSignUp> goalScenario = goalActivityRule.getScenario();

        onView(withId(R.id.goal_next)).perform(click());

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
    public void ShouldSetGoalAsLoss(){
        ActivityScenario<GoalSignUp> goalScenario = goalActivityRule.getScenario();

        onView(withId(R.id.btn_loss)).perform(click());

        onView(withId(R.id.goal_next)).perform(click());

        assertEquals(BodyInfo.DietGoal.LOSS, SignUpController.getNewBodyInfo().getGoal());

        intending(hasComponent(DietTypeSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetGoalAsKeep(){
        ActivityScenario<GoalSignUp> goalScenario = goalActivityRule.getScenario();

        onView(withId(R.id.btn_keep)).perform(click());

        onView(withId(R.id.goal_next)).perform(click());

        assertEquals(BodyInfo.DietGoal.KEEP, SignUpController.getNewBodyInfo().getGoal());

        intending(hasComponent(DietTypeSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldSetGoalAsGain(){
        ActivityScenario<GoalSignUp> goalScenario = goalActivityRule.getScenario();

        onView(withId(R.id.btn_gain)).perform(click());

        onView(withId(R.id.goal_next)).perform(click());

        assertEquals(BodyInfo.DietGoal.GAIN, SignUpController.getNewBodyInfo().getGoal());

        intending(hasComponent(DietTypeSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }



}
