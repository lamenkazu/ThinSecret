package com.daedrii.bodyapp.view.sign.signup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class AgeSignUpInstrumentedTest {
    @Rule
    public ActivityScenarioRule<AgeSignUp> ageActivityRule = new ActivityScenarioRule<>(AgeSignUp.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void ShouldInitComponentSuccessfully() {

        ActivityScenario<AgeSignUp> ageScenario = ageActivityRule.getScenario();
        ageScenario.onActivity(activity -> {
            // Verifica se os componentes são inicializados corretamente
            assertNotNull(activity.next);
            assertNotNull(activity.txtDate);
            assertNotNull(activity.materialDatePicker);
        });

    }

    @Test
    public void ShouldSetAgeSuccessfully() {

        ActivityScenario<AgeSignUp> ageScenario = ageActivityRule.getScenario();

        onView(withId(R.id.age_txt_date))
                .perform(replaceText("01/12/1999"));

        onView(withId(R.id.age_next))
                .perform(click());

        assertEquals(23, SignUpController.getNewBodyInfo().getAge().intValue());

        intending(hasComponent(UserSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));


    }

    @Test
    public void ShouldNotSetAgeIfItIsLessThanMinimalAge(){
        ActivityScenario<AgeSignUp> ageScenario = ageActivityRule.getScenario();

        onView(withId(R.id.age_txt_date))
                .perform(replaceText("05/07/2022"));

        onView(withId(R.id.age_next))
                .perform(click());

        ageScenario.onActivity(activity -> {
            assertFalse(activity.decide(1, 14, activity.txtDate.getText().toString()));
        });

        intending(hasComponent(UserSignUp.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));


    }


}
