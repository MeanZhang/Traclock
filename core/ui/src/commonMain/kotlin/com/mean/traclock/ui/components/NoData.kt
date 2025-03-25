package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NoData(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LottieWorkingLoadingView()
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 128.dp),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun LottieWorkingLoadingView(modifier: Modifier = Modifier) {
//    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("nodata.json"))
//    LottieAnimation(
//        composition,
//        modifier =
//            modifier
//                .fillMaxWidth()
//                .height(250.dp)
//                .defaultMinSize(300.dp),
//        iterations = LottieConstants.IterateForever,
//    )
}
