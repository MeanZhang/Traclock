package com.mean.traclock.statistic

import androidx.lifecycle.ViewModel
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordWithProjectRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.model.ProjectDuration
import com.mean.traclock.model.RecordWithProject
import com.mean.traclock.statistic.model.Period
import com.mean.traclock.statistic.model.PeriodType
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class StatisticViewModel(
    private val recordsRepo: RecordsRepository,
    private val recordWithProjectRepo: RecordWithProjectRepository,
    projectsRepo: ProjectsRepository,
) : ViewModel() {
    val projects = projectsRepo.projects

    fun getProjectsDuration(period: Period): Flow<List<ProjectDuration>> {
        if (period.type == PeriodType.ALL_TIME) {
            return recordsRepo.watchProjectsDuration()
                .map { it.filter { projectDuration -> projectDuration.duration > Duration.Companion.ZERO } }
        }
        return recordsRepo.watchProjectsDuration(period.startDate, period.endDate)
    }

    fun getRecordsNumber(period: Period): Flow<Int> {
        if (period.type == PeriodType.ALL_TIME) {
            return recordsRepo.watchRecordsCount()
        }
        return recordsRepo.watchRecordsCount(period.startDate, period.endDate)
    }

    fun getRecordsWithProject(date: LocalDate): Flow<List<RecordWithProject>> = recordWithProjectRepo.getRecordsWithProject(date)

    fun watchDaysDuration(period: Period): Flow<Map<LocalDate, Duration>> =
        recordsRepo.watchDaysDuration(period.startDate, period.endDate).map {
            it.mapKeys { (date, _) -> TimeUtils.getDate(date) }
                .mapValues { (_, duration) -> duration.toDuration(DurationUnit.MILLISECONDS) }
        }

    fun watchMonthsDuration(year: Int): Flow<Map<Int, Duration>> = recordsRepo.watchMonthsDuration(year)

    fun watchYearsDuration(): Flow<Map<Int, Duration>> = recordsRepo.watchYearsDuration()
}
