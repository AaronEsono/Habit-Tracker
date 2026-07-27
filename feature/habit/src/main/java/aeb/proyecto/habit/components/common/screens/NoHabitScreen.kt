package aeb.proyecto.habit.components.common.screens

import aeb.proyecto.habit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A placeholder screen component displayed when there are no habits recorded.
 *
 * Provides visual feedback to the user, typically used in dashboard or list views
 * to indicate that the habit list is currently empty, encouraging the user to
 * create their first habit.
 */
@Composable
fun NoHabitScreen(){

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing10)
            .testTag("habit_no_habit_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        // Illustrative image representing the empty state
        Image(
            painter = painterResource(R.drawable.im_no_habit),
            contentDescription = "Image no habit",
            modifier = Modifier.size(120.dp)
        )

        // Informative text to guide the user
        LabelLargeText(
            stringResource(R.string.habit_no_habit),
            modifier = Modifier.padding(top = spacing12).fillMaxWidth(),
            textAlign = TextAlign.Center)
    }

}