package com.daedrii.bodyapp.view.sign;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.model.exceptions.InvalidUserException;
import com.daedrii.bodyapp.view.home.HomeActivity;
import com.daedrii.bodyapp.view.sign.signup.InitSignUp;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.function.Consumer;

@RunWith(AndroidJUnit4.class)
public class SignInActivityInstrumentedTest {

    @Mock
    SignUpController mockSignUpController;

    @Captor
    ArgumentCaptor<Consumer<Boolean>> successCaptor;
    @Rule
    public ActivityScenarioRule<SignInActivity> activityRule = new ActivityScenarioRule<>(SignInActivity.class);

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
        ActivityScenario<SignInActivity> scenario = activityRule.getScenario();
        scenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.mAuth);
            assertNotNull(activity.done);
            assertNotNull(activity.email);
            assertNotNull(activity.password);
            assertNotNull(activity.progressIndicator);
        });
    }

    @Test
    public void ShouldThrowAnExceptionWhenSomeFieldIsEmpty(){

        onView(withId(R.id.btn_sign_in)).perform(click());

        assertThrows(IllegalArgumentException.class,  () -> {
            // Código que pode lançar a exceção
            // Chama o método handleSignIn() com campos vazios
            SignUpController.handleSignIn("", "", success -> {});
        });
    }

    @Test
    public void ShouldNotAuthUserWhenDataIsWrong() {
        ActivityScenario<SignInActivity> scenario = activityRule.getScenario();

        scenario.onActivity(activity -> {
            // Simula o preenchimento do email e senha
            activity.email.setText("teste@mail.com");
            activity.password.setText("erick789");
        });

        scenario.onActivity(activity -> {

            // Configuração do mock para simular um login bem-sucedido

                doAnswer(invocation -> {
                    // Obtém o callback passado como argumento
                    Consumer<Boolean> successCallback = invocation.getArgument(2);

                    // Chama o callback com sucesso
                    successCallback.accept(false);

                    return null;
                }).when(mockSignUpController).handleSignIn(activity.email.getText().toString(), activity.password.getText().toString(), successCaptor.capture());


        });

        // Simula o clique no botão de login
        onView(withId(R.id.btn_sign_in)).perform(click());

        scenario.onActivity(activity -> {
            // Verifica se a barra de progresso está visível
            assertEquals(View.GONE, activity.progressIndicator.getVisibility());
        });
    }

    @Test
    public void ShouldMakeSignInSuccessfully(){
// Configuração do mock para simular um login bem-sucedido
        // Obtém o cenário da atividade
        ActivityScenario<SignInActivity> scenario = activityRule.getScenario();

        scenario.onActivity(activity -> {
            // Simula o preenchimento do email e senha
            activity.email.setText("teste@mail.com");
            activity.password.setText("senha123");

        });


        // Simula o clique no botão de login
        onView(withId(R.id.btn_sign_in)).perform(click());

        scenario.onActivity(activity -> {

                doAnswer(invocation -> {
                    // Obtém o callback passado como argumento
                    Consumer<Boolean> successCallback = invocation.getArgument(2);

                    // Chama o callback com sucesso
                    successCallback.accept(true);

                    return null;
                }).when(mockSignUpController).handleSignIn(activity.email.getText().toString(), activity.password.getText().toString(), successCaptor.capture());

        });

        intending(hasComponent(HomeActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));
    }
}
