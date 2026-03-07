package ru.asmelnikov.liquidshopper.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.domain.models.Screens

interface ScreensBackgroundRepository {

    fun getAllScreensData(): Flow<List<Background>>

    fun getCurrentScreenData(screen: Screens): Flow<Background>

    suspend fun updateScreenData(data: Background)

}