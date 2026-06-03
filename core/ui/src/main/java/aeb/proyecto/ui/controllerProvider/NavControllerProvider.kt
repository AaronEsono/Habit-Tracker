package aeb.proyecto.ui.controllerProvider

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

/**
 * Global static composition dependency provider hosting the active platform [NavHostController].
 * Mitigates parametric propagation overhead ("property drilling") by exposing an implicit,
 * tree-scoped navigation routing vector accessible by down-stream visual screen nodes.
 *
 * Employs [staticCompositionLocalOf] to eliminate runtime node-tracking memory footprints,
 * as the root controller reference remains monolithic and immutable post-initialization.
 * * Throws an [IllegalStateException] if targeted outside a valid composition provider environment.
 */
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController no está disponible en este contexto.")
}