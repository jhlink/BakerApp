package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

public class ViewRecipeStepViewModel extends ViewModel {
    private LiveData<RepositoryResponse> recipeStepLiveData;

    public ViewRecipeStepViewModel(int recipeId, int stepId) {
        RecipeRepository recipeRepository = RecipeRepository.getExistingInstance();
        recipeStepLiveData = recipeRepository.getRecipeStep(recipeId, stepId);
    }

    public LiveData<RepositoryResponse> getRecipeStep() {
        return recipeStepLiveData;
    }
}
