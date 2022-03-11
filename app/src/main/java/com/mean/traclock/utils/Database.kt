package com.mean.traclock.utils

import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import kotlin.concurrent.thread

object Database {
    private val database = AppDatabase.getDatabase(App.context)
    fun deleteRecord(record: Record) {
        thread {
            database.recordDao().delete(record)
        }
    }

    fun deleteProject(project: Project) {
        thread {
            database.projectDao().delete(project)
            database.recordDao().deleteByProject(project.name)
        }
    }
}
