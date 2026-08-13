package com.example.attributions.screens

import aeb.proyecto.attributions.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.TitleMediumText
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.attributions.components.AuthorButton
import com.example.attributions.components.FlaticonButton
import com.example.attributions.constants.CATALIN_FERTU_AUTHOR
import com.example.attributions.constants.CHANUT_IS_INDUSTRIES_AUTHOR
import com.example.attributions.constants.EMBLEMICONS_AUTHOR
import com.example.attributions.constants.GARUDA_TECHNOLOGY_AUTHOR
import com.example.attributions.constants.ICONSAX_AUTHOR
import com.example.attributions.constants.IC_ACHIEVEMENT_ALARM_MANAGER_NAME
import com.example.attributions.constants.IC_ACHIEVEMENT_ALARM_MANAGER_URI
import com.example.attributions.constants.IC_ADD_HABIT_NAME
import com.example.attributions.constants.IC_ADD_HABIT_URI
import com.example.attributions.constants.IC_ARROW_BACK_UI_NAME
import com.example.attributions.constants.IC_ARROW_BACK_UI_URI
import com.example.attributions.constants.IC_ATTRIBUTIONS_SETTINGS_NAME
import com.example.attributions.constants.IC_ATTRIBUTIONS_SETTINGS_URI
import com.example.attributions.constants.IC_CALENDAR_APP_NAME
import com.example.attributions.constants.IC_CALENDAR_APP_URI
import com.example.attributions.constants.IC_CALENDAR_SETTINGS_NAME
import com.example.attributions.constants.IC_CALENDAR_SETTINGS_URI
import com.example.attributions.constants.IC_EMAIL_SETTINGS_NAME
import com.example.attributions.constants.IC_EMAIL_SETTINGS_URI
import com.example.attributions.constants.IC_FILE_SAVE_NAME
import com.example.attributions.constants.IC_FILE_SAVE_URI
import com.example.attributions.constants.IC_FIND_DATE_HABIT_NAME
import com.example.attributions.constants.IC_FIND_DATE_HABIT_URI
import com.example.attributions.constants.IC_GITHUB_SETTINGS_NAME
import com.example.attributions.constants.IC_GITHUB_SETTINGS_URI
import com.example.attributions.constants.IC_INTERVAL_TIMER_NAME
import com.example.attributions.constants.IC_INTERVAL_TIMER_URI
import com.example.attributions.constants.IC_LANGUAGE_SETTINGS_NAME
import com.example.attributions.constants.IC_LANGUAGE_SETTINGS_URI
import com.example.attributions.constants.IC_LINK_SETTINGS_NAME
import com.example.attributions.constants.IC_LINK_SETTINGS_URI
import com.example.attributions.constants.IC_NO_STATISTICS_STATISTICS_NAME
import com.example.attributions.constants.IC_NO_STATISTICS_STATISTICS_URI
import com.example.attributions.constants.IC_OVERLAY_SETTINGS_NAME
import com.example.attributions.constants.IC_OVERLAY_SETTINGS_URI
import com.example.attributions.constants.IC_PALETTE_SETTINGS_NAME
import com.example.attributions.constants.IC_PALETTE_SETTINGS_URI
import com.example.attributions.constants.IC_PRIVACY_SETTINGS_NAME
import com.example.attributions.constants.IC_PRIVACY_SETTINGS_URI
import com.example.attributions.constants.IC_SAVE_SETTINGS_NAME
import com.example.attributions.constants.IC_SAVE_SETTINGS_URI
import com.example.attributions.constants.IC_SETTINGS_APP_NAME
import com.example.attributions.constants.IC_SETTINGS_APP_URI
import com.example.attributions.constants.IC_STATISTICS_APP_NAME
import com.example.attributions.constants.IC_STATISTICS_APP_URI
import com.example.attributions.constants.IC_TERMS_SETTINGS_NAME
import com.example.attributions.constants.IC_TERMS_SETTINGS_URI
import com.example.attributions.constants.IC_TIMER_APP_NAME
import com.example.attributions.constants.IC_TIMER_APP_URI
import com.example.attributions.constants.IM_CALENDAR_SETTINGS_NAME
import com.example.attributions.constants.IM_CALENDAR_SETTINGS_URI
import com.example.attributions.constants.IM_FAVORITE_TIMER_NAME
import com.example.attributions.constants.IM_FAVORITE_TIMER_URI
import com.example.attributions.constants.IM_GOOGLE_LOGIN_NAME
import com.example.attributions.constants.IM_GOOGLE_LOGIN_URI
import com.example.attributions.constants.IM_HABIT_ADD_HABIT_NAME
import com.example.attributions.constants.IM_HABIT_ADD_HABIT_URI
import com.example.attributions.constants.IM_LANGUAGE_SETTINGS_NAME
import com.example.attributions.constants.IM_LANGUAGE_SETTINGS_URI
import com.example.attributions.constants.IM_NO_FAVORITE_TIMER_NAME
import com.example.attributions.constants.IM_NO_FAVORITE_TIMER_URI
import com.example.attributions.constants.IM_NO_HABIT_HABIT_NAME
import com.example.attributions.constants.IM_NO_HABIT_HABIT_URI
import com.example.attributions.constants.IM_SPAIN_SETTINGS_NAME
import com.example.attributions.constants.IM_SPAIN_SETTINGS_URI
import com.example.attributions.constants.IM_STOPWATCH_TIMER_NAME
import com.example.attributions.constants.IM_STOPWATCH_TIMER_URI
import com.example.attributions.constants.IM_THEME_SETTINGS_NAME
import com.example.attributions.constants.IM_THEME_SETTINGS_URI
import com.example.attributions.constants.IM_TIMER_TIMER_NAME
import com.example.attributions.constants.IM_TIMER_TIMER_URI
import com.example.attributions.constants.IM_UK_LANGUAGE_NAME
import com.example.attributions.constants.IM_UK_LANGUAGE_URI
import com.example.attributions.constants.IONICONS_AUTHOR
import com.example.attributions.constants.KONSTANTIN_FILATOV_AUTHOR
import com.example.attributions.constants.KRYSTONSCHWARZE_AUTHOR
import com.example.attributions.constants.MOUDESAIN_AUTHOR
import com.example.attributions.constants.PAOMEEDIA_AUTHOR
import com.example.attributions.constants.PETR_BILEK_AUTHOR
import com.example.attributions.constants.SCARLAB_AUTHOR
import com.example.attributions.constants.SOFTWARE_MANSION_AUTHOR
import com.example.attributions.constants.SOLAR_ICONS_AUTHOR
import com.example.attributions.constants.SVG_REPO_AUTHOR
import com.example.attributions.constants.TWITTER_AUTHOR
import com.example.attributions.constants.UI_DAZZLE_UI_AUTHOR
import com.example.attributions.constants.URI_ALARM_ICONS
import com.example.attributions.constants.URI_CC_BY_LICENSE
import com.example.attributions.constants.URI_LIFESTYLE_ICONS
import com.example.attributions.constants.URI_MIT_LICENSE
import com.example.attributions.constants.URI_NOTIFICATION_ICONS
import com.example.attributions.constants.VMWARE_AUTHOR
import com.example.attributions.constants.WISHFORGE_GAMES_AUTHOR

/**
 * Displays the landscape layout of the Attributions screen.
 *
 * This composable presents the application's third-party attributions,
 * acknowledgements, and licensing information in a vertically scrollable
 * layout optimized for horizontal (landscape) screens.
 *
 * The screen retrieves the current [UriHandler] from the composition and uses
 * it to open external attribution and licensing links when the user interacts
 * with the corresponding items.
 *
 * The content automatically applies padding for the system navigation bars to
 * ensure that all information remains visible and accessible across devices.
 */
@Composable
fun HorizontalAttributionsScreen(){

    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = spacing12, end = spacing12, top = spacing12)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
    ) {

        //Principal title
        TitleMediumText(
            stringResource(R.string.attribution_label_title)
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        // Icons by Flaticon
        TitleSmallText(
            stringResource(R.string.attribution_flaticon_web_title),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        FlaticonButton(
            title = R.string.attributions_flaticon_alarm,
            uri = URI_ALARM_ICONS,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        FlaticonButton(
            title = R.string.attributions_flaticon_notificaciones,
            uri = URI_NOTIFICATION_ICONS,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        FlaticonButton(
            title = R.string.attributions_flaticon_lifeStyle,
            uri = URI_LIFESTYLE_ICONS,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        // Icons by SVGRepo with license CC BY
        TitleSmallText(
            stringResource(R.string.attribution_svgRepo_web_title_cc),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable{
                uriHandler.openUri(URI_CC_BY_LICENSE)
            }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        AuthorButton(
            iconName = IC_CALENDAR_APP_NAME,
            authorName = UI_DAZZLE_UI_AUTHOR,
            uri = IC_CALENDAR_APP_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_FILE_SAVE_NAME,
            authorName = UI_DAZZLE_UI_AUTHOR,
            uri = IC_FILE_SAVE_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_CALENDAR_SETTINGS_NAME,
            authorName = UI_DAZZLE_UI_AUTHOR,
            uri = IC_CALENDAR_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }

        )

        AuthorButton(
            iconName = IC_PALETTE_SETTINGS_NAME,
            authorName = UI_DAZZLE_UI_AUTHOR,
            uri = IC_PALETTE_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_INTERVAL_TIMER_NAME,
            authorName = UI_DAZZLE_UI_AUTHOR,
            uri = IC_INTERVAL_TIMER_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_NO_FAVORITE_TIMER_NAME,
            authorName = UI_DAZZLE_UI_AUTHOR,
            uri = IM_NO_FAVORITE_TIMER_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_PRIVACY_SETTINGS_NAME,
            authorName = UI_DAZZLE_UI_AUTHOR,
            uri = IC_PRIVACY_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_SETTINGS_APP_NAME,
            authorName = CATALIN_FERTU_AUTHOR,
            uri = IC_SETTINGS_APP_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_STATISTICS_APP_NAME,
            authorName = PETR_BILEK_AUTHOR,
            uri = IC_STATISTICS_APP_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_TIMER_APP_NAME,
            authorName = KRYSTONSCHWARZE_AUTHOR,
            uri = IC_TIMER_APP_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_ACHIEVEMENT_ALARM_MANAGER_NAME,
            authorName = CHANUT_IS_INDUSTRIES_AUTHOR,
            uri = IC_ACHIEVEMENT_ALARM_MANAGER_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_FIND_DATE_HABIT_NAME,
            authorName = SOLAR_ICONS_AUTHOR,
            uri = IC_FIND_DATE_HABIT_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_ADD_HABIT_NAME,
            authorName = SOLAR_ICONS_AUTHOR,
            uri = IC_ADD_HABIT_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_GITHUB_SETTINGS_NAME,
            authorName = KONSTANTIN_FILATOV_AUTHOR,
            uri = IC_GITHUB_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_CALENDAR_SETTINGS_NAME,
            authorName = MOUDESAIN_AUTHOR,
            uri = IM_CALENDAR_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_TIMER_TIMER_NAME,
            authorName = WISHFORGE_GAMES_AUTHOR,
            uri = IM_TIMER_TIMER_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        // Icons by SVGRepo with MIT license
        TitleSmallText(
            stringResource(R.string.attribution_svgRepo_web_title_mit),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable{
                uriHandler.openUri(URI_MIT_LICENSE)
            }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        AuthorButton(
            iconName = IM_SPAIN_SETTINGS_NAME,
            authorName = TWITTER_AUTHOR,
            uri = IM_SPAIN_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_ARROW_BACK_UI_NAME,
            authorName = IONICONS_AUTHOR,
            uri = IC_ARROW_BACK_UI_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_GOOGLE_LOGIN_NAME,
            authorName = GARUDA_TECHNOLOGY_AUTHOR,
            uri = IM_GOOGLE_LOGIN_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_LANGUAGE_SETTINGS_NAME,
            authorName = VMWARE_AUTHOR,
            uri = IC_LANGUAGE_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_OVERLAY_SETTINGS_NAME,
            authorName = ICONSAX_AUTHOR,
            uri = IC_OVERLAY_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_ATTRIBUTIONS_SETTINGS_NAME,
            authorName = EMBLEMICONS_AUTHOR,
            uri = IC_ATTRIBUTIONS_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_STOPWATCH_TIMER_NAME,
            authorName = SOFTWARE_MANSION_AUTHOR,
            uri = IM_STOPWATCH_TIMER_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_TERMS_SETTINGS_NAME,
            authorName = SCARLAB_AUTHOR,
            uri = IC_TERMS_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        // Icons by SVGRepo with license CC0 AND PD
        TitleSmallText(
            stringResource(R.string.attribution_svgRepo_web_title_free),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable{
                uriHandler.openUri(URI_CC_BY_LICENSE)
            }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        AuthorButton(
            iconName = IM_UK_LANGUAGE_NAME,
            authorName = SVG_REPO_AUTHOR,
            uri = IM_UK_LANGUAGE_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_HABIT_ADD_HABIT_NAME,
            authorName = SVG_REPO_AUTHOR,
            uri = IM_HABIT_ADD_HABIT_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_NO_HABIT_HABIT_NAME,
            authorName = SVG_REPO_AUTHOR,
            uri = IM_NO_HABIT_HABIT_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_THEME_SETTINGS_NAME,
            authorName = SVG_REPO_AUTHOR,
            uri = IM_THEME_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_LANGUAGE_SETTINGS_NAME,
            authorName = SVG_REPO_AUTHOR,
            uri = IM_LANGUAGE_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_NO_STATISTICS_STATISTICS_NAME,
            authorName = SVG_REPO_AUTHOR,
            uri = IC_NO_STATISTICS_STATISTICS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IM_FAVORITE_TIMER_NAME,
            authorName = PAOMEEDIA_AUTHOR,
            uri = IM_FAVORITE_TIMER_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_LINK_SETTINGS_NAME,
            authorName = PAOMEEDIA_AUTHOR,
            uri = IC_LINK_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_EMAIL_SETTINGS_NAME,
            authorName = PAOMEEDIA_AUTHOR,
            uri = IC_EMAIL_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        AuthorButton(
            iconName = IC_SAVE_SETTINGS_NAME,
            authorName = PAOMEEDIA_AUTHOR,
            uri = IC_SAVE_SETTINGS_URI,
            onClick = { uri -> uriHandler.openUri(uri) }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))
    }

}