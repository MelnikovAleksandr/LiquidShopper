package ru.asmelnikov.liquidshopper.data.repository

import ru.asmelnikov.liquidshopper.data.local.TasksDao
import ru.asmelnikov.liquidshopper.data.models.toStatistics
import ru.asmelnikov.liquidshopper.data.models.toTimestamp
import ru.asmelnikov.liquidshopper.domain.models.Statistic
import ru.asmelnikov.liquidshopper.domain.repository.StatisticsRepository
import java.time.LocalDate
import java.time.LocalTime

class StatisticsRepositoryImpl(
    private val tasksDao: TasksDao
) : StatisticsRepository {
    override suspend fun getPeriodStatistics(start: LocalDate, end: LocalDate): Statistic {
        val startDate = start.atStartOfDay().toTimestamp()
        val endDate = end.atTime(LocalTime.MAX).toTimestamp()
        return tasksDao.getTasksWithItemsInPeriod(
            startDate = startDate,
            endDate = endDate
        ).toStatistics()
    }
}