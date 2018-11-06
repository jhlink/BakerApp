package com.example.oliverh.bakerapp.data.network.utils;

import android.content.Context;

import com.example.oliverh.bakerapp.R;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RecipeNetworkAPI {

    // Resource IDs for strings used to construct Recipe URL
    private final static int RECIPE_LIST_URL_ENDPOINT = R.string.recipe_list_endpoint;

    private static final OkHttpClient client = OkHttpSingleton.getInstance().getClient();

    public static Call getRecipeListDump(Context context) {
        HttpUrl builtUri = HttpUrl.parse(context.getString(RECIPE_LIST_URL_ENDPOINT)).newBuilder()
                .build();

        Request request = new Request.Builder()
                .url(builtUri)
                .build();

        return client.newCall(request);
    }
}