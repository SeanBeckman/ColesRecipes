package com.example.colesrecipes

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.example.colesrecipes.features.recipes.list.RecipeItemPreview
import com.example.colesrecipes.features.recipes.detail.RecipeDetailContentPreview
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class SnapshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule(
        options = RoborazziRule.Options(
            outputDirectoryPath = "src/test/screenshots"
        )
    )

    @Test
    fun recipeListItemSnapshot() {
        composeTestRule.setContent {
            RecipeItemPreview()
        }
        composeTestRule.onRoot().captureRoboImage("src/test/screenshots/recipe_item.png")
    }

    @Test
    fun recipeDetailSnapshot() {
        composeTestRule.setContent {
            RecipeDetailContentPreview()
        }
        composeTestRule.onRoot().captureRoboImage("src/test/screenshots/recipe_detail.png")
    }
}
