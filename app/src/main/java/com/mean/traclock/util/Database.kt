package com.mean.traclock.util

import com.mean.traclock.TraclockApplication
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import kotlin.concurrent.thread

object Database {
    fun deleteRecord(record: Record) {
        thread {
            AppDatabase.getDatabase(TraclockApplication.context).recordDao()
                .delete(record)
        }
    }

    fun deleteProject(project: Project) {
        thread {
            AppDatabase.getDatabase(TraclockApplication.context).projectDao()
                .delete(project)
            AppDatabase.getDatabase(TraclockApplication.context).recordDao().deleteByProject(project.name)
        }
    }
}