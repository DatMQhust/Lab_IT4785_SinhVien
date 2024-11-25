package com.example.studentmanager

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class editStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)

        var edithoten = findViewById<EditText>(R.id.editHoten1)
        var editMssv = findViewById<EditText>(R.id.editMSSV1)

        edithoten.setText(intent.getStringExtra("hoten"))
        editMssv.setText(intent.getStringExtra("mssv"))
        val position = intent.getIntExtra("position",0)

        findViewById<Button>(R.id.bntok1).setOnClickListener{
            val hoten = edithoten.text.toString()
            val mssv = editMssv.text.toString()

            intent.putExtra("hotendasua",hoten)
            intent.putExtra("mssvdasua",mssv)
            intent.putExtra("position1",position)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

        findViewById<Button>(R.id.btncancel1).setOnClickListener{
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}