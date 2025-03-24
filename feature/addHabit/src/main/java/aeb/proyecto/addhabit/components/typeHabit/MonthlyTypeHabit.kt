package aeb.proyecto.addhabit.components.typeHabit

import aeb.proyecto.addhabit.R
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun MonthlyTypeHabit(
    modifier: Modifier = Modifier
){

    Column (
        modifier = modifier,
    ){

        LabelLargeText(stringResource(R.string.add_habit_monthly_type_title))

    }

}