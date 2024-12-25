package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.ui.theme.DarKThemeText
import aeb.proyecto.habittracker.ui.theme.colorButtons
import aeb.proyecto.habittracker.utils.Constans.ENDBUTTON
import aeb.proyecto.habittracker.utils.Constans.STARTBUTTON
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.graphics.drawable.shapes.Shape
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CardOptionButton(
    modifier: Modifier = Modifier,
    @StringRes title:Int,
    @DrawableRes icon:Int,
    border:Int,
    onClick: () -> Unit,
) {

    var shape = RectangleShape

    if (border == STARTBUTTON) {
        shape = RoundedCornerShape(topStart = spacing8, topEnd = spacing8)
    }else if ( border == ENDBUTTON){
        shape = RoundedCornerShape(bottomStart = spacing8, bottomEnd = spacing8)
    }

    Card(
        shape = shape,
        modifier = modifier
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = spacing8
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorButtons
        )
    ) {
        Row (modifier = Modifier.fillMaxWidth().padding(spacing12),
            verticalAlignment = Alignment.CenterVertically){

            Icon(painter = painterResource(id = icon), contentDescription = "", modifier = Modifier.size(25.dp), tint = DarKThemeText)

            LabelMediumText(text = stringResource(title), modifier = Modifier.padding(start = spacing12).weight(1f)
                ,textAlign = TextAlign.Start)

            Icon(Icons.AutoMirrored.Filled.ArrowRight,"", tint = DarKThemeText)

        }
    }
}