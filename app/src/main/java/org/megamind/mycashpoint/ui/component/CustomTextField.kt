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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
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
                            } else {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    placeholder: String? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    showClearButton: Boolean = true,                     // bouton "X" pour effacer
    isError: Boolean = false,
    errorMessage: String? = null,
    maxChars: Int? = null,                               // compteur facultatif
    maxLines: Int = 1,
    singleLine: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    textSize: TextUnit? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    supportingText: (@Composable (() -> Unit))? = null,  // zone sous le champ (ex : aide)
    shape: androidx.compose.foundation.shape.CornerBasedShape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledIndicatorColor = MaterialTheme.colorScheme.onSurface


    )
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            enabled = enabled,
            value = value,
            onValueChange = {
                val new = if (maxChars != null) it.take(maxChars) else it
                onValueChange(new)
            },
            modifier = Modifier.fillMaxWidth(),
            label = label?.let { { Text(it, fontWeight = FontWeight.Bold) } },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon,
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // custom trailing icon si fourni
                    if (trailingIcon != null) {
                        trailingIcon()
                        Spacer(Modifier.width(8.dp))
                    }

                    // bouton clear automatique
                    if (showClearButton && value.isNotEmpty()) {
                        IconButton(onClick = { onValueChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            },
            isError = isError,
            maxLines = maxLines,
            singleLine = singleLine,
            textStyle = textStyle,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            shape = shape,
            colors = colors
        )

        AnimatedVisibility(isError) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

