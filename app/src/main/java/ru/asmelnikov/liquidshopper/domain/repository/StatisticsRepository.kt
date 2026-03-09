package ru.asmelnikov.liquidshopper.domain.repository

import ru.asmelnikov.liquidshopper.domain.models.Statistic
import java.time.LocalDate

interface StatisticsRepository {

    suspend fun getPeriodStatistics(start: LocalDate, end: LocalDate): Statistic

}