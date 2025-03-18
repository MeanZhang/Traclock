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
import com.mean.traclock.data.database.getAppDatabase
import com.mean.traclock.data.repository.DATA_STORE_FILE_NAME
import com.mean.traclock.data.repository.DatastoreRepository
import com.mean.traclock.data.repository.NotificationRepository
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordWithProjectRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import com.mean.traclock.data.repository.getDataStore
import com.mean.traclock.ui.TraclockApp
import com.mean.traclock.utils.initLogger
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.app_name
import traclock.composeapp.generated.resources.exit
import traclock.composeapp.generated.resources.logo
import traclock.composeapp.generated.resources.start
import traclock.composeapp.generated.resources.stop

fun main() =
    application {
        initLogger()
        val scope = rememberCoroutineScope()
        val database = getAppDatabase()
        val recordsRepo = RecordsRepository(database.recordDao())
        val recordWithProjectRepo = RecordWithProjectRepository(database.recordWithProjectDao())
        val projectsRepo = ProjectsRepository(database.projectDao(), database.recordDao())
        val dataStore =
            getDataStore { (System.getProperty("user.home").toPath() / ".traclock" / DATA_STORE_FILE_NAME).toString() }
        val datastoreRepo = DatastoreRepository(dataStore)
        val trayState = rememberTrayState()
        val notificationRepo = NotificationRepository()
        notificationRepo.init(trayState)
        val timerRepo =
            TimerRepository(
                notificationRepo = notificationRepo,
                projectsRepo = projectsRepo,
                recordsRepo = recordsRepo,
                datastoreRepo = datastoreRepo,
            )

        var isVisible by remember { mutableStateOf(true) }
        val isTiming by timerRepo.isTiming.collectAsState()

        Window(
            onCloseRequest = { isVisible = false },
            visible = isVisible,
            title = stringResource(Res.string.app_name),
            icon = painterResource(Res.drawable.logo),
        ) {
            MaterialTheme {
                TraclockApp(
                    recordsRepo = recordsRepo,
                    recordWithProjectRepo = recordWithProjectRepo,
                    projectsRepo = projectsRepo,
                    timerRepo = timerRepo,
                )
            }
        }
        Tray(
            state = trayState,
            icon = painterResource(Res.drawable.logo),
            tooltip = stringResource(Res.string.app_name),
            onAction = { isVisible = true },
            menu = {
                Item(
                    text = stringResource(Res.string.start),
                    enabled = !isTiming,
                    onClick = { scope.launch { timerRepo.start() } },
                )
                Item(
                    stringResource(Res.string.stop),
                    enabled = isTiming,
                    onClick = { scope.launch { timerRepo.stop() } },
                )
                Item(stringResource(Res.string.exit), onClick = ::exitApplication)
            },
        )
    }
