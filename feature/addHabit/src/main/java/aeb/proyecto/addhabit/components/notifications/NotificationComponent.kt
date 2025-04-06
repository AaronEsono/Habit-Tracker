package aeb.proyecto.addhabit.components.notifications

import aeb.proyecto.addhabit.constants.TypeNotificationResult
import aeb.proyecto.ui.date.DaysWeekAvr
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.ui.date.getOrderedDays
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun NotificationComponent(
    modifier: Modifier = Modifier,
    notification: AddHabitNotification,
    startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    color: Color,
    contrastColor:Color,
    onClickDelete: (String) -> Unit = {},
    onClickEdit: (String,LocalTime) -> Unit = {_,_ -> },
    onClickTypeNotification: (TypeNotificationResult) -> Unit = {}
){

    val orderedDays = remember(startDayOfWeek) {
        getOrderedDays(startDayOfWeek)
    }

    Column (
        modifier = modifier,
    ){
        CustomRipple{
            ElevatedCard(
                onClick = {onClickEdit(notification.id,notification.time)},
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
                        modifier = Modifier.size(28.dp)
                    )

                    LabelLargeText(
                        notification.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                        modifier = Modifier.padding(start = spacing12),
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if(notification.type is TypeNotification.Recurring){

                        ArrowCyclicButton(icon = Icons.Filled.Remove,
                            modifier = Modifier.padding(vertical = spacing8),
                            onClick = {
                                onClickTypeNotification(
                                    TypeNotificationResult.Recurring(false, notification.id))
                            }
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = spacing4)
                                .height(40.dp).aspectRatio(1f)
                                .clip(RoundedCornerShape(spacing8))
                                .background(MaterialTheme.colorScheme.background)
                        ){
                            LabelLargeText(notification.type.interval.toString(),
                                modifier = Modifier.align(Alignment.Center),
                                fontSize = 18.sp)
                        }

                        ArrowCyclicButton(icon = Icons.Filled.Add,
                            modifier = Modifier.padding(end = spacing12),
                            onClick = {
                                onClickTypeNotification(
                                    TypeNotificationResult.Recurring(true, notification.id))
                            })
                    }

                    CustomRipple {
                        IconButton(
                            onClick = {onClickDelete(notification.id)}
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete icon",
                                tint = color,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                if(notification.type is TypeNotification.Daily){
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = spacing8,
                                end = spacing8,
                                bottom = spacing6,
                                top = spacing4
                            )
                    ) {
                        orderedDays.forEach { day ->
                            NotificationDayButton(
                                title = day.string,
                                selected = isDaySelected(notification.type.days,day.id),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = spacing1),
                                colorSelected = color,
                                contrastColor = contrastColor,
                                onClick = {
                                    onClickTypeNotification(
                                        TypeNotificationResult.Daily(day.id,notification.id)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArrowCyclicButton(
    modifier: Modifier = Modifier,
    icon:ImageVector,
    onClick: () -> Unit = {}
){
    CustomRipple {
        Icon(
            icon,
            contentDescription = "Icon Leading",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
                .clip(RoundedCornerShape(spacing8))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .size(40.dp)
                .clickable { onClick() }
        )
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

@Composable fun isDaySelected(list:List<DayOfWeek>,day:DayOfWeek):Boolean{
    return list.contains(day)
}