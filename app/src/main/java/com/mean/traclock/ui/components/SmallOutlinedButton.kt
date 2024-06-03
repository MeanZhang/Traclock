package com.mean.traclock.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.R

@Composable
fun SmallOutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ElevatedSuggestionChip(
        elevation =
            SuggestionChipDefaults.elevatedSuggestionChipElevation(
                elevation = 0.dp,
            ),
        modifier =
            modifier
                .height(28.dp)
                .width(IntrinsicSize.Max),
        onClick = onClick,
        shape = RoundedCornerShape(50),
        icon = {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.start),
                modifier =
                    Modifier
                        .padding(start = 6.dp)
                        .size(16.dp),
                tint = MaterialTheme.colorScheme.outline,
            )
        },
        border =
            BorderStroke(
                width = 1.dp,
                // 边框颜色
                color = MaterialTheme.colorScheme.outline.copy(0.2f),
            ),
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline,
            )
        },
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    SmallOutlinedButton(text = "00:00:00") {}
}
