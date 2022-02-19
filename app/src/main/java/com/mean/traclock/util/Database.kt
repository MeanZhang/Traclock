package com.mean.traclock.util

import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import kotlin.concurrent.thread

object Database {
    fun deleteRecord(record: Record) {
        thread {
            AppDatabase.getDatabase(App.context).recordDao()
                .delete(record)
        }
    }

    fun deleteProject(project: Project) {
        thread {
            AppDatabase.getDatabase(App.context).projectDao()
                .delete(project)
            AppDatabase.getDatabase(App.context).recordDao().deleteByProject(project.name)
        }
    }
}