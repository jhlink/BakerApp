package com.example.oliverh.bakerapp.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;

import com.example.oliverh.bakerapp.AppExecutors;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.data.database.RecipeDao;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;
import com.example.oliverh.bakerapp.data.database.RecipeIngredientDao;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.database.RecipeStepDao;
import com.example.oliverh.bakerapp.data.network.utils.RecipeNetworkAPI;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timber.log.Timber;

public class RecipeRepository {
    private static RecipeRepository sInstance;
    private final AppDatabase mDb;
    private RecipeDao mRecipeDao;
    private RecipeIngredientDao mRecipeIngredientDao;
    private RecipeStepDao mRecipeStepDao;

    private RecipeRepository(final AppDatabase database) {
        mDb = database;
        mRecipeDao = mDb.recipeDao();
        mRecipeIngredientDao = mDb.recipeIngredientDao();
        mRecipeStepDao = mDb.recipeStepDao();
    }

    public static RecipeRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (RecipeRepository.class) {
                if (sInstance == null) {
                    sInstance = new RecipeRepository(database);
                }
            }
        }
        return sInstance;
    }

    public void getRecipeListData(final Context context) {
        Timber.d("Execute API request for PopularMovies list");
        Call recipeListCall = RecipeNetworkAPI.getRecipeListDump(context);
        getData(recipeListCall, Recipe.class);
    }

    private void getData(final Call apiCall, final Type targetDataType) {
        final MutableLiveData<Recipe> movieApiResponse = new MutableLiveData<>();

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                apiCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Timber.d("-- API Request[Fail]: %s", e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Timber.d("-- API Request[Success]: %s", response.body().string());
                        }
                    }
                });
            }
        });
    }



}
