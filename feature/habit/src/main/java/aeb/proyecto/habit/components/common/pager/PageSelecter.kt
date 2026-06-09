package aeb.proyecto.habit.components.common.pager

import aeb.proyecto.habit.CurrentPagerSelection
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A tabbed navigation component that displays a set of [PagerElement] categories.
 *
 * It utilizes [PrimaryTabRow] to represent the selection state, with custom
 * styling for the active indicator and dynamic font scaling based on the
 * number of available tabs.
 *
 * @param pagerElements The list of [PagerElement] categories to display in the tabs.
 * @param currentPagerSelected The current selection state, determining the active tab index.
 * @param onClickTab Callback invoked when a tab is clicked, providing the selected [PagerElement].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSelected(
    pagerElements: List<PagerElement>,
    currentPagerSelected: CurrentPagerSelection,
    onClickTab: (PagerElement) -> Unit = {},
){

    val selectedTabIndex = when (currentPagerSelected) {
        is CurrentPagerSelection.Selected -> currentPagerSelected.pagerSelected.index
        else -> 0 // O un índice predeterminado si no está inicializado
    }

    // Displays habit categories in a tab row with dynamic indicator and sizing
    PrimaryTabRow(selectedTabIndex = selectedTabIndex,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = true),
                width = Dp.Unspecified,
                color = MaterialTheme.colorScheme.onSurface,
                height = 5.dp // PrimaryNavigationTabTokens.ActiveIndicatorHeight
            )
        },
        divider = {
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
        },
        containerColor = MaterialTheme.colorScheme.surfaceTint
    ) {
        pagerElements.forEachIndexed { index, pagerElement ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onClickTab(pagerElement) },
                text = {
                    LabelMediumText(
                        stringResource(pagerElement.title),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = getTextSizePager(pagerElements.size)
                    )
                }
            )
        }
    }
}

/**
 * Helper function to determine the appropriate font size for tab labels
 * based on the total number of tabs to prevent overflow.
 *
 * @param size Total number of tabs in the row.
 * @return A [TextUnit] representing the font size.
 */
fun getTextSizePager(size: Int): TextUnit {
    return when (size) {
        4 -> 10.sp
        3 -> 12.sp
        else -> 14.sp
    }
}