package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardDailyHabitDialog(
    onCancelClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {}
){
    val size = remember { mutableStateOf(24.dp) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = spacing8, bottom = spacing8, end = spacing8)
            .wrapContentHeight()
    ) {

        iconDialog(size.value, R.drawable.ic_calendar_2) {
            onCalendarClick()
        }

        Spacer(modifier = Modifier.padding(end = spacing8))

        iconDialog(size.value, R.drawable.ic_edit) {
            onEditClick()
        }

        Spacer(modifier = Modifier.padding(end = spacing8))

        iconDialog(size.value, R.drawable.ic_delete) {
            onDeleteClick()
        }

        Spacer(modifier = Modifier.weight(1f))

        iconDialog(size.value, R.drawable.ic_cancel_2) {
            onCancelClick()
        }
    }
}










@SuppressLint("NewApi", "UnrememberedMutableInteractionSource")
@Composable
fun iconDialog(size: Dp, icon: Int, oncClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .wrapContentSize()// Tamaño total del fondo redondeado
            .background(color = secondaryColorApp, shape = CircleShape) // Fondo redondo
            .padding(8.dp) // Espaciado interno opcional
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) { oncClick() }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = DarKThemeText,
            modifier = Modifier.size(size) // Asegura que el ícono se ajuste al fondo
        )
    }
}