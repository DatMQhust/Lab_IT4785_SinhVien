package com.example.filemanager

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class FileViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_viewer)

        val textView = findViewById<TextView>(R.id.textView)
        val path = intent.getStringExtra("path") ?: return

        val file = File(path)
        if (file.exists() && file.isFile) {
            textView.text = file.readText()
        } else {
            textView.text = "File không tồn tại hoặc không đọc được."
        }
    }
}