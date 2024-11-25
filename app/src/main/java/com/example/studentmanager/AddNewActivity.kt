package com.example.studentmanager

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddNewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new)

        val edithoten = findViewById<EditText>(R.id.editHoten)
        val editMssv = findViewById<EditText>(R.id.editMSSV)
        val btnok = findViewById<Button>(R.id.bntok)
        val btncancel = findViewById<Button>(R.id.btncancel)

        btnok.setOnClickListener(){
            val hoten = edithoten.text.toString()
            val mssv = editMssv.text.toString()

            if (hoten == "" || mssv == ""){
                Toast.makeText(this,"Vui lòng nhập đầy đủ ho tên và mssv",Toast.LENGTH_SHORT).show()
            }
            else {
                intent.putExtra("hoten",hoten)
                intent.putExtra("mssv",mssv)

                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
        btncancel.setOnClickListener(){
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}