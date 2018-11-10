package com.example.oliverh.bakerapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

public class SelectRecipeDetailsFragmentViewModel extends ViewModel {
    private final RecipeRepository mRepository;
    private final LiveData<RepositoryResponse> recipeSteps;

    public SelectRecipeDetailsFragmentViewModel(int recipeId) {
        mRepository = RecipeRepository.getExistingInstance();
        recipeSteps = mRepository.getRecipeSteps(recipeId);
    }

    public LiveData<RepositoryResponse> getRecipeSteps() {
        return recipeSteps;
    }
}
