package com.sfmir.taskchamp.util

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sfmir.taskchamp.data.TaskDao
import com.sfmir.taskchamp.model.Task

@Database(entities = [Task::class], version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)], exportSchema = true)
@TypeConverters(value = [Converter::class])
abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun getTaskDao() : TaskDao

    companion object {
        @Volatile
        private var instance: TaskRoomDatabase? = null

        fun getInstance(context: Context) : TaskRoomDatabase? {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(context, TaskRoomDatabase::class.java, "task_database")
                        .addCallback(callback)
                        .build()
                }
            }
            return instance
        }

        private val callback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}