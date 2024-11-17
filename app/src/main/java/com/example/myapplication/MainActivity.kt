package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var students: MutableList<StudentModel>
    private lateinit var studentAdapter : StudentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
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

        studentAdapter = StudentAdapter(students, this )

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        findViewById<Button>(R.id.btn_add_new).setOnClickListener(){
            addNewStudent()
        }

    }

    private fun addNewStudent (){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_dialog)
        val editHoten = dialog.findViewById<EditText>(R.id.editHoten)
        val editMSSV = dialog.findViewById<EditText>(R.id.editMSSV)
        dialog.findViewById<Button>(R.id.bntok).setOnClickListener {
            val hoten = editHoten.text.toString()
            val mssv = editMSSV.text.toString()
            if (hoten.isEmpty() || mssv.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
            else {
                val student = StudentModel(hoten, mssv)
                students.add(student)
                studentAdapter.notifyItemInserted(students.size - 1)
                dialog.dismiss()
            }

        }
        dialog.findViewById<Button>(R.id.btncancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }
    override fun onEditClick(position: Int,students: MutableList<StudentModel>) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_dialog)
        val editHoten = dialog.findViewById<EditText>(R.id.editHoten)
        val editMSSV = dialog.findViewById<EditText>(R.id.editMSSV)
        editHoten.setText(students[position].studentName)
        editMSSV.setText(students[position].studentId)
        dialog.findViewById<Button>(R.id.bntok).setOnClickListener {
            val hoten = editHoten.text.toString()
            val mssv = editMSSV.text.toString()
            if (hoten.isEmpty() || mssv.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
            else {
                students[position].studentName = hoten
                students[position].studentId = mssv
                studentAdapter.notifyItemChanged(position)
                dialog.dismiss()
            }
        }
        dialog.findViewById<Button>(R.id.btncancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    override fun onRemoveClick(position: Int,students: MutableList<StudentModel>) {
        AlertDialog.Builder(this)
            .setTitle("Bạn có thực sự muốn xoá sinh viên này")
            .setPositiveButton("Có") { dialog,which->
                val student = students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
                val view = findViewById<View>(android.R.id.content)
                Snackbar.make(view, "Đã xoá thành công", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", {
                        students.add(position,student)
                        studentAdapter.notifyItemInserted(position)
                    })
                    .show()
            }
            .setNegativeButton("Không"){dialog,which -> dialog.dismiss()}
            .show()
    }
}