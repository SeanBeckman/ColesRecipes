package com.example.colesrecipes.features.recipes.detail

import androidx.lifecycle.SavedStateHandle
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
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeUseCase: RecipeUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val recipeTitle: String = checkNotNull(savedStateHandle["recipeTitle"])
            val recipe = recipeUseCase.getRecipe(recipeTitle)
            if (recipe != null) {
                _uiState.value = RecipeDetailUiState.Success(recipe)
            } else {
                _uiState.value = RecipeDetailUiState.Error("Recipe not found")
            }
        }
    }
    private val _uiState = MutableStateFlow<RecipeDetailUiState>(RecipeDetailUiState.Loading)
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()
}


sealed class RecipeDetailUiState {
    object Loading : RecipeDetailUiState()
    data class Success(val recipes: Recipe) : RecipeDetailUiState()
    data class Error(val message: String) : RecipeDetailUiState()
}
