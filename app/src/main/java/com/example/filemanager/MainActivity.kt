 package com.example.filemanager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

 class MainActivity : AppCompatActivity() {
     private lateinit var recycleView : RecyclerView
     private lateinit var adapter : FileListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        recycleView = findViewById(R.id.recycle_view)
        recycleView.layoutManager = LinearLayoutManager(this)
        val rootPath = Environment.getExternalStorageDirectory().path
        val files = FileUtils.getAllFilesInDirectory(rootPath)
        adapter = FileListAdapter(files) { file ->
            if (file.isDirectory) {
                val intent = Intent(this, DirectoryActivity::class.java)
                intent.putExtra("path", file.absolutePath)
                startActivity(intent)
            } else if (file.isFile && file.extension == "txt") {
                val intent = Intent(this, FileViewerActivity::class.java)
                intent.putExtra("path", file.absolutePath)
                startActivity(intent)
            }
        }
        recycleView.adapter = adapter
//        binding.btnWrite.setOnClickListener{
//            val sdPath = Environment.getExternalStorageDirectory().path
//            Log.v("TAG","${sdPath}")
//            val file = File("$sdPath/my_data.txt")
//            val outputStream = file.outputStream()
//            val writer = outputStream.writer()
//            writer.write(binding.inputResource.text.toString())
//            writer.close()
//        }
//        binding.btnLoad.setOnClickListener{
//            val sdPath = Environment.getExternalStorageDirectory().path
//            val file = File("$sdPath/my_data.txt")
//            val inputStream = file.inputStream()
//            val reader = inputStream.reader()
//            val content = reader.readText()
//            reader.close()
//            binding.textView.setText(content)
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent( Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }




    }
}