package com.android.movieapp.repo

import com.android.movieapp.api.MovieApiService
import com.android.movieapp.model.MovieResponse
import com.android.movieapp.model.SearchResponse
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): MovieResponse {
        return apiService.getPopularMovies(page = page)
    }

    override suspend fun searchMovies(query: String, page: Int): SearchResponse {
        return apiService.searchMovies(query = query, page = page)
    }
}