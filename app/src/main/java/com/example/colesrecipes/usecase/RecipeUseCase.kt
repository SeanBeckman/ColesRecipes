package com.example.colesrecipes.usecase

import com.example.colesrecipes.repository.Recipe
import com.example.colesrecipes.repository.RecipeRepository
import javax.inject.Inject

class RecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {

    suspend fun getRecipes(): List<Recipe> = repository.getRecipes()

    suspend fun getRecipesWithinTime(maxTotalTime: Int) = repository.getRecipesWithinTime(maxTotalTime)
}
