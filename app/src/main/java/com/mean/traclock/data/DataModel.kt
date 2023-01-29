package com.mean.traclock.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.annotation.Keep
import com.mean.traclock.R
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.utils.Utils
import kotlinx.coroutines.flow.StateFlow

/**
 * 所有应用程序范围的数据都可以通过该单例访问。
 */
class DataModel private constructor() {
    private var mContext: Context? = null

    private var mDatabaseModel: DatabaseModel? = null

    /** 获取计时器数据的模型 */
    private var mTimerModel: TimerModel? = null

    /** 获取通知数据的模型 */
    private var mNotificationModel: NotificationModel? = null

    /**
     * 初始化数据模型、[Context] 和 [SharedPreferences]
     */
    fun init(context: Context, prefs: SharedPreferences) {
        if (mContext !== context) {
            mContext = context.applicationContext
            mNotificationModel = NotificationModel()
            mDatabaseModel = DatabaseModel(mContext!!)
            mTimerModel = TimerModel(mContext!!, prefs, mNotificationModel!!)
        }
    }

    /**
     * 更新通知
     */
    fun updateNotification() {
        Utils.enforceMainLooper()
        mTimerModel!!.updateNotification()
    }

    /** 所有项目 */
    val projects: Map<String, Int>
        get() = mDatabaseModel!!.projects

    /** 当前的项目名称 */
    val projectName: StateFlow<String>
        get() {
            Utils.enforceMainLooper()
            return mTimerModel!!.projectName
        }

    /** 计时器是否在运行 */
    val isRunning: StateFlow<Boolean>
        get() {
            Utils.enforceMainLooper()
            return mTimerModel!!.isRunning
        }

    /** 计时器开始的时间，以毫秒为单位 */
    val startTime: StateFlow<Long>
        get() {
            Utils.enforceMainLooper()
            return mTimerModel!!.startTime
        }

    /** 开始计时 */
    fun startTimer(projectName: String? = null) {
        Utils.enforceMainLooper()
        if (isRunning.value) {
            Toast.makeText(
                mContext,
                mContext!!.getText(R.string.is_running_description),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            mTimerModel!!.start(projectName)
        }
    }

    /** 停止计时 */
    fun stopTimer() {
        Utils.enforceMainLooper()
        mTimerModel!!.stop()
    }

    //
    // 数据库
    //

    /**
     * 增加记录
     * @param record 要增加的记录
     * @return 是否插入成功
     */
    fun insertRecord(record: Record) = mDatabaseModel!!.insertRecord(record)

    /**
     * 删除记录
     * @param record 要删除的记录
     */
    fun deleteRecord(record: Record) = mDatabaseModel!!.deleteRecord(record)

    /**
     * 删除项目（包括该项目的所有记录）
     * @param project 要删除的项目
     */
    fun deleteProject(project: Project) = mDatabaseModel!!.deleteProject(project)

    /**
     * 增加项目
     * @param project 要增加的项目
     */
    fun insertProject(project: Project) = mDatabaseModel!!.insertProject(project)

    /**
     * 更新项目（同时更新该项目的所有记录）
     * @param oldProjectName 旧项目名
     * @param newProject 新项目
     * @return 是否更新成功
     */
    fun updateProject(oldProjectName: String, newProject: Project) =
        mDatabaseModel!!.updateProject(oldProjectName, newProject)

    /**
     * 更新项目颜色
     * @param project 要更新的项目
     */
    fun updateProject(project: Project) = mDatabaseModel!!.updateProject(project)

    /**
     * 更新记录
     * @param record 要更新的记录
     * @return 是否更新成功
     */
    fun updateRecord(record: Record) = mDatabaseModel!!.updateRecord(record)

    fun getProjectsTimeOfDay(date: Int) = mDatabaseModel!!.getProjectsTimeOfDay(date)
    fun getRecordsOfDays() = mDatabaseModel!!.getRecordsOfDays()
    fun getProjectsTimeOfDays() = mDatabaseModel!!.getProjectsTimeOfDays()
    fun getTimeOfDays() = mDatabaseModel!!.getTimeOfDays()
    fun getProjectsTime() = mDatabaseModel!!.getProjectsTime()
    fun getRecordsOfDays(projectName: String) = mDatabaseModel!!.getRecordsOfDays(projectName)

    fun getTimeOfDays(projectName: String) = mDatabaseModel!!.getTimeOfDays(projectName)

    companion object {
        @SuppressLint("StaticFieldLeak")
        val sDataModel = DataModel()

        @get:JvmStatic
        @get:Keep
        val dataModel
            get() = sDataModel
    }
}
