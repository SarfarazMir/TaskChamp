package com.sfmir.taskchamp.util

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.sfmir.taskchamp.model.Priority
import com.sfmir.taskchamp.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.log

class Utils {
    companion object {
        fun formatDate(date: Date) : String {
            val simpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
            simpleDateFormat.applyPattern("EEE, MMM, d")
            return simpleDateFormat.format(date)
        }

        fun hideSoftKeyBoard(view: View) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun isTaskExpired(dueDate: Date) : Boolean {
            val calendar = Calendar.getInstance()
            return (dueDate.month <= calendar.time.month) && ((dueDate.date < calendar.time.date))
        }

        fun priorityColor(task: Task) : Int {
            val color: Int = when (task.priority) {
                Priority.HIGH -> {
                    Color.rgb(201, 21, 23)
                }
                Priority.MEDIUM -> {
                    Color.rgb(255, 165, 0)
                }
                Priority.LOW -> {
                    Color.rgb(200, 207, 0)
                }
            }
            return color
        }
    }
}