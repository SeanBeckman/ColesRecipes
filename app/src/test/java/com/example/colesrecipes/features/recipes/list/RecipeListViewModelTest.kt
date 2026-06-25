package com.example.colesrecipes.features.recipes.list

import com.example.colesrecipes.repository.Recipe
import com.example.colesrecipes.repository.RecipeDetails
import com.example.colesrecipes.usecase.RecipeUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeListViewModelTest {

    private lateinit var viewModel: RecipeListViewModel
    private val recipeUseCase: RecipeUseCase = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = RecipeListViewModel(recipeUseCase)

    @Test
    fun `initial state is Loading`() = runTest {
        coEvery { recipeUseCase.getRecipes() } coAnswers {
            delay(1000.milliseconds)
            emptyList()
        }
        
        viewModel = createViewModel()
        
        assertThat(viewModel.uiState.value).isEqualTo(RecipeListUiState.Loading)
    }

    @Test
    fun `loadRecipes updates uiState to Success when successful`() = runTest {
        val recipes = listOf(createRecipe("Recipe 1"))
        coEvery { recipeUseCase.getRecipes() } returns recipes

        viewModel = createViewModel()

        assertThat(viewModel.uiState.value).isEqualTo(RecipeListUiState.Success(recipes))
    }

    @Test
    fun `loadRecipes updates uiState to Error when it fails`() = runTest {
        val errorMessage = "Network Error"
        coEvery { recipeUseCase.getRecipes() } throws Exception(errorMessage)

        viewModel = createViewModel()

        assertThat(viewModel.uiState.value).isEqualTo(RecipeListUiState.Error(errorMessage))
    }

    @Test
    fun `filterRecipesByTime updates uiState to Success with filtered recipes`() = runTest {
        val filteredRecipes = listOf(createRecipe("Quick Recipe"))
        coEvery { recipeUseCase.getRecipesWithinTime(any()) } returns filteredRecipes

        viewModel = createViewModel()
        viewModel.filterRecipesByTime(30)

        assertThat(viewModel.uiState.value).isEqualTo(RecipeListUiState.Success(filteredRecipes))
    }

    @Test
    fun `filterRecipesByTime updates uiState to Error when it fails`() = runTest {
        val errorMessage = "Filter Error"
        coEvery { recipeUseCase.getRecipesWithinTime(any()) } throws Exception(errorMessage)

        viewModel = createViewModel()
        viewModel.filterRecipesByTime(30)

        assertThat(viewModel.uiState.value).isEqualTo(RecipeListUiState.Error(errorMessage))
    }

    @Test
    fun `onRecipeClicked emits NavigateToDetail event`() = runTest {
        val recipe = createRecipe("Test Recipe")
        viewModel = createViewModel()
        
        val events = mutableListOf<RecipeListNavigationEvent>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.navigationEvents.toList(events)
        }

        viewModel.onRecipeClicked(recipe)

        assertThat(events).hasSize(1)
        val event = events[0]
        assertThat(event).isInstanceOf(RecipeListNavigationEvent.NavigateToDetail::class.java)
        assertThat((event as RecipeListNavigationEvent.NavigateToDetail).recipeTitle).isEqualTo("Test Recipe")
        
        job.cancel()
    }

    private fun createRecipe(title: String): Recipe {
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
                cookTimeAsMinutes = 0,
                prepTimeAsMinutes = 0
            ),
            ingredients = emptyList()
        )
    }
}
