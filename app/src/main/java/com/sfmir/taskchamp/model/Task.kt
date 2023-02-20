package com.sfmir.taskchamp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "task_table")
data class Task(
    @ColumnInfo(name = "task_id", defaultValue = "0")
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var task: String,
    var priority: Priority,
    @ColumnInfo(name = "due_date")
    var dueDate: Date,
    @ColumnInfo(name = "date_created")
    var dateCreated: Date,
    @ColumnInfo(name = "is_done")
    var isDone: Boolean
)