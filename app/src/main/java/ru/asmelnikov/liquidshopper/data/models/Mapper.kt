package ru.asmelnikov.liquidshopper.data.models

import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Task
import ru.asmelnikov.liquidshopper.domain.models.TaskTypes
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun TaskWithItems.toTasks(): Task {
    return Task(
        uid = task.uid,
        taskName = task.taskName,
        taskType = kotlin.runCatching { TaskTypes.valueOf(task.taskType) }.getOrDefault(TaskTypes.OTHER),
        timeStamp = task.timeStamp.toLocalDateTime(),
        items = items.map { it.toTaskItem() }
    )
}

fun TaskItemEntity.toTaskItem() : Item {
    return Item(
        uid = uid,
        taskId = taskId,
        itemName = itemName,
        count = count,
        price = price,
        units = units,
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

fun LocalDateTime.toTimestamp(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun Long.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()