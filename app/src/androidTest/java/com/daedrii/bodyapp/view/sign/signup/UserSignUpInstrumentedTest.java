package com.daedrii.bodyapp.view.sign.signup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doAnswer;

import android.app.Instrumentation;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.exceptions.EmptyFieldException;
import com.daedrii.bodyapp.view.home.HomeActivity;

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
public class UserSignUpInstrumentedTest {
    @Mock
    SignUpController mockSignUpController;
    @Captor
    ArgumentCaptor<Consumer<Boolean>> successCaptor;
    @Rule
    public ActivityScenarioRule<UserSignUp> userActivityRule = new ActivityScenarioRule<>(UserSignUp.class);

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
        ActivityScenario<UserSignUp> userScenario = userActivityRule.getScenario();
        userScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.done);
            assertNotNull(activity.name);
            assertNotNull(activity.phone);
            assertNotNull(activity.email);
            assertNotNull(activity.password);
            assertNotNull(activity.passwordConfirm);
            assertNotNull(activity.progressIndicator);
        });
    }

    @Test
    public void ShouldNotGoToNextScreenWhenFieldIsEmpty() {

        ActivityScenario<UserSignUp> userScenario = userActivityRule.getScenario();

        onView(withId(R.id.user_done)).perform(click());

        userScenario.onActivity(activity -> {
            String userName = activity.name.getText().toString();
            String userPhone = activity.phone.getText().toString();
            String userMail = activity.email.getText().toString();
            String userPassword = activity.password.getText().toString();
            String userPasswordConfirm = activity.passwordConfirm.getText().toString();
            // Chame o método que pode lançar a exceção
            assertFalse(activity.decide(userName, userPhone, userMail, userPassword, userPasswordConfirm));
        });
    }

    @Test
    public void ShouldMakeSignUpSuccessfully(){
        ActivityScenario<UserSignUp> userScenario = userActivityRule.getScenario();

        userScenario.onActivity(activity -> {

            activity.name.setText("Teste testoso");
            activity.phone.setText("3131313131");
            activity.email.setText("teste@mail.com");
            activity.password.setText("senha123");
            activity.passwordConfirm.setText("senha123");
        });

        onView(withId(R.id.user_done)).perform(click());

        userScenario.onActivity(activity -> {

            // Configuração do mock para simular um login bem-sucedido
            String userName = activity.name.getText().toString();
            String userPhone = activity.phone.getText().toString();
            String userMail = activity.email.getText().toString();
            String userPassword = activity.password.getText().toString();
            String userPasswordConfirm = activity.passwordConfirm.getText().toString();

            doAnswer(invocation -> {
                // Obtém o callback passado como argumento
                Consumer<Boolean> successCallback = invocation.getArgument(2);

                // Chama o callback com sucesso
                successCallback.accept(true);

                return null;
            }).when(mockSignUpController).handleUserDataSignUp(userName, userPhone, userMail, userPassword, successCaptor.capture());

        });

        intending(hasComponent(HomeActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));



    }

    @Test
    public void ShouldNotSignUpIfPasswordNotMatches(){
        ActivityScenario<UserSignUp> userScenario = userActivityRule.getScenario();

        userScenario.onActivity(activity -> {

            activity.name.setText("Teste testoso");
            activity.phone.setText("3131313131");
            activity.email.setText("teste@mail.com");
            activity.password.setText("senha123");
            activity.passwordConfirm.setText("senha321");
        });

        onView(withId(R.id.user_done)).perform(click());

        userScenario.onActivity(activity -> {
            String userName = activity.name.getText().toString();
            String userPhone = activity.phone.getText().toString();
            String userMail = activity.email.getText().toString();
            String userPassword = activity.password.getText().toString();
            String userPasswordConfirm = activity.passwordConfirm.getText().toString();
            // Chame o método que pode lançar a exceção
            assertFalse(activity.decide(userName, userPhone, userMail, userPassword, userPasswordConfirm));
        });


    }
}
