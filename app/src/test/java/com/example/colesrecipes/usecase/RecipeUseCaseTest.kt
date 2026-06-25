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
    fun `getRecipes returns all recipes from repository with formatted thumbnails`() = runTest {
        // Given
        val recipes = listOf(
            createRecipe("Test Recipe", 10, 10, "/image.jpg"),
            createRecipe("Null Image", 5, 5, null)
        )
        coEvery { repository.getRecipes() } returns recipes

        // When
        val result = recipeUseCase.getRecipes()

        // Then
        assertThat(result[0].dynamicThumbnail).isEqualTo("${RecipeRepository.COLES_BASE_URL}/image.jpg")
        assertThat(result[1].dynamicThumbnail).isNull()
    }

    @Test
    fun `getRecipesWithinTime returns recipes from repository`() = runTest {
        // Given
        val recipes = listOf(
            createRecipe("Fast", prep = 10, cook = 10, thumbnail = "/fast.jpg"),
            createRecipe("Medium", prep = 20, cook = 20, thumbnail = "/medium.jpg"),
            createRecipe("Large", prep = 20, cook = 30, thumbnail = "/large.jpg")
        )
        coEvery { repository.getRecipesWithinTime(40) } returns recipes

        // When
        val result = recipeUseCase.getRecipesWithinTime(40)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].dynamicThumbnail).isEqualTo("${RecipeRepository.COLES_BASE_URL}/fast.jpg")
        assertThat(result[1].dynamicThumbnail).isEqualTo("${RecipeRepository.COLES_BASE_URL}/medium.jpg")
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

    private fun createRecipe(title: String, prep: Int, cook: Int, thumbnail: String? = null): Recipe {
        return Recipe(
            dynamicTitle = title,
            dynamicDescription = "",
            dynamicThumbnail = thumbnail,
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
