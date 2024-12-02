package com.example.myapplication.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    ApiService requestApi;
    public HttpRequest(String baseUrl) {
        requestApi = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }
    public ApiService getApiSevice(){
        return requestApi;
    }
}
