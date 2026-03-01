package ru.asmelnikov.liquidshopper.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val taskName: String,
    val taskType: String,
    val timeStamp: Long
)

data class TaskWithItems(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "taskId"
    )
    val items: List<TaskItemEntity>
)