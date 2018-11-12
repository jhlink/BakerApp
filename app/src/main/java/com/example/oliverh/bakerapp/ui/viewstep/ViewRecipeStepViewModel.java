package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

public class ViewRecipeStepViewModel extends ViewModel {
    private LiveData<RepositoryResponse> recipeStepLiveData;
    private RecipeRepository recipeRepository;

    public ViewRecipeStepViewModel(int recipeId, int stepId) {
        recipeRepository = RecipeRepository.getExistingInstance();
        recipeStepLiveData = recipeRepository.getRecipeStep(recipeId, stepId);
    }

    public LiveData<RepositoryResponse> getRecipeStep() {
        return recipeStepLiveData;
    }

    public void queryRecipe(int recipeId, int stepId) {
        recipeRepository.queryRecipeStep(recipeId, stepId);
    }

}
