package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.RecipeStep;

public class ViewRecipeStepViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private RecipeRepository recipeRepository;
    private LiveData<RecipeStep> recipeStepLiveData;
    private int recipeId;

    public ViewRecipeStepViewModel(int recipeId, int stepId) {
        recipeRepository = RecipeRepository.getExistingInstance();
        recipeStepLiveData = recipeRepository.getRecipeStep(recipeId, stepId);
    }

    public LiveData<RecipeStep> getRecipeStep() {
        return recipeStepLiveData;
    }
}
