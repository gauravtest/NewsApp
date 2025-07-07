package com.project.newsapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.project.newsapp.model.News
import com.project.newsapp.repositiry.NewsRepository
import com.project.newsapp.util.Commons.ONE
import com.project.newsapp.util.Commons.US
import com.project.newsapp.util.NetworkUtils.isNetworkAvailable
import com.project.newsapp.network.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {

    private var currentCategory = "General"
    private val _uiState = MutableLiveData<UiState>()

    val headlines: LiveData<News>
        get() = repository.headlines

    val uiState: LiveData<UiState>
        get() = _uiState

    val newsType: String
        get() = this.currentCategory


    init {
        fetchHeadlines()
    }

    private fun fetchHeadlines() {
        if (!isNetworkAvailable(application)) {
            _uiState.postValue(UiState.NoNetwork)
            return
        }
        _uiState.postValue(UiState.Loading)
        val handler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            if (throwable is IOException) {
                _uiState.postValue(UiState.Error("Network error: ${throwable.message}"))
            } else {
                _uiState.postValue(UiState.Error("An unexpected error occurred: ${throwable.message}"))
            }
        }
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.getHeadLines(US, ONE)
            _uiState.postValue(UiState.Success)
        }
    }

    suspend fun fetchCategoryNews(category: String) {
        if (!isNetworkAvailable(application)) {
            _uiState.postValue(UiState.NoNetwork)
            return
        }
        this.currentCategory = category
        try {
            repository.searchNews(category, ONE)
            _uiState.postValue(UiState.Success)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is IOException) {
                _uiState.postValue(UiState.Error("Network error: ${e.message}"))
            } else {
                _uiState.postValue(UiState.Error("Failed to fetch $category: ${e.message}"))
            }
        }
    }
}
