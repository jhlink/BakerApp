package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

public class SelectRecipeViewModel extends AndroidViewModel {
    private final RecipeRepository mRepository;
    private final LiveData<RepositoryResponse> recipes;

    public SelectRecipeViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mRepository = RecipeRepository.getInstance(application, appDatabase);
        recipes = mRepository.getRecipeList();
    }

    public LiveData<RepositoryResponse> getRecipes() {
        return recipes;
    }
}
