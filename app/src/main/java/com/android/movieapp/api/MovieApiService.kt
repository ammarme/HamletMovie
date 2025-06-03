package com.android.movieapp.api

import com.android.movieapp.model.MovieResponse
import com.android.movieapp.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("discover/movie")
    suspend fun getPopularMovies(
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("region") region: String = "US",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en"
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en"
    ): SearchResponse
}