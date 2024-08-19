package com.mean.traclock.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import co.touchlab.kermit.Logger
import com.mean.traclock.App
import com.mean.traclock.BuildConfig
import com.mean.traclock.MainActivity
import com.mean.traclock.data.repository.TimerRepository
import kotlinx.coroutines.runBlocking

/** 时器通知服务 */
class TimerService : Service() {
    private val timerRepo: TimerRepository = App.timerRepo

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int,
    ): Int {
        when (intent.action) {
            ACTION_SHOW_APP -> {
                val showApp: Intent =
                    Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(showApp)
            }
            // 开始
            ACTION_START_TIMER -> {
                Logger.d("开始计时")
                runBlocking { timerRepo.start() }
            }
            // 停止
            ACTION_STOP_TIMER -> {
                Logger.d("停止计时")
                runBlocking { timerRepo.stop() }
            }
        }

        return START_NOT_STICKY
    }

    companion object {
        private const val ACTION_PREFIX = BuildConfig.APPLICATION_ID + ".action."

        const val ACTION_SHOW_APP = ACTION_PREFIX + "SHOW_APP"
        const val ACTION_START_TIMER = ACTION_PREFIX + "START_TIMER"
        const val ACTION_STOP_TIMER = ACTION_PREFIX + "STOP_TIMER"
    }
}
