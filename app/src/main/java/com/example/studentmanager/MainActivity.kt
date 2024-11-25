package com.example.studentmanager

import android.app.AlertDialog
import android.app.Dialog
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var students: MutableList<StudentModel>
    private lateinit var studentAdapter : StudentAdapter
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val editStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Nhận dữ liệu trả về
            val editedName = result.data?.getStringExtra("hotendasua")
            val editedID = result.data?.getStringExtra("mssvdasua")
            val position = result.data?.getIntExtra("position1", -1) ?: -1

            // Cập nhật danh sách nếu dữ liệu hợp lệ
            if (position != -1 && editedName != null && editedID != null) {
                students[position].studentName = editedName
                students[position].studentId = editedID
                studentAdapter.notifyItemChanged(position)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        students = mutableListOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003"),
            StudentModel("Phạm Thị Dung", "SV004"),
            StudentModel("Đỗ Minh Đức", "SV005"),
            StudentModel("Vũ Thị Hoa", "SV006"),
            StudentModel("Hoàng Văn Hải", "SV007"),
            StudentModel("Bùi Thị Hạnh", "SV008"),
            StudentModel("Đinh Văn Hùng", "SV009"),
            StudentModel("Nguyễn Thị Linh", "SV010"),
            StudentModel("Phạm Văn Long", "SV011"),
            StudentModel("Trần Thị Mai", "SV012"),
            StudentModel("Lê Thị Ngọc", "SV013"),
            StudentModel("Vũ Văn Nam", "SV014"),
            StudentModel("Hoàng Thị Phương", "SV015"),
            StudentModel("Đỗ Văn Quân", "SV016"),
            StudentModel("Nguyễn Thị Thu", "SV017"),
            StudentModel("Trần Văn Tài", "SV018"),
            StudentModel("Phạm Thị Tuyết", "SV019"),
            StudentModel("Lê Văn Vũ", "SV020")
        )

        studentAdapter = StudentAdapter(students)

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            {
                    it:androidx.activity.result.ActivityResult->
                if (it.resultCode == RESULT_OK){
                    var hoten = it.data?.getStringExtra("hoten") ?: ""
                    var mssv = it.data?.getStringExtra("mssv") ?:""

                    val student = StudentModel(hoten,mssv)
                    students.add(student)
                    studentAdapter.notifyItemInserted(students.size - 1)

                }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_new -> {

                val intent = Intent(this, AddNewActivity::class.java)
                launcher.launch(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = StudentAdapter.selectedPosition
        return when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this,editStudent::class.java)
                intent.putExtra("hoten",students[position].studentName)
                intent.putExtra("mssv",students[position].studentId)
                intent.putExtra("position",position)
                editStudentLauncher.launch(intent)
                true
            }
            R.id.delete -> {
                val pos = students[position].studentName
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
                Toast.makeText(this, "Deleted student  ${pos}", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}