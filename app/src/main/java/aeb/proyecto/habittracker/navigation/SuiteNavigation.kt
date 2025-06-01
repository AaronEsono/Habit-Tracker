package aeb.proyecto.habittracker.navigation

import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun suiteNavigation():NavigationSuiteType{
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val orientation = getOrientation()

    val layoutType = remember(windowSizeClass, orientation) {
        when {
            // Teléfono en vertical: barra inferior
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT && orientation == Orientation.Portrait ->
                NavigationSuiteType.NavigationBar

            // Teléfono en horizontal: rail lateral
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT && orientation == Orientation.Landscape ->
                NavigationSuiteType.NavigationRail

            // Tablet en vertical: rail lateral (más cómodo)
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM && orientation == Orientation.Portrait ->
                NavigationSuiteType.NavigationRail

            // Tablet en horizontal o pantalla grande: rail o drawer
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM && orientation == Orientation.Landscape ->
                NavigationSuiteType.NavigationRail

            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ->
                NavigationSuiteType.NavigationRail

            else -> NavigationSuiteType.NavigationBar // Fallback por defecto
        }
    }

    return layoutType
}