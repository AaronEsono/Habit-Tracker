package aeb.proyecto.habittracker.ui.components.bottomSheets

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.text.LabelLargeText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.ui.components.textField.CustomOutlinedTextField
import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Constans.onlyDigits
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetChoseSteps(
    color: Color,
    onAccept: (String) -> Unit = {},
    titleUnit: Int,
    restDays: Int,
    onDismiss: () -> Unit = {},
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val timesHabit = rememberTextFieldState("")
    val context = LocalContext.current

    LaunchedEffect(timesHabit.text) {
        if (!timesHabit.text.toString().matches(onlyDigits) && timesHabit.text.toString()
                .isNotEmpty()
        ) {
            timesHabit.edit {
                delete(timesHabit.text.length - 1, timesHabit.text.length)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = ColorsTheme.secondaryColorApp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = spacing16, end = spacing16, bottom = spacing16, top = spacing8)
        ) {

            LabelLargeText(
                text = stringResource(R.string.chose_step_bottom_sheet_title, stringResource(titleUnit)), modifier = Modifier
                    .padding(bottom = spacing8)
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing8, vertical = spacing4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ChoseTimes(restDays/2){ text -> timesHabit.edit { replace(0, length, text) }}
                Spacer(modifier = Modifier.padding(horizontal = spacing8))
                ChoseTimes(restDays){ text -> timesHabit.edit { replace(0, length, text) }}
            }

            CustomOutlinedTextField(
                rememberTextFieldState = timesHabit,
                label = R.string.add_habit_screen_name,
                labelError = R.string.add_habit_screen_no_units,
                isNeeded = true,
                showLabel = false,
                isNumeric = true,
                modifier = Modifier
                    .padding(spacing8)
                    .fillMaxWidth()
                    .height(60.dp)
            )

            CustomFilledButton(
                title = R.string.buttons_accept,
                icon = R.drawable.ic_check,
                color = color,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing16),
                onClick = {
                    if(timesHabit.text.isNotEmpty() && timesHabit.text.toString().toInt() <= restDays){
                        onAccept(timesHabit.text.toString())
                        onDismiss()
                    }else{
                        Toast.makeText(context, R.string.chose_step_bottom_sheet_toast, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

}

@Composable
fun ChoseTimes(
    restDays: Int,
    onAccept: (String) -> Unit = {},
){
    Column(
        modifier = Modifier
            .background(ColorsTheme.primaryColorApp)
            .border(1.dp, color = ColorsTheme.themeText, RoundedCornerShape(spacing8))
            .clickable {
                onAccept(restDays.toString())
            }
            .padding(horizontal = spacing16)


    ) {
        TitleSmallText(
            text = restDays.toString(),
            modifier = Modifier.padding(vertical = spacing8, horizontal = spacing16)
        )
    }
}