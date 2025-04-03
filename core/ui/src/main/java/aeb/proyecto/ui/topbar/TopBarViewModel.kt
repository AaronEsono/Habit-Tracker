package aeb.proyecto.ui.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TopBarViewModel : ViewModel() {

    var title by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())

    var navigationIcon by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())

    var actions by mutableStateOf<@Composable RowScope.() -> Unit>({ }, referentialEqualityPolicy())

}