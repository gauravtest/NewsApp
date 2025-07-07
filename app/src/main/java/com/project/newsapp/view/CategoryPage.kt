package com.project.newsapp.view

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.project.newsapp.navigation.HomePageScreen
import com.project.newsapp.R
import com.project.newsapp.model.CategoryItem
import com.project.newsapp.util.Commons.ONE
import com.project.newsapp.util.Commons.categoriesWithIcons
import com.project.newsapp.viewmodels.NewsViewModel
import kotlinx.coroutines.launch

@Composable
fun CategoryPage(newsViewModel: NewsViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CategoriesBar(newsViewModel, navController)
    }
}

@Composable
fun CategoriesBar(
    newsViewModel: NewsViewModel,
    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CategoryDisplayGrid(
            categories = categoriesWithIcons,
            onCategoryClick = { categoryName ->
                coroutineScope.launch {
                    newsViewModel.fetchCategoryNews(categoryName)
                    navController.navigate(HomePageScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}

@Composable
fun CategoryDisplayGrid(
    categories: List<CategoryItem>,
    onCategoryClick: (String) -> Unit,
    itemsPerRow: Int = 2
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.size_8dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        categories.chunked(itemsPerRow).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.size_4dp)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.size_8dp), Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Top
            ) {
                rowItems.forEach { categoryItem ->
                    Box(modifier = Modifier.weight(1f)) {
                        CategoryCard(
                            categoryItem = categoryItem,
                            onClick = { onCategoryClick(categoryItem.name) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                if (rowItems.size < itemsPerRow) {
                    for (i in 0 until (itemsPerRow - rowItems.size)) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    categoryItem: CategoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .height(dimensionResource(R.dimen.size_120dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.size_2dp)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.size_8dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = categoryItem.iconResId),
                contentDescription = "${categoryItem.name} category icon",
                modifier = Modifier
                    .size(dimensionResource(R.dimen.size_40dp))
                    .padding(bottom = dimensionResource(R.dimen.size_6dp)),
                contentScale = ContentScale.Fit
            )
            Text(
                text = categoryItem.name,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                maxLines = ONE
            )
        }
    }
}





