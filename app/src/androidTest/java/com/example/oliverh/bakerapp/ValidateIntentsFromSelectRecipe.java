package com.example.oliverh.bakerapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.oliverh.bakerapp.ui.selectrecipe.SelectRecipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ValidateIntentsFromSelectRecipe {
    @Rule
    public IntentsTestRule<SelectRecipe> mActivityRule = new IntentsTestRule<>(SelectRecipe.class);

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void validateIntentSendToSelectRecipeDetails() {
        String RECIPE_BUNDLE_KEY = mActivityRule.getActivity().getString(R.string.BUNDLE_RECIPE_ID);
        onView(withId(R.id.rv_generic_container)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_generic_container)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(hasShortClassName(".ui.selectstep.SelectRecipeDetails")));

        // This is really, really, really bad.
        //  It is assumed that there will be data supplied, and that the first element clicked possesses a recipe ID of 1.
        //  In production this would not be a good solution. Mocking / stubbing should be the way to go.
        intended(hasExtraWithKey(RECIPE_BUNDLE_KEY));
    }
}
