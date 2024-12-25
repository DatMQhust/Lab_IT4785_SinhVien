package com.example.filemanager

import java.io.File
import java.io.IOException

object FileUtils {
    fun getAllFilesInDirectory(directoryPath: String?): List<File> {
        if (directoryPath.isNullOrEmpty()) return emptyList()

        val directory = File(directoryPath)
        return try {
            if (directory.exists() && directory.isDirectory) {
                directory.listFiles()?.toList() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            // Ghi log hoặc xử lý lỗi tùy ý
            println("Error accessing directory: ${e.message}")
            emptyList()
        }
    }
}
