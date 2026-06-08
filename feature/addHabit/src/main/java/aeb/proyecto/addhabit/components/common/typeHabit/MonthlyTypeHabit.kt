package aeb.proyecto.addhabit.components.common.typeHabit

import aeb.proyecto.addhabit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

// ============================================================================
// MONTHLY VIEWPORT ARCHITECTURE METRICS
// ============================================================================

val height = 45.dp
const val NUMBER_ELEMENTS = 7

/**
 * Immutable registry index representing standard maximum calendar days for monthly matrix populations.
 */
val days = (1..31).toList()

/**
 * A highly structural layout component that manages the custom configuration strategy for monthly habit targets.
 * Features an animated workflow shifting reactively between a global monthly switch goal rule
 * and an exhaustive granular date picker selection timeline layout.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param colorSelected The primary active design [Color] token allocated to paint structural focus indicator highlights.
 * @param monthlyGoal Core state flag indicating whether the objective calculation defaults to an open monthly cumulative ceiling.
 * @param contrastColor An accessible high-contrast [Color] reference targeted to tint internal text layers during active selections.
 * @param numberSelected Currently active configuration boundary marker identifying the focused picked calendar day integer.
 * @param onNumberSelected State-mutation callback lambda tracking incremental numeric changes downstream.
 * @param onCheckedMonthly Interactive toggle action callback hub tracking global switch configuration shifts.
 */
@Composable
fun MonthlyTypeHabit(
    modifier: Modifier = Modifier,
    colorSelected: Color = MaterialTheme.colorScheme.primary,
    monthlyGoal:Boolean,
    contrastColor: Color = Color.Black,
    numberSelected:Int = 5,
    onNumberSelected: (Int) -> Unit = {},
    onCheckedMonthly: () -> Unit = {}
){

    Column (
        modifier = modifier,
    ){

        // SECTION 1: GLOBAL MONTHLY STRATEGY TOGGLE HEADER ROW
        Row (
            modifier = Modifier.fillMaxWidth().padding(vertical = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ){
                LabelLargeText(stringResource(R.string.add_habit_monthly_goal))

                LabelSmallText(stringResource(R.string.add_habit_monthly_type_label),
                    color = MaterialTheme.colorScheme.outline)
            }

            Switch(
                modifier = Modifier.padding(start = spacing8),
                checked = monthlyGoal,
                onCheckedChange = {onCheckedMonthly()},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorSelected,
                    checkedBorderColor = colorSelected
                )
            )
        }

        // SECTION 2: GRANULAR TARGET PICKER STEP WRAPPER
        AnimatedVisibility(
            visible = !monthlyGoal
        ) {
            Column {
                LabelMediumText(stringResource(R.string.add_habit_monthly_type_title))

                NumberPicker(
                    modifier = Modifier.padding(top = spacing8),
                    colorSelected = colorSelected,
                    contrastColor = contrastColor,
                    numberSelected = numberSelected,
                    onNumberSelected = onNumberSelected
                )
            }
        }
    }

}

/**
 * A highly-customized fluid numeric wheel picker components engineered on top of [HorizontalPager].
 * Projects an interactive snap-to-center single-row calendar matrix calculating advanced trigonometric
 * opacity transforms on the graphical layer to fade out side elements dynamically.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param colorSelected The primary active design [Color] token allocated to paint the centralized highlight anchor box workspace.
 * @param contrastColor Accessible high-contrast [Color] layer mapped onto highlighted texts.
 * @param numberSelected Core layout configuration pointer identifying the currently active focused calendar day integer.
 * @param onNumberSelected Event callback lambda transmitting finalized numeric selection values downstream.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    colorSelected: Color = MaterialTheme.colorScheme.primary,
    contrastColor: Color = Color.Black,
    numberSelected: Int,
    onNumberSelected: (Int) -> Unit = {}
) {

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        // Core structural metric variables mapping fluid distribution lines across the constraints sheet
        val contentPadding = (maxWidth - height) / 2
        val offSet = maxWidth / NUMBER_ELEMENTS
        val itemSpacing = offSet - height
        val pagerState = rememberPagerState(pageCount = { days.size}, initialPage = numberSelected - 1)

        val scope = rememberCoroutineScope()

        // Sinks dynamic alignment adjustments safely back up onto structural business layers
        LaunchedEffect (pagerState.currentPage){
            onNumberSelected(days[pagerState.currentPage])
        }

        val mutableInteractionSource = remember {
            MutableInteractionSource()
        }

        // Background highlight track indicating the active viewport node anchor frame
        CenterBox(
            modifier = modifier
                .align(Alignment.Center),
            color = colorSelected,
        )

        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            contentPadding = PaddingValues(horizontal = contentPadding),
            pageSpacing = itemSpacing,
        ) { page ->
            Box(
                modifier = Modifier
                    .size(height)
                    .graphicsLayer {
                        // Calculate standard fractional offsets from focus core lines
                        val pageOffset = ((pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction).absoluteValue

                        // Map alpha layers dynamically to fade away outbound items elegantly
                        val percentFromCenter = 1.0f - (pageOffset / (5f / 2f))
                        val opacity = 0.25f + (percentFromCenter * 0.75f).coerceIn(0f, 1f)

                        alpha = opacity
                        clip = true
                    }
                    .clickable(
                        interactionSource = mutableInteractionSource,
                        indication = null,
                        enabled = true,
                    ) {
                        scope.launch {
                            // Smoothly snap the viewport over to tapped coordinates
                            pagerState.animateScrollToPage(page)
                        }
                    }) {

                LabelLargeText(
                    text = "${days[page]}",
                    modifier = Modifier
                        .size(height)
                        .wrapContentHeight(),
                    color = colorText(days[page],numberSelected,contrastColor),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )

            }
        }
    }
}

/**
 * A static background anchor lens component designed to highlight the focused wheel area.
 * Placed structurally beneath the scroll track viewport layers to frame the active picker index choice.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param color The active design [Color] token assigned to paint the central selection box canvas.
 */
@Composable
fun CenterBox(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .size(height)
            .clip(RoundedCornerShape(spacing12))
            .background(color)
    ) {
    }
}

/**
 * Computes the correct accessible contrast text color token distribution based on structural focus coordinates.
 * Switches to a high-contrast ink profile when a specific page value aligns with the active chosen slot.
 *
 * @param page The numeric value representation of the current grid item being processed.
 * @param numberSelected The targeted configuration boundary marker identifying the globally focused choice index.
 * @param contrastColor The accessible high-contrast ink tint targeted to override selected item layers.
 */
@Composable
fun colorText(page:Int, numberSelected:Int,contrastColor: Color):Color{
    return if(page == numberSelected) contrastColor else MaterialTheme.colorScheme.onSurface
}