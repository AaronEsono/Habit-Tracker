package aeb.proyecto.addhabit.components.typeHabit

import aeb.proyecto.addhabit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
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
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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

val height = 45.dp
const val NUMBER_ELEMENTS = 7
val days = (1..31).toList()

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


    val label = remember (monthlyGoal){
        if (monthlyGoal) R.string.habit_monthly_type_label_true
        else R.string.habit_monthly_type_label_false
    }

    Column (
        modifier = modifier,
    ){

        Row (
            modifier = Modifier.fillMaxWidth().paddingWeeklyGoal(isVisible = monthlyGoal),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ){
                LabelLargeText(stringResource(R.string.habit_monthly_goal))

                LabelSmallText(stringResource(label),color = MaterialTheme.colorScheme.outline)
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

@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    colorSelected: Color = MaterialTheme.colorScheme.primary,
    contrastColor: Color = Color.Black,
    numberSelected: Int,
    onNumberSelected: (Int) -> Unit = {}
) {

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val contentPadding = (maxWidth - height) / 2
        val offSet = maxWidth / NUMBER_ELEMENTS
        val itemSpacing = offSet - height
        val pagerState = rememberPagerState(pageCount = {days.size}, initialPage = numberSelected - 1)

        val scope = rememberCoroutineScope()

        LaunchedEffect (pagerState.currentPage){
            onNumberSelected(days[pagerState.currentPage])
        }

        val mutableInteractionSource = remember {
            MutableInteractionSource()
        }

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
                        val pageOffset = ((pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction).absoluteValue

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

@Composable
fun colorText(page:Int, numberSelected:Int,contrastColor: Color):Color{
    return if(page == numberSelected) contrastColor else MaterialTheme.colorScheme.onSurface
}