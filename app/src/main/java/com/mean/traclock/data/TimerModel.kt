package com.mean.traclock.data

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.mean.traclock.database.Record
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** 计时器数据的模型 */
internal class TimerModel(
    private val mContext: Context,
    private val mNotificationModel: NotificationModel,
) {
    private val mNotificationManager = NotificationManagerCompat.from(mContext)
    private val mNotificationBuilder = TimerNotificationBuilder()
    private var mProjectName: MutableStateFlow<String>? = null
    private var mIsRunning: MutableStateFlow<Boolean>? = null
    private var mStartTime: MutableStateFlow<Long>? = null
    private var mStoppedTime: Long? = null

    private val mutex = Mutex()

    /** 当前的项目名称 */
    val projectName: StateFlow<String>
        get() {
            if (mProjectName == null) {
                mProjectName = MutableStateFlow(runBlocking { TimerDAO.projectNameFlow.first() })
            }
            return mProjectName!!
        }

    /** 计时器是否正在运行 */
    val isRunning: StateFlow<Boolean>
        get() {
            if (mIsRunning == null) {
                mIsRunning = MutableStateFlow(runBlocking { TimerDAO.isRunningFlow.first() })
            }
            return mIsRunning!!
        }

    /** 计时器开始的时间，以毫秒为单位 */
    val startTime: StateFlow<Long>
        get() {
            if (mStartTime == null) {
                mStartTime = MutableStateFlow(runBlocking { TimerDAO.startTimeFlow.first() })
            }
            return mStartTime!!
        }

    private suspend fun setTimer(
        projectName: String,
        isRunning: Boolean = true,
        startTime: Long = TimeUtils.now(),
        stoppedTime: Long? = null,
    ) {
        mutex.withLock {
            if (mProjectName == null) {
                mProjectName = MutableStateFlow(projectName)
            }
            mProjectName!!.value = projectName
            mIsRunning!!.value = isRunning
            mStartTime!!.value = startTime
            mStoppedTime = stoppedTime
            TimerDAO.setTimer(
                projectName,
                isRunning,
                startTime,
            )
            if (!isRunning) {
                DataModel.dataModel.insertRecord(Record(projectName, startTime, stoppedTime!!))
            }
            updateNotification()
        }
    }

    /** 更新计时通知 */
    fun updateNotification() {
        val notification: Notification =
            mNotificationBuilder.build(
                mContext,
                projectName.value,
                isRunning.value,
                startTime.value,
            )
        mNotificationBuilder.buildChannel(mContext, mNotificationManager)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mNotificationManager.notify(mNotificationModel.timerNotificationId, notification)
    }

    /**
     * 开始计时
     * @param projectName 计时的项目名称
     */
    suspend fun start(projectName: String? = null) {
        setTimer(
            projectName ?: this.projectName.value,
            true,
            TimeUtils.now(),
        )
    }

    /** 停止计时 */
    suspend fun stop() {
        if (isRunning.value) {
            setTimer(
                projectName.value,
                false,
                startTime.value,
                TimeUtils.now(),
            )
        }
    }
}
