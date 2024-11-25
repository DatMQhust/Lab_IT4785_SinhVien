package com.example.studentmanager

import android.app.AlertDialog
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val students: MutableList<StudentModel> ): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    interface OnContextMenuListener {
        fun onEdit(position: Int)
        fun onDelete(position: Int)
    }

    private var contextMenuListener: OnContextMenuListener? = null

    fun setOnContextMenuListener(listener: OnContextMenuListener) {
        contextMenuListener = listener
    }
    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        init{
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            val inflater = MenuInflater(p1?.context)
            inflater.inflate(R.menu.context_menu,p0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item,
            parent, false)
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId
        holder.itemView.setOnClickListener{}
        holder.itemView.setOnLongClickListener {
            selectedPosition = position
            false
        }

    }
    companion object {
        var selectedPosition: Int = -1
    }
}