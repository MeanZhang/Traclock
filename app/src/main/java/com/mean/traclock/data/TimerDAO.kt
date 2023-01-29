package com.mean.traclock.data

import android.content.SharedPreferences

/**
 * 该类封装 [TimerModel] 在 [SharedPreferences] 中的持久化存储
 */
internal object TimerDAO {
    private const val IS_RUNNING = "is_running"
    private const val PROJECT_NAME = "project_name"
    private const val START_TIME = "start_time"

    /** @return 计时器的项目名称 */
    fun getProjectName(prefs: SharedPreferences) = prefs.getString(PROJECT_NAME, "") ?: ""

    /** @return 计时器是否正在运行 */
    fun getIsRunning(mPrefs: SharedPreferences) = mPrefs.getBoolean(IS_RUNNING, false)

    /** @return 计时器开始的时间，以毫秒为单位 */
    fun getStartTime(mPrefs: SharedPreferences) = mPrefs.getLong(START_TIME, 0)

    /**
     * @param prefs [SharedPreferences] 对象
     * @param projectName 项目名称
     * @param isRunning 计时器是否正在运行
     * @param startTime 计时器开始的时间，以毫秒为单位
     */
    fun setTimer(
        prefs: SharedPreferences,
        projectName: String,
        isRunning: Boolean,
        startTime: Long
    ) {
        val editor: SharedPreferences.Editor = prefs.edit()

        if (isRunning) {
            editor.putString(PROJECT_NAME, projectName)
                .putBoolean(IS_RUNNING, true)
                .putLong(START_TIME, startTime)
        } else {
            editor.remove(PROJECT_NAME)
                .remove(IS_RUNNING)
                .remove(START_TIME)
        }

        editor.apply()
    }
}
