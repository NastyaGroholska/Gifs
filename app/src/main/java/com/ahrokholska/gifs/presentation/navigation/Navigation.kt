package com.ahrokholska.gifs.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ahrokholska.gifs.presentation.screens.home.GifFullScreen
import com.ahrokholska.gifs.presentation.screens.home.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation() {
    SharedTransitionLayout {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.HomeGraph) {
            navigation<Screen.HomeGraph>(Screen.HomeGraph.Home) {
                composable<Screen.HomeGraph.Home> { backStackEntry ->
                    HomeScreen(
                        viewModel = backStackEntry.sharedViewModel(navController),
                        onImageClick = {
                            navController.navigate(Screen.HomeGraph.GifFull(it))
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }

                composable<Screen.HomeGraph.GifFull> { backStackEntry ->
                    GifFullScreen(
                        initialPosition = backStackEntry.toRoute<Screen.HomeGraph.GifFull>().gifIndex,
                        viewModel = backStackEntry.sharedViewModel(navController),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}