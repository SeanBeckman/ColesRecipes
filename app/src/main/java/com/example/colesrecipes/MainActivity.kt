package com.example.colesrecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.colesrecipes.features.recipes.detail.RecipeDetailScreen
import com.example.colesrecipes.features.recipes.list.RecipeListScreen
import com.example.colesrecipes.ui.theme.ColesRecipesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColesRecipesTheme {
                ColesRecipesApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun ColesRecipesApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Coles Recipes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "recipeList",
            modifier = Modifier.padding(padding)
        ) {
            composable("recipeList") {
                RecipeListScreen(
                    onNavigateToDetail = { recipeTitle ->
                        navController.navigate("recipeDetail/$recipeTitle")
                    }
                )
            }
            composable(
                route = "recipeDetail/{recipeTitle}",
                arguments = listOf(navArgument("recipeTitle") {
                    type = androidx.navigation.NavType.StringType
                })
            ) {
                RecipeDetailScreen()
            }
        }
    }
}
