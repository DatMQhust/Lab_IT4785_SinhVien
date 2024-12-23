package com.example.sqlite

import android.app.AlertDialog
import android.app.Dialog
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var students: MutableList<StudentModel>
    private lateinit var studentAdapter : StudentAdapter
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var db :  SQLiteDatabase
    private val editStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val editedName = result.data?.getStringExtra("hotendasua")
            val editedID = result.data?.getStringExtra("mssvdasua")
            val position = result.data?.getIntExtra("position1", -1) ?: -1

            if (position != -1 && editedName != null && editedID != null) {
                students[position].studentName = editedName
                students[position].studentId = editedID
                studentAdapter.notifyItemChanged(position)
                db.beginTransaction()
                try {
                    val sql = "UPDATE tblAMIGO SET name = ?, mssv = ? WHERE recID = ?"
                    val statement = db.compileStatement(sql)
                    statement.bindString(1, editedName)
                    statement.bindString(2, editedID)
                    statement.bindLong(3, (position+1).toLong())

                    statement.executeUpdateDelete()
                    db.setTransactionSuccessful()
                    Toast.makeText(this, "Update student ${position+1} successful", Toast.LENGTH_SHORT).show()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Toast.makeText(this, "Update student ${position} failed", Toast.LENGTH_SHORT).show()
                } finally {
                    db.endTransaction()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        db = SQLiteDatabase.openDatabase(filesDir.path + "/mydb", null,
            SQLiteDatabase.CREATE_IF_NECESSARY)
        //createTable()
        students = mutableListOf()
        students.clear()
        val cs = db.rawQuery("SELECT * FROM tblAMIGO", null)
        if (cs.moveToFirst()) {
            do {
                val recID = cs.getInt(0)
                val name = cs.getString(1)
                val mssv = cs.getString(2)
                Log.v("TAG","$name-$mssv-$recID")
                students.add(StudentModel(name, mssv))
            } while (cs.moveToNext())
        }
        cs.close()
        studentAdapter = StudentAdapter(students)

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            {
                    it:androidx.activity.result.ActivityResult->
                if (it.resultCode == RESULT_OK){
                    var hoten = it.data?.getStringExtra("hoten") ?: ""
                    var mssv = it.data?.getStringExtra("mssv") ?:""
                    val newStudent = StudentModel(hoten,mssv)
                    try {
                        db.execSQL("INSERT INTO tblAMIGO(name, mssv) VALUES ('$hoten', '$mssv')")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.v("TAG","$e")
                        Toast.makeText(this, "Thêm dữ liệu thất bại", Toast.LENGTH_SHORT).show()
                    }
                    students.add(newStudent)
                    studentAdapter.notifyItemInserted(students.size - 1)

                }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_new -> {

                val intent = Intent(this, AddNewActivity::class.java)
                launcher.launch(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = StudentAdapter.selectedPosition
        return when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this,editStudent::class.java)
                intent.putExtra("hoten",students[position].studentName)
                intent.putExtra("mssv",students[position].studentId)
                intent.putExtra("position",position)
                editStudentLauncher.launch(intent)
                true
            }
            R.id.delete -> {
                val pos = students[position].studentName
                val mssv = students[position].studentId
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
                db.beginTransaction()
                try {
                    val sql = "DELETE from tblAMIGO WHERE mssv = ?"
                    val statement = db.compileStatement(sql)
                    statement.bindString(1,mssv )

                    statement.executeUpdateDelete()
                    db.setTransactionSuccessful()
                    Toast.makeText(this, "delete student ${pos} successful", Toast.LENGTH_SHORT).show()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Toast.makeText(this, "delete student ${pos} failed", Toast.LENGTH_SHORT).show()
                } finally {
                    db.endTransaction()
                }
                Toast.makeText(this, "Deleted student  ${pos}", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    fun createTable() {
        db.beginTransaction()
        try {
            db.execSQL("create table tblAMIGO(" +
                    "recID integer primary key autoincrement," +
                    "name text," +
                    "mssv text)")
            db.execSQL("insert into tblAMIGO(name, mssv) values ('Nguyễn Văn An', 'SV001')")
            db.execSQL("insert into tblAMIGO(name, mssv) values ('Trần Thị Bảo', 'SV002')")
            db.execSQL("insert into tblAMIGO(name, mssv) values ('Lê Hoàng Cường', 'SV003')")
            db.setTransactionSuccessful()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    override fun onStop() {

        super.onStop()
    }
}