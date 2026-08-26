package aeb.proyecto.habittracker.components.onboardScreen

import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.ResultOptions
import aeb.proyecto.habittracker.components.onboardScreen.vertical.VerticalOnboardingScreen
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnboardScreen(
    pageSelected: OnboardingPage,
    onClickResultOption: (ResultOptions) -> Unit
){

    // Esquema
    // Pantallas vertical y orizontal con orientation

    // Clases
    // Horizontal pager, donde los elemenos van a ser OnboardingPage
    // OnboardingPage tendra dentro su lottie y texto
    // Sealed class, con una lista, para tener los onboardingPage

    // Sistema de circulos,o barra de progresion, donde muestre por donde vas, esto es otra clase (HorizontalPagerIndicator)

    // Botones: atras, siguiente, finalizar, y omitir
    // Crear sealed class con los botones para luego pasarselo al viewModel y tener una funcion en vez de 4, solo se pasaria una funcion

    // Pantalla vertical:
    // Boton omitir arriba derecha, abajo, el onboardingPage, abajo de este, el sistema de circulos, y abajo del todo
    // Los botones, atras, siguiente, y finalizar, cual corresponda,

    // Pantalla horizontal:
    // Onboarding Page, en la parte izquierda de la pantalla, abajo, los puntos, boton omitir, a la derecha arriba
    // Los botones, atras, siguiente, y finalizar, cual corresponda,  en la parte de abajo, pegado a la izquierda

    val orientation = getOrientation()

    when(orientation){
        Orientation.Portrait -> {
            VerticalOnboardingScreen(
                pageSelected = pageSelected,
                onClickResultOption = onClickResultOption
            )
        }
        Orientation.Landscape -> {

        }
    }

}