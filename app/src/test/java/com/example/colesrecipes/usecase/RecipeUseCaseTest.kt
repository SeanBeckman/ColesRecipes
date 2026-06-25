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
    fun `getRecipe returns recipe from repository with formatted thumbnail`() = runTest {
        // Given
        val recipe = Recipe(
            dynamicTitle = "Test Recipe",
            dynamicDescription = "",
            dynamicThumbnail = "/image.jpg",
            dynamicThumbnailAlt = null,
            recipeDetails = RecipeDetails(
                amountLabel = "",
                amountNumber = 0,
                prepLabel = "",
                prepTime = "",
                prepNote = null,
                cookingLabel = "",
                cookingTime = "",
                cookTimeAsMinutes = 10,
                prepTimeAsMinutes = 10
            ),
            ingredients = emptyList()
        )
        coEvery { repository.getRecipe(any()) } returns recipe

        // When
        val result = recipeUseCase.getRecipe("Test Recipe")

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.dynamicThumbnail).isEqualTo("${RecipeRepository.COLES_BASE_URL}/image.jpg")
    }

    @Test
    fun `getRecipe returns null when not found`() = runTest {
        // Given
        coEvery { repository.getRecipe(any()) } returns null

        // When
        val result = recipeUseCase.getRecipe("Test Recipe")

        // Then
        assertThat(result).isNull()
    }
}
