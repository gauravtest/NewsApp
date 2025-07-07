package com.project.newsapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.newsapp.R
import com.project.newsapp.model.Article
import com.project.newsapp.navigation.NewsArticleScreen
import com.project.newsapp.util.Commons
import com.project.newsapp.network.UiState
import com.project.newsapp.viewmodels.NewsViewModel

@Composable
fun HomePage(newsViewModel: NewsViewModel, navController: NavHostController) {
    val uiState by newsViewModel.uiState.observeAsState()
    val newsData by newsViewModel.headlines.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.size_12dp)),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.NoNetwork -> {
                FullScreenMessage(
                    message = stringResource(R.string.no_network_connection),
                    buttonText = stringResource(R.string.retry)
                )
            }
            is UiState.Error -> {
                FullScreenMessage(
                    message = currentState.message,
                    buttonText = stringResource(R.string.retry)
                )
            }
            is UiState.Success -> {
                val articles = newsData?.articles
                if (articles.isNullOrEmpty()) {
                    FullScreenMessage(message = stringResource(R.string.no_articles_found))
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        DisplayNewsType(newsViewModel)
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = articles,
                                key = { article -> article.url ?: article.title ?: article.publishedAt ?: ""} // Add a stable key
                            ) { item ->
                                ItemRow(item, navController)
                            }
                        }
                    }
                }
            }
            null -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun DisplayNewsType(newsViewModel: NewsViewModel) {
    val name = Commons.getDisplayName(newsViewModel.newsType)
    Text(
        text = name,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            start = dimensionResource(R.dimen.size_8dp),
            end = dimensionResource(R.dimen.size_8dp),
            top = dimensionResource(R.dimen.size_8dp),
            bottom = dimensionResource(R.dimen.size_16dp)
        )
    )
}

@Composable
fun ItemRow(item: Article, navController: NavHostController) {
    Card(
        modifier = Modifier.padding(
            horizontal = dimensionResource(R.dimen.size_4dp),
            vertical = dimensionResource(R.dimen.size_8dp)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.size_4dp)),
        onClick = {
            item.url?.let { url ->
                if (url.isNotEmpty()) {
                    navController.navigate(NewsArticleScreen(url))
                }
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.size_12dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.size_5dp))
            ) {
                Text(
                    text = item.title ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.description ?: "",
                    fontSize = 14.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))
            AsyncImage(
                model = item.urlToImage,
                contentDescription = stringResource(R.string.image_content_description),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.size_100dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder)
            )
        }
    }
}

@Composable
fun FullScreenMessage(
    message: String,
    buttonText: String? = null,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (buttonText != null && onRetry != null) {
            Button(onClick = onRetry) {
                Text(text = buttonText)
            }
        }
    }
}