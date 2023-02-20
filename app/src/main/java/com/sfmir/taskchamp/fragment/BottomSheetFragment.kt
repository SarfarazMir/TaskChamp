package com.sfmir.taskchamp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.sfmir.taskchamp.R
import com.sfmir.taskchamp.data.SharedViewModel
import com.sfmir.taskchamp.data.TaskViewModel
import com.sfmir.taskchamp.model.Priority
import com.sfmir.taskchamp.model.Task
import com.sfmir.taskchamp.util.Utils
import java.util.Calendar
import java.util.Date

class BottomSheetFragment : BottomSheetDialogFragment(){

    private val TAG = "BottomSheetFragment"

    private lateinit var date: Date
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var taskEditText: EditText
    private lateinit var priorityRadioGroup: RadioGroup
    private lateinit var dueDateGroup: LinearLayout
    private lateinit var calendarView: CalendarView
    private lateinit var todayBtn: Chip
    private lateinit var tomorrowBtn: Chip
    private lateinit var nextWeekBtn: Chip

    private var priority: Priority = Priority.LOW

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayBtn = view.findViewById(R.id.today_chip)
        tomorrowBtn = view.findViewById(R.id.tomorrow_chip)
        nextWeekBtn = view.findViewById(R.id.next_week_chip)
        val calendarBtn = view.findViewById<ImageButton>(R.id.calender_button)
        val priorityBtn = view.findViewById<ImageButton>(R.id.priority_button)
        val saveButton = view.findViewById<ImageButton>(R.id.save_button)

        taskEditText = view.findViewById(R.id.enter_task_field)
        priorityRadioGroup = view.findViewById(R.id.priorityRadioGroup)
        dueDateGroup = view.findViewById(R.id.dueDateGroup)
        calendarView = view.findViewById(R.id.calenderView)

        date = calendar.time

        taskViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(TaskViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity())
            .get(SharedViewModel::class.java)

        calendarBtn.setOnClickListener {
           if (dueDateGroup.visibility == View.GONE) dueDateGroup.visibility = View.VISIBLE
            else
               dueDateGroup.visibility = View.GONE
            Utils.hideSoftKeyBoard(view)
        }

        priorityBtn.setOnClickListener {
            if (priorityRadioGroup.visibility == View.GONE) priorityRadioGroup.visibility = View.VISIBLE
            else
                priorityRadioGroup.visibility = View.GONE

            if (priorityRadioGroup.visibility == View.VISIBLE) {
                priorityRadioGroup.setOnCheckedChangeListener {
                        group, checkedId ->

                    val radioButton: RadioButton =  view.findViewById(checkedId)
                    if (radioButton.id == R.id.priority_radio_button_high) {
                        priority = Priority.HIGH
                    } else if (radioButton.id == R.id.priority_radio_button_medium) {
                        priority = Priority.MEDIUM
                    } else if (radioButton.id == R.id.priority_radio_button_low) {
                        priority = Priority.LOW
                    }
                }
            }
        }

        saveButton.setOnClickListener {
           if (taskEditText.text.isNotEmpty()) {
               if (!sharedViewModel.isEditMode) {
                   taskViewModel.insert(Task(0, taskEditText.text.toString(), priority, date, Calendar.getInstance().time, false))
                   Snackbar.make(requireActivity().findViewById(R.id.recyclerView), "Task saved", Snackbar.LENGTH_LONG).show()
               } else {
                   val task: Task? = sharedViewModel.getTask().value
                   task?.task = taskEditText.text.toString()
                   task?.priority = priority
                   task?.dueDate = date
                   task?.dateCreated = Calendar.getInstance().time
                   if (task != null) {
                       taskViewModel.update(task)
                       Snackbar.make(requireActivity().findViewById(R.id.recyclerView), "Task updated", Snackbar.LENGTH_LONG).show()
                   }
                   sharedViewModel.isEditMode = false
               }
               Utils.hideSoftKeyBoard(view)
               dismiss()
           }

            else
                Snackbar.make(view, "Task cannot be empty", Snackbar.LENGTH_LONG).show()
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            date = calendar.time
        }

        todayBtn.setOnClickListener {
            date = Calendar.getInstance().time
            selectDueDateChip(todayBtn, tomorrowBtn, nextWeekBtn)
        }

        tomorrowBtn.setOnClickListener {
            calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, 1)
            date = calendar.time
            selectDueDateChip(tomorrowBtn, todayBtn, nextWeekBtn)
        }

        nextWeekBtn.setOnClickListener {
            calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, 7)
            date = calendar.time
            selectDueDateChip(nextWeekBtn, todayBtn, tomorrowBtn)
        }
    }

    private fun selectDueDateChip(selectedChip: Chip, vararg unSelectedChips: Chip) {
        unSelectedChips.forEach { it.isSelected = false }
        selectedChip.isSelected = true
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${sharedViewModel.isEditMode}")
        if (sharedViewModel.getTask().value != null && sharedViewModel.isEditMode) {
            val task: Task = sharedViewModel.getTask().value!!
            taskEditText.setText(task.task)
        } else {
            taskEditText.setText("")
        }
    }
}