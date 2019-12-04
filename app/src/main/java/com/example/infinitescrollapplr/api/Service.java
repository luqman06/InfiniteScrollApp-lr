package com.example.infinitescrollapplr.api;

import com.example.infinitescrollapplr.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface Service {

    @GET("movie/popular")
    Call<MovieResponse> gettPopularMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

}
