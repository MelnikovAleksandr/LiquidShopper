package ru.asmelnikov.liquidshopper.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.asmelnikov.liquidshopper.data.local.ScreensImagesDao
import ru.asmelnikov.liquidshopper.data.models.toBackground
import ru.asmelnikov.liquidshopper.data.models.toScreensBackEntity
import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.domain.models.Screens
import ru.asmelnikov.liquidshopper.domain.repository.ScreensBackgroundRepository

class ScreensBackgroundRepositoryImpl(
    private val screensDao: ScreensImagesDao
) : ScreensBackgroundRepository {
    override fun getAllScreensData(): Flow<List<Background>> {
        return screensDao.getAllScreensBack().map { it.map { screen -> screen.toBackground() } }
    }

    override fun getCurrentScreenData(screen: Screens): Flow<Background> {
        return screensDao.getScreensBackById(screenId = screen.value).map { it.toBackground() }
    }

    override suspend fun updateScreenData(data: Background) {
        screensDao.updateScreenImage(data = data.toScreensBackEntity())
    }
}