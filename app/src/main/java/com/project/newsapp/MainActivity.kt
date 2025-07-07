package com.project.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.project.newsapp.model.NavItem
import com.project.newsapp.navigation.CategoryPageScreen
import com.project.newsapp.navigation.HomePageScreen
import com.project.newsapp.navigation.NewsArticleScreen
import com.project.newsapp.view.CategoryPage
import com.project.newsapp.view.HomePage
import com.project.newsapp.view.NewsArticlePage
import com.project.newsapp.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navItemList = listOf(
                NavItem(stringResource(R.string.home_nav_label), Icons.Default.Home, HomePageScreen.route),
                NavItem(stringResource(R.string.categories_nav_label), Icons.Default.Menu, CategoryPageScreen.route)
            )

            Scaffold(
                topBar = {
                    Column {
                        MyTopAppBar()
                    }
                },
                bottomBar = {
                    SetBottomBar(navItemList, navController)
                }
            ) { innerPadding ->
                AppNavigation(navController, newsViewModel, innerPadding)
            }

        }
    }

    @Composable
    fun AppNavigation(
        navController: NavHostController,
        newsViewModel: NewsViewModel,
        innerPadding: PaddingValues
    ) {
        NavHost(
            navController = navController,
            startDestination = HomePageScreen.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(HomePageScreen.route) {
                HomePage(newsViewModel, navController)
            }
            composable<NewsArticleScreen>(
            ) {
                val args = it.toRoute<NewsArticleScreen>()
                NewsArticlePage(args.url)
            }
            composable(CategoryPageScreen.route) {
                CategoryPage(newsViewModel, navController)
            }
        }
    }

    @Composable
    fun SetBottomBar(navItemList: List<NavItem>, navController: NavHostController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        NavigationBar {
            navItemList.forEach { navItem ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                    onClick = {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                    },
                    label = { Text(text = navItem.label) }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyTopAppBar() {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(R.string.app_logo),
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.size_40dp))
                            .padding(end = dimensionResource(R.dimen.size_8dp))
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


