package aeb.proyecto.statistics.components.common.goalBox

import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelSmallText
import aeb.proyecto.ui.text.TitleLargeText
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GoalBoxStreak(
    modifier: Modifier = Modifier,
    title: String,
    dateString:String,
    subTitle: String
){

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(spacing6))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(spacing6))
            .background(MaterialTheme.colorScheme.surfaceTint)
            .fillMaxWidth()
    ) {

        val fontSizeTitle = (maxHeight.value * 0.35f).sp
        val fontSizeSubtitle = (maxHeight.value * 0.15f).sp
        val fontSizeDate = (maxHeight.value * 0.15f).sp

        Column(
            modifier = Modifier
                .padding(horizontal = spacing6, vertical = spacing3)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            TitleLargeText(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = fontSizeTitle,
                lineHeight = fontSizeTitle
            )

            LabelSmallText(
                text = subTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = fontSizeSubtitle,
                lineHeight = fontSizeSubtitle
            )

            Spacer(modifier = Modifier.padding(vertical = spacing1))

            LabelSmallText(
                text = dateString,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.scrim, // Color más sutil para el subtítulo
                fontSize = fontSizeDate,
                lineHeight = fontSizeDate
            )
        }
    }

}