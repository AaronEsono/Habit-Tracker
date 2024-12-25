package aeb.proyecto.habittracker.ui.components.bottomSheets

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.SetStatusColorBar
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.buttons.CustomOutlinedButtonButton
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
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
    color: Color,
    onCancel: () -> Unit = {},
    onAccept: () -> Unit = {},
    showCancel: Boolean = false,
    onDismiss: () -> Unit = {},
    @StringRes titleAccept: Int = R.string.buttons_accept,
    @DrawableRes iconAccept: Int = R.drawable.ic_check,
    @StringRes titleCancel: Int = R.string.buttons_cancel,
    @DrawableRes iconCancel: Int = R.drawable.ic_cancel,
    @StringRes title: Int,
    @StringRes subtitle: Int
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val cancelRemember = remember { showCancel }

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = ColorsTheme.secondaryColorApp
    ) {
        SetStatusColorBar()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = spacing16, vertical = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleLargeText(
                text = stringResource(title),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            LabelMediumText(
                text = stringResource(subtitle),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            Row(modifier = Modifier.fillMaxWidth()) {

                if (cancelRemember) {
                    CustomOutlinedButtonButton(
                        title = titleCancel, icon = iconCancel,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onCancel()

                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss()
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = spacing8))
                }

                CustomFilledButton(
                    title = titleAccept,
                    icon = iconAccept,
                    color = color,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onAccept()

                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss()
                            }
                        }
                    }
                )

            }

        }

    }
}