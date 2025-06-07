package aeb.proyecto.addhabit.components.commom.dialog

import aeb.proyecto.addhabit.R
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.listFrequency
import aeb.proyecto.room.model.classes.listQuantity
import aeb.proyecto.room.model.classes.listTime
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing14
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PickUnitDialog(
    unitSeleted: UnitHabit,
    colorSelected:Color,
    contrastColor:Color,
    onDismissRequest: () -> Unit = {},
    onClickButton: (UnitHabit) -> Unit = {}
){

    CustomDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing14, vertical = spacing12)
        ){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing8)
            ){
                Image(
                    painter = painterResource(R.drawable.im_unit),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )

                CustomRipple{
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "close button",
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onDismissRequest() },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Column (
                modifier = Modifier.verticalScroll(rememberScrollState())
            ){
                BodyMediumText(
                    stringResource(R.string.add_habit_frecuency_title),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = spacing4)
                )

                LabelMediumText(stringResource(R.string.add_habit_frecuency_label))

                FlowRowCards(
                    listFrequency,
                    colorSelected = colorSelected,
                    contrastColor = contrastColor,
                    unitSelected = unitSeleted,
                    onClickButton = { unit ->
                        onClickButton(unit)
                        onDismissRequest()
                    })

                BodyMediumText(
                    stringResource(R.string.add_habit_quantity_title),
                    modifier = Modifier.padding(top = spacing4, bottom = spacing4),
                    fontWeight = FontWeight.Bold
                )

                LabelMediumText(stringResource(R.string.add_habit_quantity_label))

                FlowRowCards(
                    listQuantity,
                    colorSelected = colorSelected,
                    contrastColor = contrastColor,
                    unitSelected = unitSeleted,
                    onClickButton = { unit ->
                        onClickButton(unit)
                        onDismissRequest()
                    })

                BodyMediumText(
                    stringResource(R.string.add_habit_time_title),
                    modifier = Modifier.padding(top = spacing4, bottom = spacing4),
                    fontWeight = FontWeight.Bold
                )

                LabelMediumText(stringResource(R.string.add_habit_time_label))

                FlowRowCards(
                    listTime,
                    colorSelected = colorSelected,
                    contrastColor = contrastColor,
                    unitSelected = unitSeleted,
                    onClickButton = { unit ->
                        onClickButton(unit)
                        onDismissRequest()
                    })
            }
        }
    }
}

@Composable
fun UnitCard(
    unit: UnitHabit,
    colorSelected: Color,
    contrastColor: Color,
    onClick: () -> Unit = {}
){
    CustomRipple {
        Row (
            modifier = Modifier
                .clip(RoundedCornerShape(spacing6))
                .background(colorSelected)
                .clickable { onClick() }
                .padding(spacing6),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                unit.icon,
                contentDescription = "icon card button",
                tint = contrastColor,
                modifier = Modifier.size(20.dp),
            )

            Spacer(modifier = Modifier.padding(horizontal = spacing2))

            LabelMediumText(stringResource(unit.titlePlural), color = contrastColor)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowCards(
    list: List<UnitHabit>, onClickButton: (UnitHabit) -> Unit = {},
    colorSelected: Color, contrastColor: Color, unitSelected: UnitHabit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = spacing6, top = spacing8),
        verticalArrangement = Arrangement.spacedBy(spacing8, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(spacing8)
    ) {

        list.forEach { unit ->
            UnitCard(
                unit = unit,
                colorSelected = colorSelected(unit, unitSelected, colorSelected),
                contrastColor = contrastSelected(unit, unitSelected, contrastColor),
                onClick = {
                    onClickButton(unit)
                }
            )
        }
    }
}

@Composable
fun colorSelected(unit: UnitHabit, unitSelected: UnitHabit, colorSelected: Color):Color{
    return if(unit == unitSelected) colorSelected else MaterialTheme.colorScheme.background
}

@Composable
fun contrastSelected(unit: UnitHabit, unitSelected: UnitHabit, contrastSelected: Color):Color{
    return if(unit == unitSelected) contrastSelected else MaterialTheme.colorScheme.onSurface
}