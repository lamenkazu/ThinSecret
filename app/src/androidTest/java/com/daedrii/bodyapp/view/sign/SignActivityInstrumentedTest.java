package com.daedrii.bodyapp.view.sign;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

import android.app.Instrumentation;


import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.view.sign.signup.InitSignUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SignActivity> activityRule = new ActivityScenarioRule<>(SignActivity.class);

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
        ActivityScenario<SignActivity> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.newSignUp);
            assertNotNull(activity.goToSignIn);
        });
    }

    @Test
    public void ShouldGoToInitSignUpWhenButtonIsClicked() {

        onView(withId(R.id.newUserButton)).perform(click());

        intending(hasComponent(InitSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));

    }

    @Test
    public void ShouldGoToSignInActivityWhenButtonIsClicked() {
        onView(withId(R.id.btn_goto_sign_in)).perform(click());

        intending(hasComponent(SignInActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));
    }

    @Test
    public void CheckLifeCycleOfActivity() {
        ActivityScenario<SignActivity> scenario = activityRule.getScenario();

        //OnCreated já é testada no primeiro teste de InitComponents
        //Não há assertions para uma View após ser destruida.
        //Então foi checado apenas o Started e o Resumed da Activity.

        activityRule.getScenario().moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            // Verificar se a atividade está visível na tela
            assertTrue(activity.getWindow().getDecorView().isShown());

        });

        activityRule.getScenario().moveToState(Lifecycle.State.RESUMED);
        // Faça as verificações necessárias para o estado RESUMED
        scenario.onActivity(activity -> {
            // Verificar se a atividade está interativa e pronta para receber interações do usuário
            Stage stage = ActivityLifecycleMonitorRegistry.getInstance().getLifecycleStageOf(activity);
            assertEquals(Stage.RESUMED, stage);

        });

    }
}
