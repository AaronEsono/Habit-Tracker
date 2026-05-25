package aeb.proyecto.habittracker.components.topbar

import aeb.proyecto.ui.controllerProvider.LocalNavController
import aeb.proyecto.ui.topbar.TopBarViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * A highly dynamic, adaptive [CenterAlignedTopAppBar] that orchestrates the global top bar area.
 *
 * This Composable observes the active [NavBackStackEntry] state reactively to instantiate or retrieve
 * a scoped instance of [TopBarViewModel]. By tying the [viewModelStoreOwner] to the specific navigation
 * backstack entry, top bar configurations (titles, contextual actions, and navigation behaviors) are isolated
 * per screen lifecycle.
 *
 * To deliver a premium user experience, any structural update to the bar contents triggers elegant,
 * directional horizontal transitions managed entirely through declarative [AnimatedContent] slots.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHabit() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    navBackStackEntry?.let { entry ->
        val viewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { TopBarViewModel() },
        )

        val title = viewModel.title
        val actions = viewModel.actions
        val navigationIcon = viewModel.navigationIcon

        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            title = { AnimatedContent(targetState = title) {titleAnim ->titleAnim()} },
            actions = { AnimatedContent(targetState = actions,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(durationMillis = 300)
                    ) togetherWith  slideOutHorizontally(
                        targetOffsetX = { 1000 },
                        animationSpec = tween(durationMillis = 300)
                    ) using SizeTransform(clip = false)
                }
            ) {actionsAnim ->actionsAnim()} },
            navigationIcon = {
                AnimatedContent(
                    targetState = navigationIcon,
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 300)
                        ) togetherWith  slideOutHorizontally(
                            targetOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 300)
                        ) using SizeTransform(clip = false)
                    }
                ) {navigationIconAnim -> navigationIconAnim() }
            },
            windowInsets = WindowInsets(
                left = 0,
                top = TopAppBarDefaults.windowInsets.getTop(LocalDensity.current),
                right = 0,
                bottom = TopAppBarDefaults.windowInsets.getBottom(LocalDensity.current)
            )
        )

    }
}