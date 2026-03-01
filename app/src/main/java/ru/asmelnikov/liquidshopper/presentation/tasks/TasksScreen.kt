package ru.asmelnikov.liquidshopper.presentation.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.asmelnikov.liquidshopper.domain.models.Task
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(items = state.tasks) { task ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "${task.uid}"
                    )

                    IconButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            viewModel.deleteTask(task = task)
                        }) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "delete"
                        )
                    }
                }
            }

        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            shape = RoundedCornerShape(18.dp),
            onClick = {
                viewModel.insertTask(
                    Task(
                        uid = UUID.randomUUID().hashCode(),
                        taskName = "Carl Mendez",
                        taskType = "posuere",
                        timeStamp = LocalDateTime.now().toTimestamp(),
                        items = listOf()
                    )
                )
            }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Filled.Add,
                contentDescription = "add"
            )
        }
    }
}

fun LocalDateTime.toTimestamp(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}