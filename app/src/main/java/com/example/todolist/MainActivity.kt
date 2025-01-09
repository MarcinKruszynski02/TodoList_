package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.TodoListTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                TodoApp()
            }
        }
    }
}

data class Task(
    val text: String,
    var isStarred: Boolean = false
)

@Composable
fun TodoApp() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTask by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
                    .background(Color(0xFF333333)),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Lista zadań", style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF333333))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .background(Color.Black.copy(alpha = 0.2f)), // Półprzezroczyste tło
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Default, // Używamy tej samej czcionki
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 20.sp // Używamy tej samej wielkości czcionki
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            if (newTask.text.isEmpty()) {
                                Text(
                                    "Wprowadź zadanie",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = TextStyle(
                                        fontFamily = FontFamily.Default, // Taka sama czcionka
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 20.sp // Taka sama czcionka
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Button(onClick = {
                    if (newTask.text.isNotBlank()) {
                        tasks = tasks + Task(newTask.text)
                        newTask = TextFieldValue("")
                    }
                }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                tasks.forEachIndexed { index, task ->
                    TaskItem(
                        task = task,
                        taskNumber = index + 1, // Numeracja zaczyna się od 1
                        onDelete = {
                            tasks = tasks.toMutableList().apply { removeAt(index) }
                        },
                        onStar = {
                            tasks = tasks.toMutableList().apply {
                                this[index] = this[index].copy(isStarred = !this[index].isStarred)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, taskNumber: Int, onDelete: () -> Unit, onStar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            // Numerowanie zadań
            text = "$taskNumber. ${task.text}",
            modifier = Modifier.weight(1f),
            color = if (task.isStarred) Color(0xFFFFD700) else Color.White // Złoty kolor dla gwiazdkowanych zadań
        )
        Row {
            Button(onClick = onStar) {
                Text(if (task.isStarred) "Odważnik" else "Ważne")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}