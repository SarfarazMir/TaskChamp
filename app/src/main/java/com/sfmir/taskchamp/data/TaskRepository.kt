package com.sfmir.taskchamp.data

import androidx.lifecycle.LiveData
import com.sfmir.taskchamp.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks() : LiveData<List<Task>> {
        return taskDao.getAllTasks()
    }

    fun getTask(id: Int) : LiveData<Task> {
        return taskDao.get(id)
    }

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }
}