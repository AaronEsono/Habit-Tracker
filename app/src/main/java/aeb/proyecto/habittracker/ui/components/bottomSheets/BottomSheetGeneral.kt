package aeb.proyecto.habittracker.ui.components.bottomSheets

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.buttons.CustomOutlinedButton
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetGeneral(
    colorButton: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    colorBottomSheet: Color = MaterialTheme.colorScheme.primaryContainer,
    colorIconCancel: Color = MaterialTheme.colorScheme.onSurface,
    colorTextCancel: Color = MaterialTheme.colorScheme.onSurface,
    colorIconAccept: Color = MaterialTheme.colorScheme.onSurface,
    colorTextAccept: Color = MaterialTheme.colorScheme.onSurface,
    borderColorCancel: Color = MaterialTheme.colorScheme.onSurface,
    colorDisabled: Color = MaterialTheme.colorScheme.surfaceVariant,
    @StringRes title: Int,
    @StringRes subtitle: Int,
    onCancel: () -> Unit = {},
    onAccept: () -> Unit = {},
    showCancel: Boolean = false,
    onDismiss: () -> Unit = {},
    textAlign: TextAlign = TextAlign.Center,
    textAlignTitle: TextAlign = TextAlign.Center,
    modifierColumn: Modifier = Modifier,
    modifierTitle: Modifier = Modifier,
    modifierSubtitle: Modifier = Modifier,
    modifierCancel: Modifier = Modifier,
    modifierAccept: Modifier = Modifier,
    @StringRes titleAccept: Int = R.string.buttons_accept,
    @DrawableRes iconAccept: Int = R.drawable.ic_check,
    @StringRes titleCancel: Int = R.string.buttons_cancel,
    @DrawableRes iconCancel: Int = R.drawable.ic_cancel,
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = colorBottomSheet
    ) {

        Column(
            modifier = modifierColumn
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = spacing16, vertical = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleLargeText(
                text = stringResource(title),
                textAlign = textAlignTitle,
                modifier = modifierTitle.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            LabelMediumText(
                text = stringResource(subtitle),
                modifier = modifierSubtitle.fillMaxWidth(),
                textAlign = textAlign
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            Row(modifier = Modifier.fillMaxWidth()) {

                if (showCancel) {
                    CustomOutlinedButton(
                        title = titleCancel,
                        icon = iconCancel,
                        colorIcon = colorIconCancel,
                        colorText = colorTextCancel,
                        modifier = modifierCancel.weight(1f),
                        borderColor = borderColorCancel,
                        onClick = {
                            onCancel()

                            scope.launch {
                                try {sheetState.hide()}
                                finally {onDismiss()}
                            }
                        }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = spacing8))
                }

                CustomFilledButton(
                    title = titleAccept,
                    icon = iconAccept,
                    color = colorButton,
                    colorIcon = colorIconAccept,
                    colorText = colorTextAccept,
                    enabledColor = colorDisabled,
                    modifier = modifierAccept.weight(1f),

                    onClick = {
                        onAccept()

                        scope.launch {
                            try {sheetState.hide()}
                            finally {onDismiss()}
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

        }
    }
}