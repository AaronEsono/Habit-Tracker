package aeb.proyecto.habittracker.ui.screens.saveHabit.saveHabitComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.di.LastSearched
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.card.CardImport
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing24
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SaveHabitScreen(name:String,saveData:() -> Unit,logOut: () -> Unit,onDelete:() -> Unit,restoreData:() -> Unit, date: String?) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = spacing24, vertical = spacing16)) {

        TitleLargeText(
            stringResource(R.string.save_habit_title, name),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        LabelMediumText(stringResource(R.string.save_habit_subtitle)
        ,modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Left)

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        CardImport {
            if(date.isNullOrEmpty()){
                LabelMediumText(
                    stringResource(R.string.save_habit_no_data),
                    modifier = Modifier.padding(horizontal = spacing8)
                )
            }else{
                Column(modifier = Modifier.padding(horizontal = spacing8), verticalArrangement = Arrangement.Center) {
                    LabelMediumText(stringResource(R.string.save_habit_data),textAlign = TextAlign.Left)

                    Spacer(modifier = Modifier.padding(vertical = spacing2))

                    BodySmallText(stringResource(R.string.save_habit_data_date, date), textAlign = TextAlign.Left)
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        if(!date.isNullOrEmpty()){
            CustomFilledButton(
                title = R.string.save_habit_data_restore,
                icon = null,
                color = MaterialTheme.colorScheme.onSurface,
                colorIcon = MaterialTheme.colorScheme.inverseOnSurface,
                colorText = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.fillMaxWidth(),
                onClick = { restoreData() }
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            CustomFilledButton(
                title = R.string.save_habit_data_delete,
                icon = null,
                color = MaterialTheme.colorScheme.onSurface,
                colorIcon = MaterialTheme.colorScheme.inverseOnSurface,
                colorText = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onDelete() }
            )
        }else{
            CustomFilledButton(
                title = R.string.save_habit_add_habit,
                icon = null,
                color = MaterialTheme.colorScheme.onSurface,
                colorIcon = MaterialTheme.colorScheme.inverseOnSurface,
                colorText = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.fillMaxWidth(),
                onClick = { saveData() }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CustomFilledButton(
            title = R.string.save_habit_log_out,
            icon = null,
            color = MaterialTheme.colorScheme.onSurface,
            colorIcon = MaterialTheme.colorScheme.inverseOnSurface,
            colorText = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.fillMaxWidth(),
            onClick = { logOut() }
        )

    }
}