package ru.asmelnikov.liquidshopper.data.models

import ru.asmelnikov.liquidshopper.domain.models.Item
import ru.asmelnikov.liquidshopper.domain.models.Task

fun TaskWithItems.toTasks(): Task {
    return Task(
        uid = task.uid,
        taskName = task.taskName,
        taskType = task.taskType,
        timeStamp = task.timeStamp,
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
        taskType = taskType,
        timeStamp = timeStamp
    )
}