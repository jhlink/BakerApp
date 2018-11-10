package com.example.oliverh.bakerapp.ui.selectstep;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class SelectRecipeDetailsFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final int mRecipeId;

    public SelectRecipeDetailsFragmentViewModelFactory(int recipeId) {
        mRecipeId = recipeId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SelectRecipeDetailsFragmentViewModel(mRecipeId);
    }
}

