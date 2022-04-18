package com.mean.traclock.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.R

// @Composable
// fun SmallOutlinedButton(text: String, onClick: () -> Unit) {
//    OutlinedButton(
//        onClick = onClick,
//        border = BorderStroke(
//            width = 1.dp,
//            // 边框颜色
//            color = MaterialTheme.colorScheme.outline.copy(0.2f)
//        ),
//        colors = ButtonDefaults.outlinedButtonColors(
//            contentColor = MaterialTheme.colorScheme.outline,
//            containerColor = MaterialTheme.colorScheme.inverseOnSurface
//        ),
//        modifier = Modifier.height(32.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.PlayArrow,
//                contentDescription = stringResource(R.string.start),
//                modifier = Modifier.size(16.dp)
//            )
//            Text(text)
//        }
//    }
// }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SmallOutlinedButton(text: String, onClick: () -> Unit = {}) {
    Chip(
        modifier = Modifier.height(28.dp),
        onClick = onClick,
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
            contentColor = MaterialTheme.colorScheme.outline.copy(0.2f),
            leadingIconContentColor = MaterialTheme.colorScheme.outline.copy(0.2f)
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.start),
                modifier = Modifier.padding(start = 6.dp).size(16.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        },
        border = BorderStroke(
            width = 1.dp,
            // 边框颜色
            color = MaterialTheme.colorScheme.outline.copy(0.2f)
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    SmallOutlinedButton(text = "00:00:00") {}
}
