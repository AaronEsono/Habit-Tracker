package com.example.attributions.components

import aeb.proyecto.attributions.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.text.LabelLargeText
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
 * Renders an attribution button for SVGRepo authors displaying the icon and author names alongside an external link icon.
 *
 * This composable builds a localized string combining [iconName] and [authorName] into an interactive row
 * that invokes a callback when clicked, passing the targeted [uri].
 *
 * @param modifier The [Modifier] to be applied to the button container layout.
 * @param iconName The name or label of the icon being attributed.
 * @param authorName The name of the creator or author to credit.
 * @param uri The destination target URL string associated with the author's profile or asset.
 * @param onClick Callback triggered when the user clicks the button. Receives the provided [uri] string as a parameter.
 */
@Composable
fun AuthorButton(
    modifier:Modifier = Modifier,
    iconName: String,
    authorName: String,
    uri: String,
    onClick: (String) -> Unit = {},
){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick(uri) }
            .padding(vertical = spacing1)
            .testTag("attribution_author_${iconName}_$authorName")
    ) {
        LabelLargeText(
            text = stringResource(
                R.string.attribution_svgRepo_author_created_by,iconName,authorName
            ),
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