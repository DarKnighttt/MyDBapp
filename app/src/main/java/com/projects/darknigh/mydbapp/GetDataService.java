package com.projects.darknigh.mydbapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("/bins/kkbn8")
    Call<List<Film>> getAllFilms();
}
