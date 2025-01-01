package com.example.sqlite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDAO {
    @Query("select * from students")
    suspend fun getAllStudents(): List<Student>

    @Query("select * from students where mssv = :ms")
    suspend fun getStudentByMssv(ms: String): List<Student>

    @Query("select * from students where hoten like '%' || :name || '%'")
    suspend fun getStudentsByName(name: String): List<Student>

    @Insert
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student): Int
    @Query("select _id from students where mssv = :ms limit 1")
    suspend fun getIdbyMssv(ms: String): Int
    @Delete
    suspend fun deleteStudent(student: Student): Int

    @Query("delete from students where mssv = :mssv")
    suspend fun deleteByMssv(mssv: String): Int
}