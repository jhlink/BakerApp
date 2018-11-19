package com.example.oliverh.bakerapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetails;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSubstring;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectRecipeDetailsScreenTest {

    @Rule
    public IntentsTestRule<SelectRecipeDetails> mIntentRule = new IntentsTestRule<SelectRecipeDetails>(SelectRecipeDetails.class) {
        @Override
        protected void beforeActivityLaunched() {
            AppDatabase appDatabase = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext());
            RecipeRepository.getInstance(InstrumentationRegistry.getTargetContext(), appDatabase);
            super.beforeActivityLaunched();
        }

        @Override
        public SelectRecipeDetails launchActivity(@Nullable Intent startIntent) {
            Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), SelectRecipeDetails.class);

            String bundleRecipeStringKey = InstrumentationRegistry.getTargetContext().getString(R.string.BUNDLE_RECIPE_ID);
            intent.putExtra(bundleRecipeStringKey, 1);

            return super.launchActivity(intent);
        }
    };


    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

    }

    @Test
    public void validateReceivedIntentFromSelectRecipe() {
        String RECIPE_BUNDLE_KEY = mIntentRule.getActivity().getString(R.string.BUNDLE_RECIPE_ID);

        Intent receivedIntent = mIntentRule.getActivity().getIntent();
        assertThat(receivedIntent, hasExtra(RECIPE_BUNDLE_KEY, 1));
        assertThat(receivedIntent, hasComponent(SelectRecipeDetails.class.getCanonicalName()));
    }

    @Test
    public void checkIfRecipeIsLoaded() {
        onView(withId(R.id.rv_generic_container)).check(matches(isDisplayed()));

        onView(withText("Recipe Introduction")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfIngredientListIsLoaded() {
        onView(withId(R.id.rv_generic_container)).perform(RecyclerViewActions.actionOnItem(withText("Recipe Ingredients"), click()));
        onView(withId(R.id.rv_generic_container)).perform(RecyclerViewActions.scrollToPosition(5));

        // This test isn't very flexible given that we assume that the recipe in element 1 is
        //  a recipe for Brownies and a constant, hardcoded string is used to validate
        //  that the Ingredients list is properly loaded.
        onView(withSubstring("unsalted butter")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfRecipeStepIsLoaded() {
        onView(withId(R.id.rv_generic_container)).perform(RecyclerViewActions.actionOnItem(withSubstring("Step 1"), click()));

        // This test isn't very flexible given that we assume that the recipe in element 1 is
        //  a recipe for Brownies and a constant, hardcoded string is used to validate
        //  that the Recipe Step is properly loaded.
        onView(withSubstring("Step 1")).check(matches(isDisplayed()));
    }
}
