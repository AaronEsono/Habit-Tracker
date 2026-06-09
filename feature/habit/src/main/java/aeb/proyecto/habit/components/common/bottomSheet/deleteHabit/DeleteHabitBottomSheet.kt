package aeb.proyecto.habit.components.common.bottomSheet.deleteHabit

import aeb.proyecto.habit.R
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleLargeText
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch


/**
 * Destructive action modal sheet used to confirm habit removal.
 * Employs high-contrast iconography and dual-action buttons to ensure
 * clear intent before executing permanent data deletion.
 *
 * @param colorButton The habit's unique brand color used for the confirmation button.
 * @param onDismiss Callback to handle the modal cancellation/dismissal flow.
 * @param onAcceptDelete Callback to trigger the permanent deletion logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteHabitBottomSheet (
    colorButton: Int,
    onDismiss: () -> Unit = {},
    onAcceptDelete: () -> Unit = {}
){

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val color = remember (colorButton){
        Color(colorButton)
    }

    CustomBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing12, end = spacing8, bottom = spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header: Warning Icon and Title
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Icon Title delete habit",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(spacing20)
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing3))

                TitleLargeText(stringResource(R.string.bt_delete_title))
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            // Subtitle: Warning explanation
            LabelLargeText(
                stringResource(R.string.bt_delete_subtitle),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacing8)
            )

            Spacer(modifier = Modifier.padding(spacing2))

            // Actions Row: Cancel & Confirm
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing12)
            ) {

                // Cancel Button
                CustomRipple (){
                    OutlinedButton(
                        shape = RoundedCornerShape(spacing12),
                        contentPadding = PaddingValues(vertical = spacing12),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        },
                    ) {
                        Row {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Icon Cancel button",
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.padding(horizontal = spacing2))

                            TitleSmallText(
                                stringResource(R.string.habit_cancel),
                            )
                        }
                    }
                }

                Spacer(Modifier.padding(horizontal = spacing8))

                // Delete Button
                CustomRipple (){
                    OutlinedButton(
                        shape = RoundedCornerShape(spacing12),
                        contentPadding = PaddingValues(vertical = spacing12),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onAcceptDelete()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color
                        ),
                    ) {
                        Row {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Icon Cancel button",
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.padding(horizontal = spacing2))

                            TitleSmallText(
                                stringResource(R.string.habit_accept),
                            )
                        }
                    }

                }
            }
        }
    }
}

