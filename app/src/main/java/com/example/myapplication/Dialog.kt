package com.example.myapplication
//
//import android.app.Dialog
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import androidx.fragment.app.DialogFragment
//
//class Dialog(val items : List<StudentModel>) : DialogFragment() {
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = Dialog(requireContext())
//        dialog.setContentView(R.layout.layout_dialog)
//        val editHoten = dialog.findViewById<EditText>(R.id.editHoten)
//        val editMSSV = dialog.findViewById<EditText>(R.id.editMSSV)
//        dialog.findViewById<Button>(R.id.bntok).setOnClickListener{
//            val hoten = editHoten.text.toString()
//            val mssv = editMSSV.text.toString()
//            val student = StudentModel(hoten,mssv)
//            items.add(student)
//        }
//    }
//}