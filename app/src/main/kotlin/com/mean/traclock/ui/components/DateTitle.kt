package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.utils.Config.HORIZONTAL_MARGIN
import com.mean.traclock.utils.getDurationString

@Composable
fun DateTitle(date: String, duration: Long) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = HORIZONTAL_MARGIN),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                date,
                color = MaterialTheme.colorScheme.primary
//            style = MaterialTheme.typography.headlineSmall
            )
            Text(
                getDurationString(duration),
                color = MaterialTheme.colorScheme.primary
//            style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DateTitlePreview() {
    Scaffold(topBar = { SmallTopAppBar(title = { Text(text = "DateTitle") }) }) {
        LazyColumn(contentPadding = it) {
            item {
                DateTitle(date = "今天", duration = 80)
            }
        }
    }
}
