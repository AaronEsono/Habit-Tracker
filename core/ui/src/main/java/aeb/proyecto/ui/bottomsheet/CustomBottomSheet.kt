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