package com.sfmir.taskchamp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sfmir.taskchamp.model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Query("SELECT * FROM task_table ORDER BY CASE priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 END;")
    fun getAllTasks() : LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE task_id = :id")
    fun get(id: Int) : LiveData<Task>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()
}