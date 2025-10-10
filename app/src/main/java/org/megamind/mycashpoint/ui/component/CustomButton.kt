package org.megamind.mycashpoint.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


@Composable
fun CustomerButton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    contenairColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    enable: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit,


    ) {


    Button(
        enabled = enable,
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = contenairColor,
            contentColor = contentColor,
            disabledContainerColor = Color.Gray,
            disabledContentColor = contentColor

        ),
        border = BorderStroke(1.dp, color = borderColor)

    ) { content() }


}


@Composable
fun CustomerTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.tertiary.copy(.1f),
    contentColor: Color = MaterialTheme.colorScheme.tertiary,
    content: @Composable () -> Unit,

    ) {


    TextButton(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        content()
    }
}
