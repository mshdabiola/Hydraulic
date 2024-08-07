package com.mshdabiola.skeletonapp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.mshdabiola.skeletonapp.ui.SkAppState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SkAppState {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var state: SkAppState

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun currentDestination() = runTest {
        var currentDestination: String? = null

        composeTestRule.setContent {
            val density = LocalDensity.current
            val navController = rememberNavController().apply {
                graph = createGraph(startDestination = "a") {
                    composable("a") { }
                    composable("b") { }
                    composable("c") { }
                }
            }
            state = remember(navController) {
                SkAppState(
                    navController = navController,
                    coroutineScope = backgroundScope,
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        size = Size(456f, 3f),
                        density = density,
                    ),

                )
            }

            // Update currentDestination whenever it changes
            currentDestination = state.currentRoute

            // Navigate to destination b once
            LaunchedEffect(Unit) {
                navController.navigate("b")
            }
        }

        assertEquals("b", currentDestination)
    }
}
