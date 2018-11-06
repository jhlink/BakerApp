package com.example.oliverh.bakerapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import timber.log.Timber;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = AppDatabase.class;
    private static final String DATABASE_NAME = "recipeDb";
    private static volatile AppDatabase ourInstance;

    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredient recipeIngredientDao();
    public abstract RecipeStep recipeStepDao();

    public static AppDatabase getInstance(final Context context) {
        if (ourInstance == null) {
            synchronized (LOCK) {
                Timber.d("Creating new database instance");
                ourInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        //.allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Timber.d( "Getting database instance");
        return ourInstance;
    }
}
