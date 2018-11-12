package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import timber.log.Timber;

public class ViewRecipeStepViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int mRecipeId;
    private int mStepId;

    public ViewRecipeStepViewModelFactory(int recipeId, int stepId) {
        mRecipeId = recipeId;
        mStepId = stepId;
        Timber.d("ViewModelFactory | Constructory - recipeId %d, stepId %d", mRecipeId, mStepId);
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Timber.d("ViewModelFactory - recipeId %d, stepId %d", mRecipeId, mStepId);
        return (T) new ViewRecipeStepViewModel(mRecipeId, mStepId);
    }
}

