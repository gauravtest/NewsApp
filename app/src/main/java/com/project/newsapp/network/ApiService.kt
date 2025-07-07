package com.project.newsapp.network

import com.project.newsapp.model.News
import com.project.newsapp.util.Commons.COUNTRY
import com.project.newsapp.util.Commons.ONE
import com.project.newsapp.util.Commons.PAGE
import com.project.newsapp.util.Commons.US
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query(COUNTRY) countryCode:String = US,
        @Query(PAGE) pageNumber: Int = ONE
    ): Response<News>

    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query:String,
        @Query(PAGE) pageNumber:Int = ONE
    ) : Response<News>

}