package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetailsFragmentViewModel;

public class ViewRecipeStepFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final int mRecipeId;
    private final int mStepId;

    public ViewRecipeStepFragmentViewModelFactory(int recipeId, int stepId) {
        mRecipeId = recipeId;
        mStepId = stepId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ViewRecipeStepViewModel(mRecipeId, mStepId);
    }
}

