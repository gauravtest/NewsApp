package com.project.newsapp.util

import com.project.newsapp.R
import com.project.newsapp.model.CategoryItem
import java.util.Locale

object Commons {

    private const val NEWS = " News"
    private const val ZERO = 0
    const val API_KEY = "3f1501e19f5c44619bd60e4f5ea36609"
    const val BASE_URL = "https://newsapi.org/"
    const val COUNTRY = "country"
    const val PAGE = "page"
    const val APIKEY = "apikey"
    const val ONE = 1
    const val US = "us"

    fun getDisplayName(str: String?): String {
        return (str?.substring(ZERO, ONE)?.uppercase(Locale.ROOT) + str?.substring(ONE)
            ?.lowercase(Locale.ROOT)).plus(NEWS)
    }

    val categoriesWithIcons = listOf(
        CategoryItem("GENERAL", R.drawable.news),
        CategoryItem("BUSINESS", R.drawable.business),
        CategoryItem("ENTERTAINMENT", R.drawable.entertainment),
        CategoryItem("HEALTH", R.drawable.health),
        CategoryItem("SCIENCE", R.drawable.science),
        CategoryItem("SPORTS", R.drawable.sports),
        CategoryItem("TECHNOLOGY", R.drawable.technology)
    )

}