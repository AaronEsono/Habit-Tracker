package aeb.proyecto.addhabit.components.common.bottomSheet

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.model.DataBottomSheet
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBottomSheet(
    modifier: Modifier = Modifier,
    dataBottomSheet: DataBottomSheet,
    color:Color,
    contrastColor:Color,
    onDismiss: () -> Unit = {},
    onAccept: () -> Unit = {}
){

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    CustomBottomSheet (
        modifier = modifier,
        onDismiss = onDismiss,
        sheetState = sheetState
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing8, end = spacing8, bottom = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    dataBottomSheet.icon,
                    contentDescription = "Icon Title",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(spacing20)
                )

                TitleLargeText(
                    stringResource(dataBottomSheet.title),
                    modifier = Modifier.padding(horizontal = spacing3))
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            LabelLargeText(
                stringResource(dataBottomSheet.subtitle),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacing8)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing8, top = spacing12),
            ){

                when(dataBottomSheet){
                    DataBottomSheet.DELETE_NOTIFICATION -> {
                        CustomRipple {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        onDismiss()
                                    }
                                },
                                shape = RoundedCornerShape(spacing8),
                                modifier = Modifier.weight(1f)
                            ) {
                                LabelLargeText(stringResource(R.string.add_habit_cancel))
                            }
                        }

                        Spacer(modifier = Modifier.padding(horizontal = spacing8))
                    }
                    DataBottomSheet.ERROR_NAME_UNIT,DataBottomSheet.ERROR_HOUR, DataBottomSheet.ERROR_INTERVAL_UNIT, DataBottomSheet.GENERAL_ERROR -> Unit
                }

                CustomRipple {
                    Button(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(spacing8),
                        onClick = {
                            onAccept()
                            scope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color
                        )
                    ) {
                        LabelLargeText(stringResource(R.string.add_habit_accept),
                            color = contrastColor)
                    }
                }
            }
        }
    }
}