package com.project.newsapp.di

import com.project.newsapp.network.ApiKeyInterceptor
import com.project.newsapp.util.Commons.BASE_URL
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.project.newsapp.network.ApiService
import com.project.newsapp.repositiry.NewsRepository
import dagger.Module
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .build()

    @Singleton
    @Provides
    fun providesAPI(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsRepository(apiService: ApiService): NewsRepository {
        return NewsRepository(apiService)
    }

}