package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.data.Record
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import com.mean.traclock.ui.screens.home.Period
import com.mean.traclock.ui.screens.home.PeriodType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StatisticViewModel(
    private val recordsRepo: RecordsRepository,
    projectsRepo: ProjectsRepository,
    private val timerRepo: TimerRepository,
) : ViewModel() {
    val projects = projectsRepo.projects

    val startTime: Long?
        get() = timerRepo.startTime

    fun getProjectsTimeOfPeriod(period: Period): Flow<List<Record>> {
        if (period.type == PeriodType.ALL_TIME) {
            return recordsRepo.getProjectsTime().map { it.filter { project -> project.endTime > 0 } }
        }
        return recordsRepo.getProjectsTimeOfPeriod(period.startDate, period.endDate)
    }

    fun getRecordsNumber(period: Period): Flow<Int> {
        if (period.type == PeriodType.ALL_TIME) {
            return recordsRepo.getAllRecordsNumber()
        }
        return recordsRepo.getRecordsNumber(period.startDate, period.endDate)
    }
}
