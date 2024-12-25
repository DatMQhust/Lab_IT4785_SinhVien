package com.example.filemanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DirectoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)

        recyclerView = findViewById(R.id.recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val path = intent.getStringExtra("path") ?: return
        val files = FileUtils.getAllFilesInDirectory(path)

        adapter = FileListAdapter(files) { file ->
            if (file.isDirectory) {
                val intent = Intent(this, DirectoryActivity::class.java)
                intent.putExtra("path", file.absolutePath)
                startActivity(intent)
            } else if (file.isFile && file.extension == "txt") {
                val intent = Intent(this, FileViewerActivity::class.java)
                intent.putExtra("path", file.absolutePath)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Không hỗ trợ mở file này", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = adapter
    }
}