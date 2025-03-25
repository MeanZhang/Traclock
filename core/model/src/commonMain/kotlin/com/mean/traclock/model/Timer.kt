package com.mean.traclock.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class Timer(val projectId: Long, val startTime: Instant = Clock.System.now()) {
    enum class State {
        STOPPED,
        RUNNING,
    }

    private var _stopTime: Instant? = null
    private var _state = State.RUNNING

    val stopTime: Instant?
        get() = _stopTime

    val state: State
        get() = _state

    val isRunning: Boolean
        get() = _state == State.RUNNING

    fun start(): Timer =
        if (_state == State.RUNNING) {
            this
        } else {
            Timer(projectId)
        }

    fun stop(stopTime: Instant = Clock.System.now()) {
        _stopTime = stopTime
        _state = State.STOPPED
    }

    override fun toString(): String {
        return "Timer(projectId=$projectId, startTime=$startTime, stopTime=$stopTime, state=$_state)"
    }
}
