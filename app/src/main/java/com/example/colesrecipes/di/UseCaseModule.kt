package com.example.colesrecipes.di

import com.example.colesrecipes.repository.RecipeRepository
import com.example.colesrecipes.usecase.RecipeUseCase
import com.example.colesrecipes.usecase.RecipesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideRecipeSUseCase(repository: RecipeRepository): RecipesUseCase {
        return RecipesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRecipeUseCase(repository: RecipeRepository): RecipeUseCase {
        return RecipeUseCase(repository)
    }
}
