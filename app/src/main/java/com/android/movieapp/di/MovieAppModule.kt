package com.android.movieapp.di

import android.content.Context
import com.android.movieapp.api.MovieApiConfig
import com.android.movieapp.api.MovieApiService
import com.android.movieapp.repo.MovieRepository
import com.android.movieapp.repo.MovieRepositoryImpl
import com.android.movieapp.utils.ConnectivityMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieAppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: MovieApiService): MovieRepository {
        return MovieRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {
        return MovieApiConfig.provideApiService()
    }

    @Provides
    @Singleton
    fun provideConnectivityMonitor(@ApplicationContext context: Context): ConnectivityMonitor {
        return ConnectivityMonitor(context)
    }
}
