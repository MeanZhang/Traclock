package com.mean.traclock.utils

import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import java.io.BufferedReader
import java.io.InputStreamReader

class RestoreWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val builder = NotificationCompat.Builder(
            App.context, App.context.getString(R.string.restore_channel_id)
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
//            .setOnlyAlertOnce(true)
            .setOngoing(true)
        val manager = NotificationManagerCompat.from(App.context)
        builder.setContentTitle(App.context.getString(R.string.restoring))
        manager.notify(RESTORE_NOTIFICATION_ID, builder.build())
        val uri = Uri.parse(inputData.getString("uri"))
        restore(uri)
        builder.setContentTitle(App.context.getString(R.string.restore_completed)).setOngoing(false)
        manager.notify(RESTORE_NOTIFICATION_ID, builder.build())
        return Result.success()
    }

    private fun restore(uri: Uri) {
        val contentResolver = App.context.contentResolver
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readLine() // 去掉第一行
                var line = reader.readLine()
                while (line != null) {
                    val res = restore(line)
                    if (res < 0) {
                        log(res.toString())
                        break
                    }
                    line = reader.readLine()
                }
            }
        }
    }

    private fun restore(line: String): Int {
        val columns = line.split(",")
        // 项目 -1 颜色
        // 项目 开始时间（2000-01-01 00:00:00） 结束时间
        if (columns.size < 3) {
            return -1 // 列数错误
        }
        val projectName = columns[0]
        if (projectName.isBlank()) {
            return -2 // 项目名为空
        }
        val startTime = try {
            columns[1].toLong()
        } catch (e: Exception) {
            log(e.toString())
            return -3
        }
        if (startTime == -1L) {
            val color = try {
                columns[2].toInt()
            } catch (e: Exception) {
                log(e.toString())
                return -3
            }
            Database.insertProject(Project(projectName, color))
        } else {
            val date = getIntDate(startTime)
            val endTime = try {
                columns[2].toLong()
            } catch (e: Exception) {
                return -4
            }
            val record = Record(projectName, startTime, endTime, date)
            if (!Database.insertRecord(record)) {
                return -4
            }
        }
        return 0
    }
}
