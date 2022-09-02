package com.example.assignment;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Service {

    @GET("/api/")
    Call<JsonElement> getMatchData(@Query("results") String results);


}
