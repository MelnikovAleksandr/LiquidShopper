package ru.asmelnikov.liquidshopper.data.models

import ru.asmelnikov.liquidshopper.domain.models.Background
import ru.asmelnikov.liquidshopper.domain.models.BackgroundImage
import ru.asmelnikov.liquidshopper.domain.models.GroupedTasksByDay
import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Screens
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import ru.asmelnikov.liquidshopper.domain.models.UnitType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun List<TaskWithItems>.toGroupedTasksByDay(): List<GroupedTasksByDay> {
    return this
        .map { it.toTasks() }
        .groupBy { task -> task.timeStamp.toLocalDate() }
        .map { (date, tasks) ->
            GroupedTasksByDay(
                start = date,
                tasksCount = tasks.count(),
                tasks = tasks.sortedBy { it.isCompleted }
            )
        }
        .sortedBy { it.start }
}

fun TaskWithItems.toTasks(): Task {
    val itemsDomain = items.map { it.toTaskItem() }.sortedBy { it.bought }
    val allItemsCount = itemsDomain.count()
    val inProgressItemsCount = itemsDomain.count { it.bought }
    val isCompleted = allItemsCount > 0 && allItemsCount == inProgressItemsCount

    return Task(
        uid = task.uid,
        taskName = task.taskName,
        taskType = kotlin.runCatching { TaskTypes.valueOf(task.taskType) }
            .getOrDefault(TaskTypes.OTHER),
        timeStamp = task.timeStamp.toLocalDateTime(),
        isCompleted = isCompleted,
        allItemsCount = allItemsCount,
        inProgressItemsCount = inProgressItemsCount,
        sumPrice = itemsDomain.sumOf { it.price },
        items = itemsDomain
    )
}

fun TaskItemEntity.toTaskItem(): Item {
    return Item(
        uid = uid,
        taskId = taskId,
        itemName = itemName,
        count = count,
        price = price,
        units = kotlin.runCatching { UnitType.valueOf(units) }.getOrDefault(UnitType.PIECES),
        bought = bought
    )
}

fun Item.toTaskItemEntity(): TaskItemEntity {
    return TaskItemEntity(
        uid = uid,
        taskId = taskId,
        itemName = itemName,
        count = count,
        price = price,
        units = units.name,
        bought = bought
    )
}

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        uid = uid,
        taskName = taskName,
        taskType = taskType.name,
        timeStamp = timeStamp.toTimestamp()
    )
}

fun ScreensBackEntity.toBackground(): Background {
    return Background(
        screen = Screens.fromValue(this.screenId),
        data = BackgroundImage.fromValue(this.backImageId)
    )
}

fun Background.toScreensBackEntity(): ScreensBackEntity {
    return ScreensBackEntity(
        screenId = this.screen.value,
        backImageId = this.data.value
    )
}

fun LocalDateTime.toTimestamp(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()