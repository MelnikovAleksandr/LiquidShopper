package ru.asmelnikov.liquidshopper.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("taskId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class TaskItemEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val taskId: Int,
    val itemName: String,
    val count: Int,
    val price: Int,
    val units: String,
    val bought: Boolean
)

data class TaskItemWithTask(
    @Embedded val taskItem: TaskItemEntity,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "uid"
    )
    val task: TaskEntity
)