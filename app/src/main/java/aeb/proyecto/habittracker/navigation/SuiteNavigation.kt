package aeb.proyecto.habittracker.navigation

import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * A responsive Composable that determines the optimal global navigation layout paradigm
 * based on the device's current window size metrics and orientation posture.
 *
 * This implementation follows modern Material 3 Adaptive Design guidelines by scanning
 * [WindowWidthSizeClass] boundaries to seamlessly transition between mobile-first formats
 * and large-screen interfaces:
 * * - **[NavigationSuiteType.NavigationBar] (Bottom Bar):** Formatted for compact devices (smartphones)
 * held in portrait orientation, ensuring natural thumb reachability.
 * - **[NavigationSuiteType.NavigationRail] (Side Rail):** Selected for landscape smartphones, medium
 * tablets, and fully expanded desktops/foldables to optimize horizontal real estate and avoid vertical crowding.
 *
 * @return The calculated [NavigationSuiteType] layout flag to be consumed by the root Scaffold infrastructure.
 */
@Composable
fun suiteNavigation():NavigationSuiteType{
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val orientation = getOrientation()

    val layoutType = remember(windowSizeClass, orientation) {
        when {
            // Smartphone in Portrait: Bottom navigation bar
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT && orientation == Orientation.Portrait ->
                NavigationSuiteType.NavigationBar

            // Smartphone in Landscape: Side navigation rail
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT && orientation == Orientation.Landscape ->
                NavigationSuiteType.NavigationRail

            // Tablet in Portrait: Side navigation rail (more ergonomic)
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM && orientation == Orientation.Portrait ->
                NavigationSuiteType.NavigationRail

            // Tablet in Landscape or Large Screen: Rail configuration
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM && orientation == Orientation.Landscape ->
                NavigationSuiteType.NavigationRail

            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ->
                NavigationSuiteType.NavigationRail

            else -> NavigationSuiteType.NavigationBar // Fallback default pattern
        }
    }

    return layoutType
}