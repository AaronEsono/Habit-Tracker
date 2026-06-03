package aeb.proyecto.ui.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * High-performance state-orchestrator governing the global application toolbar layout profile.
 * Acts as an architectural structural bridge allowing separate decoupled display screen nodes to dynamically
 * inject text headings, interactive anchors, and actions into the main single-host scaffold framework.
 *
 * Implements strict [referentialEqualityPolicy] strategies across all state slots to suppress layout
 * invalidation noise and prevent continuous compiler lambda recomposition loops.
 */
class TopBarViewModel : ViewModel() {

    /**
     * Stateful composable slot tracking the active horizontal title view container.
     * Defaults to an empty structural closure.
     */
    var title by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())

    /**
     * Stateful composable slot driving the leading operational navigation action asset
     * (e.g., Back buttons or drawer anchors). Defaults to an empty structural closure.
     */
    var navigationIcon by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())

    /**
     * Stateful row-scoped composable slot managing the trailing command action matrix array
     * (e.g., Save anchors, calendar menus, context controls). Defaults to an empty structural closure.
     */
    var actions by mutableStateOf<@Composable RowScope.() -> Unit>({ }, referentialEqualityPolicy())
}