package com.example.oliverh.bakerapp.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.oliverh.bakerapp.AppExecutors;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.data.database.RecipeDao;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;
import com.example.oliverh.bakerapp.data.database.RecipeIngredientDao;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.database.RecipeStepDao;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;
import com.example.oliverh.bakerapp.data.network.utils.RecipeNetworkAPI;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private MediatorLiveData<RepositoryResponse> mediatorLiveDataRecipeList = new MediatorLiveData<>();
    private MutableLiveData<RepositoryResponse> repoResponse;

    private final Context mContext;

    private RecipeRepository(Context context, final AppDatabase database) {
        mDb = database;
        mContext = context;
        mRecipeDao = mDb.recipeDao();
        mRecipeIngredientDao = mDb.recipeIngredientDao();
        mRecipeStepDao = mDb.recipeStepDao();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //mRecipeDao.nukeTable();
                //mRecipeStepDao.nukeTable();
                //mRecipeIngredientDao.nukeTable();
            }
        });

        LiveData<RepositoryResponse> dbSource =
                Transformations.map(mRecipeDao.loadAllRecipes(), new Function<List<Recipe>, RepositoryResponse>(){
                    @Override
                    public RepositoryResponse apply(List<Recipe> recipes) {
                        if (recipes.isEmpty()) {
                            // On initial load, there is no data. Doesn't mean that this should be an error.
                            return new RepositoryResponse(new IOException("No Data"));
                        }
                        return new RepositoryResponse(recipes);
                    }
                });

        mediatorLiveDataRecipeList.addSource(dbSource, new Observer<RepositoryResponse>() {
            @Override
            public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                Timber.d("Initialized Mediator Live Data");
                mediatorLiveDataRecipeList.postValue(repositoryResponse);
            }
        });
    }

    public static RecipeRepository getInstance(final Context context, final AppDatabase database) {
        if (sInstance == null) {
            synchronized (RecipeRepository.class) {
                if (sInstance == null) {
                    sInstance = new RecipeRepository(context, database);
                }
            }
        }
        return sInstance;
    }

    public static RecipeRepository getExistingInstance() {
        Timber.d("Retrieving existing instance.");
        if (sInstance != null) {
            Timber.d("Found existing instance.");
            return sInstance;
        }
        Timber.d("Repository instance not found.");
        return null;
    }

    public void fetchRecipeListData() {
        Timber.d("Execute API request for Recipe List");
        Call recipeListCall = RecipeNetworkAPI.getRecipeListDump(mContext);
        getData(recipeListCall);

        // TODO: Test recipeRequest using IdlingResource in Espresso.
        //String test = context.getString(R.string.test_json_recipe_list);
        //Timber.d("Result -- %s : ", test);
    }


    public LiveData<RepositoryResponse> getRecipeList() {
        fetchRecipeListData();
        return mediatorLiveDataRecipeList;
    }

    public List<RecipeIngredient> getRawIngredientsList(int recipeId) {
        List<RecipeIngredient> ingredients = mRecipeIngredientDao.getRawListOfRecipeIngredientsById(recipeId);

        if (ingredients == null || ingredients.isEmpty()) {
            ingredients = new ArrayList<>();
        }

        return ingredients;
    }

    public LiveData<RepositoryResponse> getRecipeStep(final int recipeId, final int stepId) {

        if (repoResponse == null) {
            repoResponse = new MutableLiveData<>();
        }

        queryRecipeStep(recipeId, stepId);

        return repoResponse;
    }

    public void queryRecipeStep(final int recipeId, final int stepId) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                RecipeStep recipeStep = mRecipeStepDao.getRecipeStepByRecipeId(recipeId, stepId);

                if (recipeStep == null) {
                    Timber.e("Error: RecipeStep object doesn't exist");
                    repoResponse.postValue(new RepositoryResponse(new IOException("Object does not exist")));
                    return;
                }

                Timber.d("Queried Step: %s", recipeStep.toString());
                repoResponse.postValue(new RepositoryResponse(recipeStep));
            }
        });

    }

    public LiveData<RepositoryResponse> getRecipeSteps(final int recipeId) {
        LiveData<List<RecipeStep>> tRecipeSteps =  mRecipeStepDao.getRecipeStepsByRecipeId(recipeId);

        Timber.d("Requested Recipe Step List - Recipe ID: %d", recipeId);

        LiveData<RepositoryResponse> result =
                Transformations.map(tRecipeSteps, new Function<List<RecipeStep>, RepositoryResponse>() {
                    @Override
                    public RepositoryResponse apply(List<RecipeStep> input) {
                        return new RepositoryResponse(input);
                    }
                });

        return result;
    }

    public LiveData<RepositoryResponse> getRecipeIngredients(final int recipeId) {
        LiveData<List<RecipeIngredient>> ingredientLiveData = mRecipeIngredientDao.getRecipeIngredientsById(recipeId);

        LiveData<RepositoryResponse> result =
                Transformations.map(ingredientLiveData, new Function<List<RecipeIngredient>, RepositoryResponse>() {
                    @Override
                    public RepositoryResponse apply(List<RecipeIngredient> input) {
                        return new RepositoryResponse(input);
                    }
                });

        return result;
    }

    private synchronized void getData(final Call apiCall) {

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                apiCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Timber.d("-- API Request[Fail]: %s", e.getMessage());
                        mediatorLiveDataRecipeList.postValue(new RepositoryResponse(e));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            //Timber.d("-- API Request[Success]: %s", responseString);

                            final List<Recipe> parsedData = jsonParser(responseString);
                            //printRecipeList(parsedData);

                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mRecipeDao.insertRecipes(parsedData);
                                    for (Recipe recipe : parsedData) {
                                        mRecipeIngredientDao.insertRecipeIngredients(recipe.getIngredients());
                                        mRecipeStepDao.insertRecipeSteps(recipe.getSteps());
                                    }

                                }
                            });
                        }
                    }
                })
                ;
            }
        });
    }

    private List<Recipe> jsonParser(String jsonResponse) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<RecipeIngredient> recipeIngredientJsonAdapter = moshi.adapter(RecipeIngredient.class);
        JsonAdapter<RecipeStep> recipeStepJsonAdapter = moshi.adapter(RecipeStep.class);
        JsonAdapter<Recipe> recipeJsonAdapter = moshi.adapter(Recipe.class);

        moshi = moshi.newBuilder()
                .add(RecipeIngredient.class, recipeIngredientJsonAdapter)
                .add(RecipeStep.class, recipeStepJsonAdapter)
                .add(Recipe.class, recipeJsonAdapter)
            .build();

        Type recipeListType = Types.newParameterizedType(List.class, Recipe.class);
        JsonAdapter<List<Recipe>> jsonAdapter = moshi.adapter(recipeListType);

        List<Recipe> recipes = jsonAdapter.fromJson(jsonResponse);

        primeRecipes(recipes);

        //Timber.d("Object %s : ", jsonResponse);
        //printRecipeList(recipes);

        return recipes;
    }

    private void primeRecipes( List<Recipe> recipeList ) {
        if (recipeList.isEmpty()) {
            return ;
        }
        for (Recipe element : recipeList) {
            element.initalizeRecipeIdInRecipeSubtypes();
        }
    }

    private void printRecipeList( List<Recipe> recipeList ) {
        if (recipeList.isEmpty()) {
            return;
        }
        for (Recipe element : recipeList) {
            Timber.d(element.toString());
            printObjectList(element.getIngredients());
            printObjectList(element.getSteps());
        }
    }

    private void printObjectList ( List<?> slist ) {
        if (slist.isEmpty()) {
            return;
        }
        for (Object element : slist) {
            Timber.d(element.toString());
        }
    }
}
