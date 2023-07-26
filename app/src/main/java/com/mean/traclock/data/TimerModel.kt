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
    private var mProjectId: MutableStateFlow<Int?> = MutableStateFlow(null)
    private var mIsRunning: MutableStateFlow<Boolean>? = null
    private var mStartTime: MutableStateFlow<Long>? = null
    private var mStoppedTime: Long? = null

    private val mutex = Mutex()

    /** 当前的项目 id */
    val projectId: StateFlow<Int?>
        get() {
            if (mProjectId.value == null) {
                mProjectId.value = runBlocking { TimerDAO.projectIdFlow.first() }
            }
            return mProjectId
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
        projectId: Int,
        isRunning: Boolean = true,
        startTime: Long = TimeUtils.now(),
        stoppedTime: Long? = null,
    ) {
        mutex.withLock {
            if (mProjectId.value == null) {
                mProjectId.value = projectId
            }
            mProjectId.value = projectId
            mIsRunning!!.value = isRunning
            mStartTime!!.value = startTime
            mStoppedTime = stoppedTime
            TimerDAO.setTimer(
                projectId,
                isRunning,
                startTime,
            )
            if (!isRunning) {
                DataModel.dataModel.insertRecord(Record(projectId, startTime, stoppedTime!!))
            }
            updateNotification()
        }
    }

    /** 更新计时通知 */
    fun updateNotification() {
        if (projectId.value != null) {
            val notification: Notification =
                mNotificationBuilder.build(
                    mContext,
                    DataModel.dataModel.projects[projectId.value]?.name ?: "",
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
    }

    /**
     * 开始计时
     * @param projectId 计时的项目 Id
     */
    suspend fun start(projectId: Int? = null) {
        setTimer(
            projectId ?: mProjectId.value!!,
            true,
            TimeUtils.now(),
        )
    }

    /** 停止计时 */
    suspend fun stop() {
        if (isRunning.value) {
            setTimer(
                projectId.value!!,
                false,
                startTime.value,
                TimeUtils.now(),
            )
        }
    }
}
