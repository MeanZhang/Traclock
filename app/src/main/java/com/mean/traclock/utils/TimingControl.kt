package com.mean.traclock.utils

import android.content.Context
import android.widget.Toast
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mean.traclock.App
import com.mean.traclock.App.Companion.context
import com.mean.traclock.R
import com.mean.traclock.database.Record
import kotlinx.coroutines.DelicateCoroutinesApi

object TimingControl {
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    @DelicateCoroutinesApi
    fun startRecord(project: String) {
        if (App.isTiming.value) {
            Toast.makeText(context, context.getString(R.string.is_tracking), Toast.LENGTH_SHORT)
                .show()
        } else {
            App.isTiming.value = true
            val startTime = System.currentTimeMillis()
            with(sharedPref.edit()) {
                putBoolean("isTiming", true)
                putString("projectName", project)
                putLong("startTime", startTime)
                apply()
            }
            startNotify(project)
        }
    }

    fun stopRecord() {
        if (App.isTiming.value) {
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
            Database.insertRecord(record)
        }
    }

    fun startNotify(projectName: String? = null, startTime: Long? = null) {
        val data =
            Data.Builder().putString("projectName", projectName ?: getProjectName())
                .putLong("startTime", startTime ?: getStartTime())
                .build()
        val request =
            OneTimeWorkRequest.Builder(NotificationWorker::class.java).setInputData(data)
                .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                context.getString(R.string.notification_work_name),
                ExistingWorkPolicy.KEEP,
                request
            )
    }

    fun getIsTiming() = sharedPref.getBoolean("isTiming", false)

    fun getProjectName() = sharedPref.getString("projectName", "") ?: ""

    fun getStartTime() = sharedPref.getLong("startTime", System.currentTimeMillis())
}
