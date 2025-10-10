package org.megamind.mycashpoint.ui.component


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlin.text.all
import kotlin.text.isDigit
import kotlin.text.isEmpty


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String = "",
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    label: String = "",
    enabled: Boolean = true,
    supportText: String = "Ne peut pas être vide",
    isError: Boolean = false,
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next
) {


    Column() {
        Text(
            modifier = Modifier.padding(start = 22.dp),
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            modifier = modifier
                .height(68.dp),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            isError = isError,
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.bodySmall,
            supportingText = {
                AnimatedVisibility(
                    visible = isError,
                    enter = slideInHorizontally(animationSpec = tween(easing = LinearOutSlowInEasing)),
                    exit = slideOutHorizontally(animationSpec = tween(easing = LinearOutSlowInEasing))
                ) {
                    Text(
                        text = supportText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            placeholder = {
                Text(
                    placeHolder,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = contentColor,
                focusedTextColor = contentColor,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                errorContainerColor = containerColor,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = containerColor,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = contentColor,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,

                ),
            shape = RoundedCornerShape(100),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(imeAction = imeAction)


        )
    }


}


@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLines: Int = 1,
    placeHolder: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    supportText: String = "Ne peut pas être vide",
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth(0.8f)) {
        Text(
            modifier = Modifier,
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clip(RoundedCornerShape(12.dp)) // Coins arrondis
                .background(containerColor)
                .border(
                    BorderStroke(
                        0.5.dp,

                        if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primaryContainer
                    ),
                    RoundedCornerShape(12.dp)
                )

                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(Modifier.width(8.dp))
                }

                BasicTextField(
                    modifier = Modifier
                        .weight(1f),
                    value = value,
                    onValueChange = {
                        if (keyboardType == KeyboardType.Number) {
                            if (it.all { it.isDigit() }) {

                                onValueChange(it)
                            }
                            else{
                                isError
                            }
                        } else {
                            onValueChange(it)
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(color = contentColor),
                    singleLine = maxLines == 1,
                    maxLines = maxLines,
                    enabled = enabled,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = imeAction
                    ),
                    keyboardActions = keyboardActions,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(placeHolder, color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                if (trailingIcon != null) {
                    Spacer(Modifier.width(8.dp))
                    trailingIcon()
                }
            }
        }

        AnimatedVisibility(visible = isError) {
            Text(
                text = supportText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}