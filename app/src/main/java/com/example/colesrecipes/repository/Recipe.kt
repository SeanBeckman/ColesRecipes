package com.example.colesrecipes.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class RecipeResponse(
    val recipes: List<Recipe>
)

@Parcelize
data class Recipe(
    val dynamicTitle: String,
    val dynamicDescription: String,
    val dynamicThumbnail: String?,
    val dynamicThumbnailAlt: String?,
    val recipeDetails: RecipeDetails,
    val ingredients: List<Ingredient>,
): Parcelable

@Parcelize
data class RecipeDetails(
    val amountLabel: String,
    val amountNumber: Int,
    val prepLabel: String,
    val prepTime: String,
    val prepNote: String?,
    val cookingLabel: String,
    val cookingTime: String,
    val cookTimeAsMinutes: Int,
    val prepTimeAsMinutes: Int,
): Parcelable

@Parcelize
data class Ingredient(
    val ingredient: String,
): Parcelable