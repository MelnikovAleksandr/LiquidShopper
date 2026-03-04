package ru.asmelnikov.liquidshopper.utils.intent

import android.content.Context
import android.content.Intent
import ru.asmelnikov.liquidshopper.R
import ru.asmelnikov.liquidshopper.domain.models.Task

fun sharedTask(task: Task, context: Context) {
    try {
        Intent(Intent.ACTION_SEND).apply {

            val itemsListString =
                "${task.taskName}:\n " + task.items.map { "${it.itemName} - ${it.count} ${context.getString(it.units.stringRes)}\n" }

            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                itemsListString.replace("\\[".toRegex(), "")
                    .replace("]".toRegex(), "")
                    .replace(",".toRegex(), "")
            )
            putExtra(
                Intent.EXTRA_SUBJECT,
                "${context.getString(R.string.shared_list_title)} ${task.taskName}"
            )
        }.also { intent ->
            val chooseIntent = Intent.createChooser(
                intent, context.getString(R.string.shared_list)
            )
            context.startActivity(chooseIntent)
        }
    } catch (_: Exception) {}
}