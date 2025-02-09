package com.ahrokholska.gifs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ahrokholska.gifs.presentation.screens.home.GifFullScreen
import com.ahrokholska.gifs.presentation.screens.home.HomeScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home) {
        composable<Screen.Home> { backStackEntry ->
            HomeScreen(
                viewModel = backStackEntry.sharedViewModel(navController),
                onImageClick = {
                    navController.navigate(Screen.GifFull(it))
                })
        }
        composable<Screen.GifFull> { backStackEntry ->
            GifFullScreen(
                initialPosition = backStackEntry.toRoute<Screen.GifFull>().gifIndex,
                viewModel = backStackEntry.sharedViewModel(navController),
            )
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