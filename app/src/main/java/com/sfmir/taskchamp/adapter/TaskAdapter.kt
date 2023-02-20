package com.sfmir.taskchamp.adapter

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.chip.Chip
import com.sfmir.taskchamp.R
import com.sfmir.taskchamp.model.Task
import com.sfmir.taskchamp.util.Utils

class TaskAdapter(private val dataset: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onRadioButtonClick(task: Task)
    }

    private lateinit var mListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class TaskViewHolder(view: View) : ViewHolder(view) {
        var radioButton: RadioButton
        var chipButton: Chip
        var task: TextView

        init {
            radioButton = view.findViewById(R.id.radio_button)
            chipButton = view.findViewById(R.id.chip_button)
            task = view.findViewById(R.id.task_textview)
        }

        init {
            view.setOnClickListener {
                mListener.onItemClick(dataset[adapterPosition])
            }

            radioButton.setOnClickListener {
                mListener.onRadioButtonClick(dataset[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.todo_row, parent, false)
        return TaskViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.chipButton.text = Utils.formatDate(dataset[position].dueDate)
        holder.task.text = dataset[position].task

        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(
                Color.LTGRAY,
                Utils.priorityColor(dataset[position])
            )
        )
        holder.radioButton.buttonTintList = colorStateList
        holder.chipButton.setTextColor(colorStateList)
        holder.chipButton.chipIconTint = colorStateList
        holder.task.setTextColor(colorStateList)
        // display strike through the task text if the task is expired
        if (Utils.isTaskExpired(dataset[position].dueDate)) holder.task.paintFlags = holder.task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        if (Utils.isTaskExpired(dataset[position].dueDate)) holder.chipButton.paintFlags = holder.chipButton.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}