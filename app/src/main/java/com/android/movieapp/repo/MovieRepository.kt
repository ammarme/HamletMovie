package com.android.movieapp.repo

import com.android.movieapp.model.MovieResponse
import com.android.movieapp.model.SearchResponse

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): MovieResponse
    suspend fun searchMovies(query: String, page: Int): SearchResponse
}