package com.mean.traclock.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mean.traclock.R

//@Composable
//fun SmallOutlinedButton(text:String,onClick:()->Unit){
//    OutlinedButton(
//        onClick = onClick,
//        modifier = Modifier.height(28.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = Icons.Default.PlayArrow,
//                contentDescription = stringResource(R.string.start),
////                modifier = Modifier.size(16.dp)
//            )
//            Text(
//                text,
////                style = MaterialTheme.typography.labelMedium
//            )
//        }
//    }
//}

@Composable
fun SmallOutlinedButton(text: String, onClick: () -> Unit = {}) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            //边框颜色
            color = MaterialTheme.colorScheme.outline.copy(0.2f)
        ),
        onClick = onClick,
        //图标和文字颜色
        contentColor = MaterialTheme.colorScheme.secondary.copy(0.8f),
        //按钮背景颜色
        color = MaterialTheme.colorScheme.inverseOnSurface.copy(0.6f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.start),
                modifier = Modifier
                    .size(24.dp)
                    .padding(vertical = 4.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 6.dp)
            )
        }
    }
}