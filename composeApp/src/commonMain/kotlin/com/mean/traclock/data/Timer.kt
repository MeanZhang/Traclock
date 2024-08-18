package com.mean.traclock.data

import com.mean.traclock.utils.TimeUtils

class Timer(val projectId: Long, val startTime: Long = TimeUtils.now()) {
    enum class State {
        STOPPED,
        RUNNING,
    }

    private var _stopTime: Long? = null
    private var _state = State.RUNNING

    val stopTime: Long?
        get() = _stopTime

    val state: State
        get() = _state

    val isRunning: Boolean
        get() = _state == State.RUNNING

    fun start() =
        if (_state == State.RUNNING) {
            this
        } else {
            Timer(projectId)
        }

    fun stop(stopTime: Long = TimeUtils.now()) {
        _stopTime = stopTime
        _state = State.STOPPED
    }

    override fun toString(): String {
        return "Timer(projectId=$projectId, startTime=${
            TimeUtils.getDateTimeString(
                startTime,
            )
        }, stopTime=${stopTime?.let { TimeUtils.getDateTimeString(it) }}, state=$_state)"
    }
}
