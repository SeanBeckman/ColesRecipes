package com.example.colesrecipes.usecase

import com.example.colesrecipes.repository.Recipe
import com.example.colesrecipes.repository.RecipeRepository
import javax.inject.Inject

class RecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {

    suspend fun getRecipe(title: String): Recipe? {
        val recipe = repository.getRecipe(title)
        return recipe?.let { prependBaseUrlToImageUrl(it) }
    }

    suspend fun getRecipes(): List<Recipe> {
        return repository.getRecipes().map { prependBaseUrlToImageUrl(it) }
    }

    suspend fun getRecipesWithinTime(maxTotalTime: Int): List<Recipe> {
        return repository.getRecipesWithinTime(maxTotalTime).map { prependBaseUrlToImageUrl(it) }
    }

    private fun prependBaseUrlToImageUrl(recipe: Recipe): Recipe {
        val thumbnail = recipe.dynamicThumbnail?.let {
            "${RecipeRepository.COLES_BASE_URL}$it"
        }
        return recipe.copy(dynamicThumbnail = thumbnail)
    }
}
