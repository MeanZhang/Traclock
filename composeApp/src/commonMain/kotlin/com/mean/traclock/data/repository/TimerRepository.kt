package com.mean.traclock.data.repository

import co.touchlab.kermit.Logger
import com.mean.traclock.model.Record
import com.mean.traclock.model.Timer
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant

class TimerRepository(
    private val notificationRepo: NotificationRepository,
    private val projectsRepo: ProjectsRepository,
    private val recordsRepo: RecordsRepository,
    private val datastoreRepo: DatastoreRepository,
) {
    private var initialized = false

    private var timer: Timer? = null

    private val mutex = Mutex()

    /** 计时器是否正在运行 */
    private val _isTiming = MutableStateFlow(false)
    val isTiming: StateFlow<Boolean>
        get() = _isTiming

    /** 当前的项目名 */
    private var _projectName: MutableStateFlow<String?> = MutableStateFlow(null)
    val projectName: MutableStateFlow<String?>
        get() = _projectName

    /** 计时器开始的时间，以毫秒为单位 */
    val startTime: Instant?
        get() = timer?.startTime

    suspend fun init() {
        timer = datastoreRepo.timer.first()
        if (timer != null) {
            _projectName.value = projectsRepo.get(timer!!.projectId).name
        }
        _isTiming.value = timer?.isRunning ?: false
        if (isTiming.value) {
            Logger.d("恢复计时器: $timer")
        }
        initialized = true
    }

    /** 更新计时通知 */
    suspend fun updateNotification() {
        if (!initialized) {
            init()
        }
        if (timer != null) {
            Logger.d("更新计时通知")
            notificationRepo.notify(
                _projectName.value ?: "",
                timer!!.isRunning,
                startTime!!,
            )
        }
    }

    /**
     * 开始计时
     * @param projectId 计时的项目 Id
     */
    suspend fun start(projectId: Long? = null) {
        mutex.withLock {
            Logger.d("开始计时: $projectId")
            _isTiming.value = true
            timer =
                if (timer?.isRunning == true) {
                    return
                } else if (projectId == null) {
                    timer!!.start()
                } else {
                    Timer(projectId)
                }
            _projectName.value = projectsRepo.get(timer!!.projectId).name
            updateNotification()
            datastoreRepo.saveTimer(timer!!)
        }
    }

    /** 停止计时 */
    suspend fun stop() {
        mutex.withLock {
            Logger.d("停止计时")
            _isTiming.value = false
            if (timer?.isRunning == true) {
                timer!!.stop()
                recordsRepo.insert(
                    Record(
                        timer!!.projectId,
                        startTime!!,
                        TimeUtils.now(),
                    ),
                )
                datastoreRepo.stopTimer(timer!!)
            }
            updateNotification()
        }
    }
}
