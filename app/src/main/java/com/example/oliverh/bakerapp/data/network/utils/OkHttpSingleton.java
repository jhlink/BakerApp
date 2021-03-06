
package com.example.oliverh.bakerapp.data.network.utils;

import com.example.oliverh.bakerapp.testUtils.IdlingResources;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class OkHttpSingleton {

    private static OkHttpSingleton singletonInstance;

    private OkHttpClient client;

    private OkHttpSingleton() {
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .retryOnConnectionFailure(true)
                .build();
        IdlingResources.registerOkHttp(client);
    }

    public static OkHttpSingleton getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new OkHttpSingleton();
        }
        return singletonInstance;
    }

    public OkHttpClient getClient() { return client; }
}
