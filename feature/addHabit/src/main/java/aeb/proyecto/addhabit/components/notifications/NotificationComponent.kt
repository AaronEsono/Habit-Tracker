package aeb.proyecto.addhabit.components.notifications

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.typeHabit.WeeklyButton
import aeb.proyecto.addhabit.components.typeHabit.numberOfDaysWeek
import aeb.proyecto.addhabit.components.typeHabit.numberSelected
import aeb.proyecto.addhabit.constants.DaysWeekAvr
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.addhabit.model.TypeNotification
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter

@Composable
fun NotificationComponent(
    modifier: Modifier = Modifier,
    notification: AddHabitNotification,
    color: Color,
    contrastColor:Color,
    onClickEdit: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onClickTypeNotification: () -> Unit = {}
){

    Column (
        modifier = modifier,
    ){
        CustomRipple {
            ElevatedCard(
                onClick = { },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = spacing8),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Icon Leading",
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )

                    LabelLargeText(
                        notification.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                        modifier = Modifier.padding(start = spacing12),
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CustomRipple {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete icon",
                                tint = color,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                when(notification.type){
                    is TypeNotification.Daily -> {

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = spacing8, end = spacing8, bottom = spacing6)
                        ) {
                            DaysWeekAvr.entries.forEach { day ->
                                NotificationDayButton(
                                    title = day.string,
                                    selected = isDaySelected(notification.type.days,day.id),
                                    modifier = Modifier.weight(1f).padding(horizontal = spacing1),
                                    colorSelected = color,
                                    contrastColor = contrastColor,
                                )
                            }
                        }
                    }
                    is TypeNotification.Recurring -> {

                    }
                }
            }
        }
    }
}

@Composable
fun NotificationDayButton(
    modifier: Modifier = Modifier,
    title:Int,
    selected:Boolean,
    colorSelected:Color,
    contrastColor:Color,
    onClick: () -> Unit = {}
) {
    val containerColor =
        if (selected) colorSelected else MaterialTheme.colorScheme.background
    val titleColor =
        if (selected) contrastColor else MaterialTheme.colorScheme.onSurface

    CustomRipple {
        ElevatedCard(
            modifier = modifier
                .padding(horizontal = spacing2)
                .height(35.dp),
            onClick = onClick,
            shape = RoundedCornerShape(spacing8),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LabelLargeText(stringResource(title),color = titleColor)
            }
        }
    }
}

@Composable fun isDaySelected(list:List<Int>,day:Int):Boolean{
    return list.contains(day)
}