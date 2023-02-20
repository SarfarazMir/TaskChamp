package com.sfmir.taskchamp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sfmir.taskchamp.model.Task
import com.sfmir.taskchamp.util.TaskRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private var taskRoomDatabase: TaskRoomDatabase
    private var repository: TaskRepository

    init {
        taskRoomDatabase = TaskRoomDatabase.getInstance(application)!!
        repository = TaskRepository(taskRoomDatabase.getTaskDao())
    }

    fun getAllTasks() : LiveData<List<Task>> {
        return repository.getAllTasks()
    }

    fun getTask(id: Int) : LiveData<Task> {
        return repository.getTask(id)
    }

    fun insert(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(task)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(task)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(task)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}