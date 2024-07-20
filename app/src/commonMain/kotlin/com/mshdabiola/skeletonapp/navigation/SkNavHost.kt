/*
 *abiola 2022
 */

package com.mshdabiola.skeletonapp.navigation

import androidx.navigation.toRoute
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.mshdabiola.detail.navigation.detailScreen
import com.mshdabiola.detail.navigation.navigateToDetail
import com.mshdabiola.main.navigation.mainScreen
import com.mshdabiola.model.naviagation.Main
import com.mshdabiola.setting.navigation.settingScreen
import com.mshdabiola.skeletonapp.ui.SkAppState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SkNavHost(
    appState: SkAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean = { _, _ -> false },
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    SharedTransitionLayout(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = Main,
        ) {
            mainScreen(
                modifier = Modifier,
                sharedTransitionScope = this@SharedTransitionLayout,
                onShowSnack = onShowSnackbar,
                navigateToDetail = navController::navigateToDetail,
            )
            detailScreen(
                modifier = Modifier,
                sharedTransitionScope = this@SharedTransitionLayout,
                onShowSnack = onShowSnackbar,
                onBack = navController::popBackStack,
            )
            settingScreen(
                modifier = Modifier,
                sharedTransitionScope = this@SharedTransitionLayout,
                onShowSnack = onShowSnackbar,
            )
        }
    }

}
