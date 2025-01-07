package aeb.proyecto.habittracker.ui.screens.saveHabit.saveHabitComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.user.UserData
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing24
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SaveHabitScreen() {

    val data = remember { UserData }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = spacing24, vertical = spacing16)) {

        TitleLargeText(
            stringResource(
                R.string.save_habit_title,
                data.email?.substringBefore("@") ?: ""
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        LabelMediumText(stringResource(R.string.save_habit_subtitle)
        ,modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Left)

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        CustomFilledButton(
            title = R.string.save_habit_add_habit,
            icon = null,
            color = ColorsTheme.terciaryColorApp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomFilledButton(
            title = R.string.save_habit_change_email,
            icon = null,
            color = ColorsTheme.terciaryColorApp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        CustomFilledButton(
            title = R.string.save_habit_log_out,
            icon = null,
            color = ColorsTheme.terciaryColorApp,
            modifier = Modifier.fillMaxWidth()
        )

    }

}