package ru.asmelnikov.liquidshopper.domain.models

data class Task(
    val uid: Int,
    val taskName: String,
    val taskType: String,
    val timeStamp: Long,
    val items: List<Item>
)

data class Item(
    val uid: Int,
    val taskId: Int,
    val itemName: String,
    val count: Int,
    val price: Int,
    val units: String,
    val bought: Boolean
)
