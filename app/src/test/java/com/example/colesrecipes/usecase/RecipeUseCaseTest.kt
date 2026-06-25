package com.example.colesrecipes.usecase

import com.example.colesrecipes.repository.Recipe
import com.example.colesrecipes.repository.RecipeDetails
import com.example.colesrecipes.repository.RecipeRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecipeUseCaseTest {

    private lateinit var recipeUseCase: RecipeUseCase
    private val repository: RecipeRepository = mockk()

    @Before
    fun setUp() {
        recipeUseCase = RecipeUseCase(repository)
    }

    @Test
    fun `getRecipes returns all recipes from repository`() = runTest {
        // Given
        val recipes = listOf(createRecipe("Test Recipe", 10, 10))
        coEvery { repository.getRecipes() } returns recipes

        // When
        val result = recipeUseCase.getRecipes()

        // Then
        assertThat(result).isEqualTo(recipes)
    }

    @Test
    fun `getRecipesWithinTime filters recipes correctly`() = runTest {
        // Given
        val recipes = listOf(
            createRecipe("Fast", prep = 10, cook = 10), // 20 mins
            createRecipe("Medium", prep = 20, cook = 20), // 40 mins
            createRecipe("Long", prep = 20, cook = 30) // 50 mins
        )
        coEvery { repository.getRecipesWithinTime(40) } returns recipes

        // When
        val result = recipeUseCase.getRecipesWithinTime(40)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.map { it.dynamicTitle }).containsExactly("Fast", "Medium")
    }

    @Test
    fun `getRecipesWithinTime returns empty list when no recipes match`() = runTest {
        // Given
        coEvery { repository.getRecipesWithinTime(30) } returns emptyList()

        // When
        val result = recipeUseCase.getRecipesWithinTime(30)

        // Then
        assertThat(result).isEmpty()
    }

    private fun createRecipe(title: String, prep: Int, cook: Int): Recipe {
        return Recipe(
            dynamicTitle = title,
            dynamicDescription = "",
            dynamicThumbnail = null,
            dynamicThumbnailAlt = null,
            recipeDetails = RecipeDetails(
                amountLabel = "",
                amountNumber = 0,
                prepLabel = "",
                prepTime = "",
                prepNote = null,
                cookingLabel = "",
                cookingTime = "",
                cookTimeAsMinutes = cook,
                prepTimeAsMinutes = prep
            ),
            ingredients = emptyList()
        )
    }
}
