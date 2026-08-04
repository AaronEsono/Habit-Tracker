package com.example.attributions.components

import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Renders an attribution button for Flaticon resources displaying a localized text label and an external link icon.
 *
 * This composables provides an interactive row that triggers a callback when clicked, passing the targeted [uri].
 *
 * @param modifier The [Modifier] to be applied to the button container layout.
 * @param title String resource identifier ([Int]) representing the localized title or attribution text.
 * @param uri The destination target URL string associated with the attribution source.
 * @param onClick Callback triggered when the user clicks the button. Receives the provided [uri] string as a parameter.
 */
@Composable
fun FlaticonButton(
    modifier:Modifier = Modifier,
    title:Int,
    uri: String,
    onClick: (String) -> Unit = {}
){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick(uri) }
            .padding(vertical = spacing1)
            .testTag("attribution_flaticon_button_$uri")
    ) {
        LabelLargeText(
            text = stringResource(title),
            color = MaterialTheme.colorScheme.scrim,
            modifier = Modifier.weight(1f, fill = false)

        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.scrim,
            modifier = Modifier.size(14.dp)
        )
    }

}