package com.sfmir.taskchamp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sfmir.taskchamp.model.Task

class SharedViewModel : ViewModel() {

    private val mutableLiveData = MutableLiveData<Task>()
    var isEditMode: Boolean = false

    fun setTask(task: Task) {
        mutableLiveData.value = task
    }

    fun getTask() : LiveData<Task> = mutableLiveData
}