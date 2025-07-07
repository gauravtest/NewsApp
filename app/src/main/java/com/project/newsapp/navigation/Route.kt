package com.project.newsapp.navigation
import kotlinx.serialization.Serializable


@Serializable
object HomePageScreen {
    val route = "home_page"
}

@Serializable
object CategoryPageScreen {
    val route = "category_page"
}

@Serializable
data class NewsArticleScreen(
    val url : String
)
