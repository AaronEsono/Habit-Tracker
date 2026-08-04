package com.example.attributions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.attributions.AttributionsScreen
import kotlinx.serialization.Serializable

/**
 * Navigation destination representing the Attributions screen.
 *
 * This destination is used by the type-safe Navigation API to identify the
 * screen that displays third-party attributions, licenses, and acknowledgements.
 */
@Serializable
data object Attributions

/**
 * Navigates to the Attributions screen.
 *
 * This extension function performs type-safe navigation to the
 * [Attributions] destination.
 */
fun NavController.navigateToAttributions(){
    navigate(Attributions)
}

/**
 * Registers the Attributions screen in the navigation graph.
 *
 * This function adds the [Attributions] destination and associates it with
 * the [AttributionsScreen] composable, allowing it to be reached through
 * type-safe navigation.
 */
fun NavGraphBuilder.attributionsScreen() {
    composable<Attributions> {
        AttributionsScreen()
    }
}