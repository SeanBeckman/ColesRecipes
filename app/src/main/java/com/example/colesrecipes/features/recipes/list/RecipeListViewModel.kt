package com.example.colesrecipes.features.recipes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colesrecipes.repository.Recipe
import com.example.colesrecipes.usecase.RecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val recipeUseCase: RecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState.Loading)
    val uiState: StateFlow<RecipeListUiState> = _uiState.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = RecipeListUiState.Loading
            try {
                val recipes = recipeUseCase.getRecipes()
                _uiState.value = RecipeListUiState.Success(recipes)
            } catch (e: Exception) {
                _uiState.value = RecipeListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun filterRecipesByTime(maxTime: Int) {
        viewModelScope.launch {
            _uiState.value = RecipeListUiState.Loading
            try {
                val filteredRecipes = recipeUseCase.getRecipesWithinTime(maxTime)
                _uiState.value = RecipeListUiState.Success(filteredRecipes)
            } catch (e: Exception) {
                _uiState.value = RecipeListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class RecipeListUiState {
    object Loading : RecipeListUiState()
    data class Success(val recipes: List<Recipe>) : RecipeListUiState()
    data class Error(val message: String) : RecipeListUiState()
}
