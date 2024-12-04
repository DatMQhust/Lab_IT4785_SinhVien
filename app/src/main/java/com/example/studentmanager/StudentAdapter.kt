package com.example.studentmanager

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter

class StudentAdapter(private val context: Context, private var students: MutableList<StudentModel>) : BaseAdapter() {
    override fun getCount(): Int = students.size
    override fun getItem(position: Int): Any = students[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_student_item, parent, false)
        val student = students[position]

        view.findViewById<TextView>(R.id.student_name).text = student.studentName
        view.findViewById<TextView>(R.id.student_id).text = student.studentId

        return view
    }

    fun updateData(newStudents: MutableList<StudentModel>) {
        Log.d("StudentAdapter", "Updating data: $newStudents") // Log dữ liệu
        students = newStudents
        notifyDataSetChanged()

    }
}