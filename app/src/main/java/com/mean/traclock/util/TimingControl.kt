package com.mean.traclock.util

import android.content.Context
import android.widget.Toast
import com.mean.traclock.App
import com.mean.traclock.App.Companion.context
import com.mean.traclock.R
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Record
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.concurrent.thread


object TimingControl {
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    @DelicateCoroutinesApi
    fun startRecord(project: String) {
        if (App.isTiming.value == true) {
            Toast.makeText(context, context.getString(R.string.is_tracking), Toast.LENGTH_SHORT)
                .show()
        } else {
            App.isTiming.value = true
            with(sharedPref.edit()) {
                putBoolean("isTiming", true)
                putString("project", project)
                putLong("startTime", System.currentTimeMillis())
                apply()
            }
            startNotify(project, System.currentTimeMillis())
        }
    }

    fun stopRecord() {
        if (App.isTiming.value == true) {
            App.isTiming.value = false
            with(sharedPref.edit()) {
                putBoolean("isTiming", false)
                apply()
            }
            val record = Record(
                getProjectName(),
                getStartTime(),
                System.currentTimeMillis()
            )
            thread {
                AppDatabase.getDatabase(context).recordDao().insert(record)
            }
            stopNotify(record.project, record.startTime)
        }
    }

    fun getIsTiming() = sharedPref.getBoolean("isTiming", false)

    fun getProjectName() = sharedPref.getString("project", "") ?: ""

    fun getStartTime() = sharedPref.getLong("startTime", 0L)
}