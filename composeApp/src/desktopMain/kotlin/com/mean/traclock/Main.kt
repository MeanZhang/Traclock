package com.mean.traclock

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import com.mean.traclock.di.appModule
import com.mean.traclock.di.getLogger
import com.mean.traclock.timer.DesktopNotifier
import com.mean.traclock.timer.Notifier
import com.mean.traclock.timer.TimerRepository
import com.mean.traclock.ui.TraclockApp
import com.mean.traclock.utils.initLogger
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

fun main() =
    application {
        remember { initLogger() }
        remember {
            startKoin {
                logger(getLogger())
                modules(appModule)
            }
        }
        val scope = rememberCoroutineScope()
        val trayState = rememberTrayState()
        val notifier: DesktopNotifier by inject(Notifier::class.java)
        notifier.init(trayState)
        val timerRepo: TimerRepository by inject(TimerRepository::class.java)

        var isVisible by remember { mutableStateOf(true) }
        val isTiming by timerRepo.isTiming.collectAsState()

        Window(
            onCloseRequest = { isVisible = false },
            visible = isVisible,
            title = stringResource(CommonRes.strings.app_name),
            icon = painterResource(CommonRes.images.ic_logo),
        ) {
            KoinContext {
                MaterialTheme {
                    TraclockApp()
                }
            }
        }
        Tray(
            state = trayState,
            icon = painterResource(CommonRes.images.ic_logo),
            tooltip = stringResource(CommonRes.strings.app_name),
            onAction = { isVisible = true },
            menu = {
                Item(
                    text = stringResource(CommonRes.strings.start),
                    enabled = !isTiming,
                    onClick = { scope.launch { timerRepo.start() } },
                )
                Item(
                    stringResource(CommonRes.strings.stop),
                    enabled = isTiming,
                    onClick = { scope.launch { timerRepo.stop() } },
                )
                Item(stringResource(CommonRes.strings.exit), onClick = ::exitApplication)
            },
        )
    }
