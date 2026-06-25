package com.example.colesrecipes.usecase

import com.example.colesrecipes.repository.Recipe
import com.example.colesrecipes.repository.RecipeRepository
import javax.inject.Inject

class RecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {

    suspend fun getRecipes(): List<Recipe> = repository.getRecipes().map { prependBaseUrlToImageUrl(it) }

    suspend fun getRecipesWithinTime(maxTotalTime: Int) =
        repository.getRecipesWithinTime(maxTotalTime).map { prependBaseUrlToImageUrl(it) }

    private fun prependBaseUrlToImageUrl(recipe: Recipe): Recipe {
        val thumbnail = recipe.dynamicThumbnail?.let {
            "${RecipeRepository.COLES_BASE_URL}$it"
        }
        return recipe.copy(dynamicThumbnail = thumbnail)
    }
}
