package com.example.colesrecipes.features.recipes.detail

import androidx.lifecycle.SavedStateHandle
import com.example.colesrecipes.repository.Recipe
import com.example.colesrecipes.repository.RecipeDetails
import com.example.colesrecipes.usecase.RecipeUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailViewModelTest {

    private lateinit var viewModel: RecipeDetailViewModel
    private val recipeUseCase: RecipeUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init updates uiState to Success when recipe is found`() = runTest {
        val recipeTitle = "Test Recipe"
        val recipe = createRecipe(recipeTitle)
        val savedStateHandle = SavedStateHandle(mapOf("recipeTitle" to recipeTitle))
        
        coEvery { recipeUseCase.getRecipe(recipeTitle) } returns recipe

        viewModel = RecipeDetailViewModel(savedStateHandle, recipeUseCase)

        assertThat(viewModel.uiState.value).isEqualTo(RecipeDetailUiState.Success(recipe))
    }

    @Test
    fun `init updates uiState to Error when recipe is not found`() = runTest {
        val recipeTitle = "Unknown Recipe"
        val savedStateHandle = SavedStateHandle(mapOf("recipeTitle" to recipeTitle))
        
        coEvery { recipeUseCase.getRecipe(recipeTitle) } returns null

        viewModel = RecipeDetailViewModel(savedStateHandle, recipeUseCase)

        assertThat(viewModel.uiState.value).isEqualTo(RecipeDetailUiState.Error("Recipe not found"))
    }

    private fun createRecipe(title: String): Recipe {
        return Recipe(
            dynamicTitle = title,
            dynamicDescription = "Description",
            dynamicThumbnail = null,
            dynamicThumbnailAlt = null,
            recipeDetails = RecipeDetails(
                amountLabel = "Serves",
                amountNumber = 4,
                prepLabel = "Prep",
                prepTime = "10m",
                prepNote = null,
                cookingLabel = "Cooking",
                cookingTime = "20m",
                cookTimeAsMinutes = 20,
                prepTimeAsMinutes = 10
            ),
            ingredients = emptyList()
        )
    }
}
