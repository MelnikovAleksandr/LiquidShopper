package ru.asmelnikov.liquidshopper.domain.models

import java.time.LocalDateTime
import java.util.UUID

val taskItemsMock = listOf(
    Item(
        uid = UUID.randomUUID().hashCode(),
        taskId = UUID.randomUUID().hashCode(),
        itemName = "",
        count = 1,
        price = 1,
        units = UnitType.PIECES,
        bought = false
    ),
    Item(
        uid = UUID.randomUUID().hashCode(),
        taskId = UUID.randomUUID().hashCode(),
        itemName = "",
        count = 1,
        price = 1,
        units = UnitType.PIECES,
        bought = true
    ),
    Item(
        uid = UUID.randomUUID().hashCode(),
        taskId = UUID.randomUUID().hashCode(),
        itemName = "",
        count = 1,
        price = 1,
        units = UnitType.PIECES,
        bought = true
    ),
    Item(
        uid = UUID.randomUUID().hashCode(),
        taskId = UUID.randomUUID().hashCode(),
        itemName = "",
        count = 1,
        price = 1,
        units = UnitType.PIECES,
        bought = true
    ),
    Item(
        uid = UUID.randomUUID().hashCode(),
        taskId = UUID.randomUUID().hashCode(),
        itemName = "",
        count = 1,
        price = 1,
        units = UnitType.PIECES,
        bought = true
    )
)

val taskMock = Task(
    uid = 1377,
    taskName = "Test name of Task for PreviewTest name of Task for PreviewTest name of Task for Preview",
    taskType = TaskTypes.PETS,
    timeStamp = LocalDateTime.now(),
    isCompleted = false,
    allItemsCount = 0,
    inProgressItemsCount = 0,
    items = listOf()
)

val taskWithItemsMock = Task(
    uid = UUID.randomUUID().hashCode(),
    taskName = "Test name of Task",
    taskType = TaskTypes.ALCOHOL,
    timeStamp = LocalDateTime.now(),
    isCompleted = true,
    allItemsCount = 2,
    inProgressItemsCount = 2,
    items = taskItemsMock
)

val tasksListMock = listOf(
    taskWithItemsMock, taskWithItemsMock, taskWithItemsMock.copy(isCompleted = false), taskWithItemsMock.copy(isCompleted = false), taskWithItemsMock.copy(isCompleted = false)
)