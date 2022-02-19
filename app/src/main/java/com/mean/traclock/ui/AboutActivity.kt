package com.mean.traclock.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.BuildConfig
import com.mean.traclock.R
import com.mean.traclock.App
import com.mean.traclock.ui.theme.TraclockTheme

class AboutActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraclockTheme {
                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                Scaffold(topBar = {
                    SmallTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                            }
                        },
                        title = { Text(stringResource(R.string.about)) },
                        scrollBehavior = scrollBehavior
                    )
                }) {
                    Content()
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun Content() {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Image(
            painterResource(R.drawable.ic_launcher_foreground),
            stringResource(R.string.app_name),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(120.dp)
                .fillMaxWidth()
        )
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stringResource(R.string.version) + BuildConfig.VERSION_NAME,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Divider(Modifier.padding(vertical = 16.dp))
        Text(
            "开发者",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = App.horizontalMargin)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(R.drawable.avatar),
                stringResource(R.string.developer_avatar),
                modifier = Modifier
                    .padding(App.horizontalMargin, 12.dp)
                    .height(46.dp)
                    .clip(CircleShape)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Mean",style = MaterialTheme.typography.titleMedium,)
                Text(
                    stringResource(R.string.developer_introduction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}