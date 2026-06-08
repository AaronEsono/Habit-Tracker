package aeb.proyecto.addhabit.components.common.dialog

import aeb.proyecto.addhabit.R
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * A highly-customized modal system date picker dialog tailored for habit timeline definitions.
 * Synchronizes selected epoch timestamps into standard localized [LocalDate] primitives while
 * overriding the core Material 3 design palette to align with user-selected theme accent tokens.
 *
 * @param onDismissRequest Contextual callback lambda fired when the dialogue layer mounts a cancellation execution.
 * @param colorSelected The personalized custom design [Color] allocated to highlight active selection nodes.
 * @param contrastColor An accessible, high-contrast [Color] mapped onto text nodes inside highlighted selection states.
 * @param onClickDate Event callback lambda carrying the finalized [LocalDate] object selected by the user.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogHabit(
    onDismissRequest: () -> Unit,
    colorSelected:Color,
    contrastColor: Color,
    onClickDate:(LocalDate) -> Unit,
){
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.verticalScroll(rememberScrollState()),
        confirmButton = {
            CustomRipple {
                TextButton(onClick = {
                    // Cast selected epoch milliseconds into standard localized calendar primitives
                    val localDate = datePickerState.selectedDateMillis?.let { millis ->
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    } ?: LocalDate.now()

                    onClickDate(localDate)
                    onDismissRequest()
                }) {
                    LabelLargeText(stringResource(R.string.add_habit_accept))
                }
            }
        },
        dismissButton = {
            CustomRipple {
                TextButton(onClick = onDismissRequest) {
                    LabelLargeText(stringResource(R.string.add_habit_cancel))
                }
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,

                // Dynamic chosen color customization vectors
                selectedDayContainerColor = colorSelected,
                selectedYearContainerColor = colorSelected,

                selectedDayContentColor = contrastColor,
                selectedYearContentColor = contrastColor,

                dayContentColor = MaterialTheme.colorScheme.onSurface,
                yearContentColor = MaterialTheme.colorScheme.onSurface,
                todayContentColor = MaterialTheme.colorScheme.onSurface,
                todayDateBorderColor = MaterialTheme.colorScheme.onSurface,
                currentYearContentColor = MaterialTheme.colorScheme.onSurface,

                dateTextFieldColors =   TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,

                    focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    errorTextColor = MaterialTheme.colorScheme.error,
                    cursorColor = MaterialTheme.colorScheme.onSurface,

                    selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.onSurface,
                        backgroundColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            ))
    }
}