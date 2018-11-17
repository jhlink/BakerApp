package com.example.oliverh.bakerapp;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.oliverh.bakerapp.ui.selectrecipe.SelectRecipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectRecipeScreenTest {
    @Rule
    public ActivityTestRule<SelectRecipe> mActivityTestRule = new ActivityTestRule<>(SelectRecipe.class);

    @Test
    public void checkIfRecipesAreLoadedInPortrait() {
        setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.rv_generic_container)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_generic_container)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText("Recipe Ingredients")).check(matches(isDisplayed()));
    }

    @Test
    public void checkIfRecipesAreLoadedInLandscape() {
        setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.rv_generic_container)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_generic_container)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText("Recipe Ingredients")).check(matches(isDisplayed()));

    }

    private void setActivityOrientation(int activityInfoConstant) {
        mActivityTestRule.getActivity().setRequestedOrientation(activityInfoConstant);
    }
}
