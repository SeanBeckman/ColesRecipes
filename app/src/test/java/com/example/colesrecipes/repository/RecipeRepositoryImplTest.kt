package com.example.colesrecipes.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RecipeRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: RecipeRepositoryImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        repository = RecipeRepositoryImpl(database.recipeDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `getRecipes returns a list of recipes from hardcoded JSON`() = runTest {
        // When
        val result = repository.getRecipes()

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result[0].dynamicTitle).isEqualTo("Curtis Stone's tomato and bread salad with BBQ eggplant and capsicum")
        assertThat(result).hasSize(8)
    }

    @Test
    fun `first recipe has correct number of ingredients`() = runTest {
        // When
        val result = repository.getRecipes()

        // Then
        val firstRecipe = result[0]
        assertThat(firstRecipe.ingredients).hasSize(14)
        assertThat(firstRecipe.ingredients[0].ingredient).isEqualTo("1 cup (250ml) extra virgin olive oil, divided")
    }
}
