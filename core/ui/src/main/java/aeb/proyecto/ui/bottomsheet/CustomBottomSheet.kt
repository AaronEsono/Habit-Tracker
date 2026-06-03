package aeb.proyecto.ui.bottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Reusable layout wrapper abstraction over the Material 3 [ModalBottomSheet] infrastructure.
 * Encapsulates boilerplates regarding experimental API tracking tokens and default state caching,
 * positioning generic composable content slots to standardize contextual bottom sheets across screens.
 *
 * @param modifier The structural composition modifier layout adjustment token.
 * @param onDismiss The programmatic callback closure triggered when the sheet is swiped down or dismissed via scrim clicks.
 * @param containerColor The surface background color bound by default to the active thematic container token.
 * @param sheetState The structural state tracker governing modal presentation vectors, defaults to standard animations.
 * @param content The declarative architectural sub-view sheet layout injected into the bottom sheet canvas wrapper.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = containerColor,
        sheetState = sheetState
    ) {
        content()
    }

}