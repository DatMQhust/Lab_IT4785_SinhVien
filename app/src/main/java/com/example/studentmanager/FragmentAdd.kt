package com.example.studentmanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studentmanager.R
import com.example.studentmanager.StudentModel
import com.example.studentmanager.StudentViewModel

class FragmentAdd : Fragment() {
    private var studentName: String? = null
    private var studentId: String? = null
    private var position: Int? = null
    private val studentViewModel: StudentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FragmentAdd", "onCreate called")
        arguments?.let {
            studentName = it.getString("studentName")
            studentId = it.getString("studentId")
            position = it.getInt("position", -1).takeIf { pos -> pos >= 0 }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("FragmentAdd", "onCreateView called")
        val view = inflater.inflate(R.layout.activity_add_new, container, false)
        Log.d("FragmentAdd", "View inflated") // Log sau khi inflate
        val editName = view.findViewById<EditText>(R.id.editHoten)
        val editId = view.findViewById<EditText>(R.id.editMSSV)
        val btnSave = view.findViewById<Button>(R.id.bntok)
        val btnCancel = view.findViewById<Button>(R.id.btncancel)
        Log.d("FragmentAdd", "View created successfully")
        if (position != null) {
            btnSave.text = "Save Changes"
            editName.setText(studentName)
            editId.setText(studentId)
        } else {
            btnSave.text = "Add Student"
        }

        editName.setText(studentName)
        editId.setText(studentId)

        btnSave.setOnClickListener {
            val updatedName = editName.text.toString().trim()
            val updatedId = editId.text.toString().trim()

            if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
                val student = StudentModel(updatedName, updatedId)
                studentViewModel.saveStudent(student, position)
                findNavController().navigateUp()
            }
        }
        btnCancel.setOnClickListener{
            findNavController().navigateUp()
        }
        return view
    }


}