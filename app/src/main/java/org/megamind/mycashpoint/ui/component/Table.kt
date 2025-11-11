@file:Suppress("FunctionName")

package org.megamind.mycashpoint.ui.component


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Reusable dynamic table with header and rows.
 * Supports both horizontal & vertical scrolling and dynamic column width.
 */
@Composable
fun <T> Table(
    modifier: Modifier = Modifier,
    items: List<T>,
    columnCount: Int,
    headers: List<String>,

    headerStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
    headerBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    verticalLazyListState: LazyListState = rememberLazyListState(),
    horizontalScrollState: ScrollState = rememberScrollState(),
    cellContent: @Composable (columnIndex: Int, rowIndex: Int, item: T) -> Unit,
) {
    val columnWidths = remember { mutableStateMapOf<Int, Int>() }

    Box(
        modifier = modifier
            .horizontalScroll(horizontalScrollState)
            .border(1.dp, borderColor)
    ) {
        Column {
            // HEADER ROW
            Row(Modifier.background(headerBackgroundColor)) {
                headers.forEachIndexed { columnIndex, header ->
                    TableCellDynamicWidth(
                        text = header,
                        columnIndex = columnIndex,
                        columnWidths = columnWidths,
                        style = headerStyle,
                        backgroundColor = headerBackgroundColor,
                        borderColor = borderColor
                    )
                }
            }

            // DATA ROWS
            LazyColumn(state = verticalLazyListState) {
                itemsIndexed(items) { rowIndex, item ->
                    Row {
                        (0 until columnCount).forEach { columnIndex ->
                            TableCellDynamicWidth(
                                columnIndex = columnIndex,
                                columnWidths = columnWidths,
                                borderColor = borderColor
                            ) { modifier ->
                                cellContent(columnIndex, rowIndex, item)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Table cell that automatically adjusts its width
 */
@Composable
private fun TableCellDynamicWidth(
    text: String? = null,
    columnIndex: Int,
    columnWidths: MutableMap<Int, Int>,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    onClick: (() -> Unit)? = null,
    innerPadding: Dp = 4.dp,
    content: (@Composable (Modifier) -> Unit)? = null
) {
    Box(
        modifier = Modifier

            .border(0.5.dp, borderColor)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val existingWidth = columnWidths[columnIndex] ?: 0
                val maxWidth = max(existingWidth, placeable.width)
                if (maxWidth > existingWidth) {
                    columnWidths[columnIndex] = maxWidth
                }
                layout(width = maxWidth, height = placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }
            .background(backgroundColor)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = innerPadding)
    ) {
        if (content != null) {
            content(Modifier.padding(8.dp))
        } else if (text != null) {
            Text(

                text = text,
                style = style,
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
