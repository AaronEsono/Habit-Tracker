package aeb.proyecto.habittracker.components

import aeb.proyecto.ui.controllerProvider.LocalNavController
import aeb.proyecto.ui.topbar.TopBarViewModel
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState

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