package com.example.sqlite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student (
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    var hoten: String,
    var mssv: String
)