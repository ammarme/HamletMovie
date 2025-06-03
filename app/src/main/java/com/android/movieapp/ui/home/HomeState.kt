package com.android.movieapp.ui.home

import com.android.movieapp.model.Movie

sealed class HomeState {
    data object Idle : HomeState()
    data object Loading : HomeState()
    data class Success(val movies: List<Movie>, val isLastPage: Boolean) : HomeState()
    data class Error(val message: String) : HomeState()
}
