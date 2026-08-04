package com.example.attributions

import aeb.proyecto.attributions.R
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.attributions.screens.HorizontalAttributionsScreen
import com.example.attributions.screens.VerticalAttributionsScreen

/**
 * Displays the Attributions screen.
 *
 * This composable is the entry point for the application's attributions UI.
 * It configures the app bar by providing a navigation icon and a localized
 * title, then displays the appropriate screen layout based on the current
 * device orientation.
 *
 * - In portrait orientation, [VerticalAttributionsScreen] is displayed.
 * - In landscape orientation, [HorizontalAttributionsScreen] is displayed.
 *
 * The orientation is determined at runtime, allowing the UI to adapt
 * automatically to configuration changes.
 */
@Composable
fun AttributionsScreen(){

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.attributions_title_topbar),fontSize = 20.sp)
    }

    val orientation = getOrientation()

    when(orientation){
        Orientation.Portrait -> VerticalAttributionsScreen()
        Orientation.Landscape -> HorizontalAttributionsScreen()
    }

}