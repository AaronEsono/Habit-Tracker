package aeb.proyecto.addhabit.components.common.dialog

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

/**
 * A comprehensive modal metric selection dialog that serves as the central configuration dashboard
 * for unit metrics. Organizes dynamic units into distinct behavioral sections (Frequency, Quantity,
 * and Time boundaries) leveraging reuse-optimized fluid wrapping layouts.
 *
 * @param unitSeleted The active runtime marker identifying the currently selected [UnitHabit] metric.
 * @param colorSelected The calculated custom design [Color] token used to highlight the focused selection node.
 * @param contrastColor An accessible high-contrast [Color] reference mapped onto content labels inside the active state.
 * @param onDismissRequest Contextual closure callback lambda dispatched to close or unmount the dialog overlay layer.
 * @param onClickButton Action callback lambda carrying the finalized [UnitHabit] option clicked by the user.
 */
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

            // Header decorative asset block managing image resources and close anchors
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

            // Scroll-defended core catalog wrapper separating structural metrics segments
            Column (
                modifier = Modifier.verticalScroll(rememberScrollState())
            ){
                // SECTION 1: FREQUENCY METRICS
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

                // SECTION 2: QUANTITY METRICS
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

                // SECTION 3: TIME BOUNDARY METRICS
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

/**
 * An atomic selective chip element representing a standalone measurement unit metric wrapper.
 * Adapts its internal iconography and background tokens dynamically based on focused activation states.
 *
 * @param unit The structural metric configuration model [UnitHabit] anchoring text tokens and vector graphics.
 * @param colorSelected The calculated contextual [Color] token injected to paint the chip background workspace.
 * @param contrastColor An accessible high-contrast [Color] reference targeted to tint internal text and graphic nodes.
 * @param onClick Interactive click action callback lambda triggered when the user selects this target tile.
 */
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

/**
 * An elastic adaptive flex-wrap container layer designed to lay out unit metrics across dynamic fluid lines.
 * Prevents text truncation across dynamic system font scaling setups by leveraging flow distribution paths.
 *
 * @param list Core immutable collection registry tracking available [UnitHabit] instances to inflate.
 * @param onClickButton Callback action lambda tracking single-tap unit update intents.
 * @param colorSelected Personalized design accent [Color] token applied over the active selection tile background.
 * @param contrastColor High-contrast accessible design [Color] token assigned to paint content elements inside the selected state.
 * @param unitSelected Currently active configuration boundary marker identifying the runtime selected target metric type.
 */
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

/**
 * Evaluates operational background colors defensively based on focused active configuration rules.
 */
@Composable
fun colorSelected(unit: UnitHabit, unitSelected: UnitHabit, colorSelected: Color):Color{
    return if(unit == unitSelected) colorSelected else MaterialTheme.colorScheme.background
}

/**
 * Evaluates operational text content contrast color balances based on focused active configuration rules.
 */
@Composable
fun contrastSelected(unit: UnitHabit, unitSelected: UnitHabit, contrastSelected: Color):Color{
    return if(unit == unitSelected) contrastSelected else MaterialTheme.colorScheme.onSurface
}