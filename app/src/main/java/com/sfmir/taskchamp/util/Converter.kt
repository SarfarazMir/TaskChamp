package com.sfmir.taskchamp.util

import androidx.room.TypeConverter
import com.sfmir.taskchamp.model.Priority
import java.util.Date

class Converter {
    @TypeConverter
    fun fromTimestamp(time: Long?) : Date? {
        return time?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(time: Date?) : Long? {
        return time?.time
    }

    @TypeConverter
    fun fromPriority(priority: Priority?) : String? {
        return priority?.name
    }

    @TypeConverter
    fun toPriority(priority: String?) : Priority? {
        return priority?.let { Priority.valueOf(it) }
    }
}