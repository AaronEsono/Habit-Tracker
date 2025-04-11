package aeb.proyecto.habit.components.screen

import aeb.proyecto.habit.TypeUIState
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerElementScreen(
    pagerElements: List<PagerElement>,
    selectedType: Int,
    onClickTab: (Int) -> Unit = {}
){

    Column (
        modifier = Modifier.fillMaxSize()
    ){

        PrimaryTabRow(selectedTabIndex = selectedType,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedType, matchContentSize = true),
                    width = Dp.Unspecified,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            divider = {
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
            },
            containerColor = MaterialTheme.colorScheme.surfaceTint
        ) {
            pagerElements.forEachIndexed { index, pagerElement ->
                Tab(
                    selected = selectedType == index,
                    onClick = { onClickTab(index) },
                    text = {
                        LabelLargeText(
                            stringResource(pagerElement.title),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        //Content

    }
}