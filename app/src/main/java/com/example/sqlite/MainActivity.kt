package com.example.sqlite

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var students: MutableList<Student>  = mutableListOf()
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentDao: StudentDAO

    private val editStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val editedName = result.data?.getStringExtra("hotendasua")
            val editedID = result.data?.getStringExtra("mssvdasua")
            val position = result.data?.getIntExtra("position1", -1) ?: -1

            if (position != -1 && editedName != null && editedID != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val id = studentDao.getIdbyMssv(students[position].mssv)
                    val updatedStudent = Student(_id = id, hoten = editedName, mssv = editedID)
                    studentDao.updateStudent(updatedStudent)

                    withContext(Dispatchers.Main) {
                        students[position] = updatedStudent
                        studentAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        studentDao = StudentDatabase.getInstance(this).studentDao()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        lifecycleScope.launch(Dispatchers.IO) {
            val studentList = studentDao.getAllStudents().toMutableList()
            withContext(Dispatchers.Main) {
                students = studentList
                setupRecyclerView()
            }
        }
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(students)
        findViewById<RecyclerView>(R.id.recycler_view_students).apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_new -> {
                val intent = Intent(this, AddNewActivity::class.java)
                launcher.launch(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val hoten = result.data?.getStringExtra("hoten") ?: ""
            val mssv = result.data?.getStringExtra("mssv") ?: ""
            val newStudent = Student(hoten = hoten, mssv = mssv)

            lifecycleScope.launch(Dispatchers.IO) {
                val id = studentDao.insertStudent(newStudent).toInt()
                val insertedStudent = newStudent.copy(_id = id)

                withContext(Dispatchers.Main) {
                    students.add(insertedStudent)
                    studentAdapter.notifyItemInserted(students.size - 1)
                }
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = StudentAdapter.selectedPosition

        return when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this, editStudent::class.java).apply {
                    putExtra("hoten", students[position].hoten)
                    putExtra("mssv", students[position].mssv)
                    putExtra("position", position)
                }
                editStudentLauncher.launch(intent)
                true
            }
            R.id.delete -> {
                val studentToDelete = students[position]
                lifecycleScope.launch(Dispatchers.IO) {
                    val id = studentDao.getIdbyMssv(studentToDelete.mssv)
                    studentDao.deleteStudent(studentToDelete.copy(_id = id))

                    withContext(Dispatchers.Main) {
                        students.removeAt(position)
                        studentAdapter.notifyItemRemoved(position)
                        Toast.makeText(this@MainActivity, "Deleted student: ${studentToDelete.hoten}", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
