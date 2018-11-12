package com.example.oliverh.bakerapp.ui.selectstep;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

public class SelectRecipeDetailsFragmentViewModel extends ViewModel {
    private final RecipeRepository mRepository;
    private final LiveData<RepositoryResponse> recipeSteps;
    private final LiveData<RepositoryResponse> recipeIngredients;

    public SelectRecipeDetailsFragmentViewModel(int recipeId) {
        mRepository = RecipeRepository.getExistingInstance();
        recipeSteps = mRepository.getRecipeSteps(recipeId);
        recipeIngredients = mRepository.getRecipeIngredients(recipeId);
    }

    public LiveData<RepositoryResponse> getRecipeSteps() {
        return recipeSteps;
    }

    public LiveData<RepositoryResponse> getRecipeIngredients() {
        return recipeIngredients;
    }
}
