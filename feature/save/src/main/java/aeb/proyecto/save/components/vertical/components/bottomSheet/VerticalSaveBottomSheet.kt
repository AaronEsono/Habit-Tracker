package aeb.proyecto.save.components.vertical.components.bottomSheet

import aeb.proyecto.save.components.common.button.BottomSheetFilledButton
import aeb.proyecto.save.components.common.button.BottomSheetOutLinedButton
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

/**
 * A vertical layout for modal bottom sheets used in the Save module.
 * Designed for portrait orientation or narrow screens where vertical stacking
 * of action buttons improves usability.
 * @param dataBottomSheet The configuration data defining the icon, title, and labels.
 * @param onDismiss Callback triggered when the sheet is closed or cancelled.
 * @param onAccept Callback triggered when the primary action button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalSaveBottomSheet(
    dataBottomSheet: DataBottomSheet,
    onDismiss: () -> Unit = {},
    onAccept: () -> Unit = {}
) {

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    CustomBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
        modifier = Modifier.testTag("vertical_settings_dialog")
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing8, end = spacing8, bottom = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    dataBottomSheet.iconTitle,
                    contentDescription = "Icon Title",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(spacing20)
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing3))

                TitleLargeText(stringResource(dataBottomSheet.title))
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            LabelLargeText(
                stringResource(dataBottomSheet.label),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacing8)
            )

            Spacer(modifier = Modifier.padding(spacing2))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing12)
            ) {

                when (dataBottomSheet) {
                    DataBottomSheet.SAVE_HABIT, DataBottomSheet.DELETE_HABIT, DataBottomSheet.LOG_OUT, DataBottomSheet.RESTORE_HABIT,DataBottomSheet.DELETE_ACCOUNT -> {
                        BottomSheetOutLinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("vertical_settings_dialog_cancel"),
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onDismiss()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.padding(horizontal = spacing8))
                    }

                    else -> Unit
                }

                BottomSheetFilledButton(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("vertical_settings_dialog_accept"),
                    onClick = {
                        coroutineScope.launch {
                            onAccept()
                            sheetState.hide()
                        }
                    }
                )
            }
        }
    }
}