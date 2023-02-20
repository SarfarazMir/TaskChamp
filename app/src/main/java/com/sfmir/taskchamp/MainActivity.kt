package com.sfmir.taskchamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sfmir.taskchamp.activity.AboutActivity
import com.sfmir.taskchamp.adapter.TaskAdapter
import com.sfmir.taskchamp.data.SharedViewModel
import com.sfmir.taskchamp.data.TaskViewModel
import com.sfmir.taskchamp.databinding.ActivityMainBinding
import com.sfmir.taskchamp.fragment.BottomSheetFragment
import com.sfmir.taskchamp.model.Task

class MainActivity : AppCompatActivity(), TaskAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetFragment: BottomSheetFragment
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var tasksList: List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.showOverflowMenu()

        bottomSheetFragment = BottomSheetFragment()

        taskViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(TaskViewModel::class.java)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        taskViewModel.getAllTasks().observe(this) {
            tasksList = it
            val taskAdapter = TaskAdapter(it)
            recyclerView = binding.recyclerView
            taskAdapter.setOnItemClickListener(this)
            recyclerView.adapter = taskAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        binding.fabButton.setOnClickListener {
            sharedViewModel.isEditMode = false
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_button -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete all tasks?")
                builder.setPositiveButton("Ok") { dialog, id ->
                    if (tasksList.isNotEmpty()) {
                        taskViewModel.deleteAll()
                        Snackbar.make(binding.recyclerView, "All tasks deleted", Snackbar.LENGTH_LONG).show()
                    }
                }
                builder.setNegativeButton("Cancel") {
                    dialog, id ->
                }
                val dialog = builder.create()
                dialog.show()
            }
            R.id.about_button ->  {
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onItemClick(task: Task) {
        sharedViewModel.setTask(task)
        sharedViewModel.isEditMode = true
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    override fun onRadioButtonClick(task: Task) {
        taskViewModel.delete(task)
        Snackbar.make(binding.recyclerView, "Task deleted", Snackbar.LENGTH_LONG).show()
    }
}