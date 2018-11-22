package com.example.oliverh.bakerapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepHolder;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepTextFragment;

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
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewRecipeStepDetailsScreenTest_Mobile {

    //
    @Rule
    public IntentsTestRule<ViewRecipeStepHolder> mIntentRule = new IntentsTestRule<ViewRecipeStepHolder>(ViewRecipeStepHolder.class) {
        @Override
        protected void beforeActivityLaunched() {
            AppDatabase appDatabase = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext());
            RecipeRepository.getInstance(InstrumentationRegistry.getTargetContext(), appDatabase);
            super.beforeActivityLaunched();
        }

        @Override
        public ViewRecipeStepHolder launchActivity(@Nullable Intent startIntent) {
            Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), ViewRecipeStepHolder.class);

            String bundleRecipeIdKey = InstrumentationRegistry.getTargetContext().getString(R.string.BUNDLE_RECIPE_ID);
            String bundleStepIdKey = InstrumentationRegistry.getTargetContext().getString(R.string.BUNDLE_STEP_ID);
            String bundleVSStateKey = InstrumentationRegistry.getTargetContext().getString(R.string.BUNDLE_STEP_STATE);
            String bundleIsTabletKey = ViewRecipeStepTextFragment.ARG_IS_NEXT_BTN_VISIBLE;

            intent.putExtra(bundleRecipeIdKey, 1);
            intent.putExtra(bundleStepIdKey, 2);
            intent.putExtra(bundleVSStateKey, 1);
            intent.putExtra(bundleIsTabletKey, false);

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
        String STEP_ID_BUNDLE_KEY = mIntentRule.getActivity().getString(R.string.BUNDLE_STEP_ID);
        String VS_STATE_BUNDLE_KEY = mIntentRule.getActivity().getString(R.string.BUNDLE_STEP_STATE);
        String IS_TABLET_BOOLEAN_BUNDLE_KEY = ViewRecipeStepTextFragment.ARG_IS_NEXT_BTN_VISIBLE;

        Intent receivedIntent = mIntentRule.getActivity().getIntent();
        assertThat(receivedIntent, hasExtra(RECIPE_BUNDLE_KEY, 1));
        assertThat(receivedIntent, hasExtra(STEP_ID_BUNDLE_KEY, 2));
        assertThat(receivedIntent, hasExtra(VS_STATE_BUNDLE_KEY, 1));
        assertThat(receivedIntent, hasExtra(IS_TABLET_BOOLEAN_BUNDLE_KEY, false));
        assertThat(receivedIntent, hasComponent(ViewRecipeStepHolder.class.getCanonicalName()));
    }

    @Test
    public void checkIfVideoPlayerHasLoaded() {
        onView(withId(R.id.recipePlayerView)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfNextButtonExists() {
        onView(withId(R.id.btn_nextStep)).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfRecipeStepHeaderTextLoads() {
        String stepHeaderText = "Step 2";
        onView(withId(R.id.tv_recipeStepHeader)).check(matches(withText(stepHeaderText)));
    }

    @Test
    public void checkIfNextStepProperlyLoadsNextRecipeStep() {
        String nextStepHeaderText = "Step 3";

        checkIfRecipeStepHeaderTextLoads();

        onView(withId(R.id.btn_nextStep)).perform(click());
        onView(withId(R.id.tv_recipeStepHeader)).check(matches(withText(nextStepHeaderText)));
    }
}
