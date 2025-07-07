package com.project.newsapp.repositiry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.newsapp.network.ApiService
import com.project.newsapp.model.News
import javax.inject.Inject

class NewsRepository @Inject constructor (private val apiService: ApiService) {

    private val headLinesLiveData = MutableLiveData<News>()

    val headlines: LiveData<News>
        get() = headLinesLiveData

    suspend fun getHeadLines(countryCode:String, pageNumber:Int) {
        val result = apiService.getHeadlines(countryCode, pageNumber)
        if (result.body() != null) {
            headLinesLiveData.postValue(result.body())
        }
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int) {
        val result = apiService.getEverything(searchQuery, pageNumber)
        if (result.body() != null) {
            headLinesLiveData.postValue(result.body())
        }
    }
}