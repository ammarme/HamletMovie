package com.android.movieapp.api

import com.android.movieapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MovieApiConfig {

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(MovieApiConfig::addAuthParameters)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private fun addAuthParameters(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlWithParams = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlWithParams)
            .addHeader("Authorization", "Bearer ${BuildConfig.BEARER_TOKEN}")
            .build()

        return chain.proceed(newRequest)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideApiService(): MovieApiService {
        return createRetrofit().create(MovieApiService::class.java)
    }
}
