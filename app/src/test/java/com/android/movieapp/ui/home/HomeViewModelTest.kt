package com.android.movieapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.movieapp.model.Movie
import com.android.movieapp.model.MovieResponse
import com.android.movieapp.repo.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadMovies_success_updatesStateFlows() = runTest {
        val sampleMovies = listOf(
            Movie(
                id = 1,
                title = "Interstellar",
                overview = "Epic science fiction film",
                poster_path = null,
                backdrop_path = null,
                release_date = "2014-11-07",
                vote_average = 8.6,
                vote_count = 10000,
                popularity = 100.0,
                adult = false,
                original_language = "en",
                original_title = "Interstellar",
                genre_ids = listOf(12, 18, 878)
            )
        )
        val response = MovieResponse(
            page = 1,
            results = sampleMovies,
            total_pages = 1,
            total_results = sampleMovies.size
        )

        Mockito.`when`(repository.getPopularMovies(1)).thenReturn(response)

        viewModel.loadMovies()
        advanceUntilIdle()

        val state = viewModel.homeState.value
        assertTrue(state is HomeState.Success)
        val successState = state as HomeState.Success
        assertEquals(sampleMovies, successState.movies)
        assertTrue(successState.isLastPage)
    }

    @Test
    fun loadMovies_failure_setsErrorFlow() = runTest {
        val errorMessage = "Network error"
        val exception = RuntimeException(errorMessage)

        Mockito.`when`(repository.getPopularMovies(1)).thenThrow(exception)

        viewModel.loadMovies()
        advanceUntilIdle()

        val state = viewModel.homeState.value
        assertTrue(state is HomeState.Error)
        val errorState = state as HomeState.Error
        assertEquals(errorMessage, errorState.message)
    }
}
