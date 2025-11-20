package com.example.androidmvvmcomposetemplate.di

import com.example.androidmvvmcomposetemplate.feature.tasks.data.remote.TaskApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton




/*
                    OkHttpClient

                    Handles HTTP calls. We add a logging interceptor so API requests/responses are visible in Logcat during dev.

                    Retrofit

                    Turns the TaskApi interface into a concrete implementation at runtime.

                    MoshiConverterFactory handles JSON ↔ Kotlin data class conversions.

                    TaskApi

                    Simple interface using Retrofit annotations:

                    @GET("tasks") tells Retrofit to call GET BASE_URL/tasks.

                    Everything is annotated with Hilt so we can inject it:

                    @Singleton – one instance for the whole app.

                    Later, TaskRepositoryImpl will get a TaskApi in its constructor.
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/" // TODO: replace with real URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            // BASIC or BODY depending on how noisy you want it
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }


    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())   // <-- this is key for Kotlin
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskApi(
        retrofit: Retrofit
    ): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }
}