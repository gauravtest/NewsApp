package com.project.newsapp.network

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val message: String) : UiState()
    data object NoNetwork : UiState()
}