package com.example.e_disiplin.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomListIcon(tint: Color = androidx.compose.material3.LocalContentColor.current, modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(24.dp)) {
        val strokeWidth = 2.dp.toPx()
        val cornerRadius = 6.dp.toPx()
        
        // Draw fill (light gray-blue)
        drawRoundRect(
            color = Color(0xFFE8EAF6),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius),
            topLeft = androidx.compose.ui.geometry.Offset(2.dp.toPx(), 2.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(20.dp.toPx(), 20.dp.toPx())
        )
        
        // Draw the rounded rectangle outline
        drawRoundRect(
            color = tint,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                join = androidx.compose.ui.graphics.StrokeJoin.Round
            ),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius),
            topLeft = androidx.compose.ui.geometry.Offset(2.dp.toPx(), 2.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(20.dp.toPx(), 20.dp.toPx())
        )
        
        // Draw lines
        // Top line
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(7.dp.toPx(), 7.dp.toPx()),
            end = androidx.compose.ui.geometry.Offset(15.dp.toPx(), 7.dp.toPx()),
            strokeWidth = strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        // Middle line (longest)
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(7.dp.toPx(), 12.dp.toPx()),
            end = androidx.compose.ui.geometry.Offset(17.dp.toPx(), 12.dp.toPx()),
            strokeWidth = strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        // Bottom line (shortest)
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(7.dp.toPx(), 17.dp.toPx()),
            end = androidx.compose.ui.geometry.Offset(12.dp.toPx(), 17.dp.toPx()),
            strokeWidth = strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}
