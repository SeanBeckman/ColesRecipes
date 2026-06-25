package com.example.colesrecipes.repository

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dynamicTitle: String,
    val dynamicDescription: String,
    val dynamicThumbnail: String?,
    val dynamicThumbnailAlt: String?,
    val amountLabel: String,
    val amountNumber: Int,
    val prepLabel: String,
    val prepTime: String,
    val prepNote: String?,
    val cookingLabel: String,
    val cookingTime: String,
    val cookTimeAsMinutes: Int,
    val prepTimeAsMinutes: Int,
    val ingredientsJson: String
)

fun RecipeEntity.toDomain(): Recipe {
    val ingredientType = object : TypeToken<List<Ingredient>>() {}.type
    val ingredients: List<Ingredient> = Gson().fromJson(ingredientsJson, ingredientType)
    
    return Recipe(
        dynamicTitle = dynamicTitle,
        dynamicDescription = dynamicDescription,
        dynamicThumbnail = dynamicThumbnail,
        dynamicThumbnailAlt = dynamicThumbnailAlt,
        recipeDetails = RecipeDetails(
            amountLabel = amountLabel,
            amountNumber = amountNumber,
            prepLabel = prepLabel,
            prepTime = prepTime,
            prepNote = prepNote,
            cookingLabel = cookingLabel,
            cookingTime = cookingTime,
            cookTimeAsMinutes = cookTimeAsMinutes,
            prepTimeAsMinutes = prepTimeAsMinutes
        ),
        ingredients = ingredients
    )
}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        dynamicTitle = dynamicTitle,
        dynamicDescription = dynamicDescription,
        dynamicThumbnail = dynamicThumbnail,
        dynamicThumbnailAlt = dynamicThumbnailAlt,
        amountLabel = recipeDetails.amountLabel,
        amountNumber = recipeDetails.amountNumber,
        prepLabel = recipeDetails.prepLabel,
        prepTime = recipeDetails.prepTime,
        prepNote = recipeDetails.prepNote,
        cookingLabel = recipeDetails.cookingLabel,
        cookingTime = recipeDetails.cookingTime,
        cookTimeAsMinutes = recipeDetails.cookTimeAsMinutes,
        prepTimeAsMinutes = recipeDetails.prepTimeAsMinutes,
        ingredientsJson = Gson().toJson(ingredients)
    )
}
