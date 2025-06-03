package com.android.movieapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.movieapp.model.Movie
import com.android.movieapp.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Idle)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val moviesList = mutableListOf<Movie>()

    private var currentPage = 1
    private var totalPages = 1

    fun loadMovies() {
        val currentState = _homeState.value
        if (currentState is HomeState.Loading) return
        if (currentState is HomeState.Success && currentState.isLastPage) return

        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            try {
                val response = repository.getPopularMovies(currentPage)

                totalPages = response.total_pages
                val lastPage = currentPage >= totalPages

                moviesList.addAll(response.results)

                _homeState.value = HomeState.Success(moviesList.toList(), lastPage)

                currentPage++
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun refresh() {
        currentPage = 1
        totalPages = 1
        moviesList.clear()
        _homeState.value = HomeState.Idle
        loadMovies()
    }
}
