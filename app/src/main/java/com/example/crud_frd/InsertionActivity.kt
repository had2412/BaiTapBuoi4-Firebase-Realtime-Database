package com.example.crud_frd

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {
    private lateinit var dbRef : DatabaseReference
    private lateinit var btnSave: Button
    private lateinit var edtEmpName: EditText
    private lateinit var edtEmpAge: EditText
    private lateinit var edtEmpSalary: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)
        btnSave = findViewById(R.id.btnSave)
        edtEmpName = findViewById(R.id.edtEmpName)
        edtEmpAge = findViewById(R.id.edtEmpAge)
        edtEmpSalary = findViewById(R.id.edtEmpSalary)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")
        btnSave.setOnClickListener{
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {
        val empName = edtEmpName.text.toString()
        val empAge = edtEmpAge.text.toString()
        val empSalary = edtEmpSalary.text.toString()

        val empId = dbRef.push().key!!
        val employee = EmployeeModel(empId,empName,empAge,empSalary)

        dbRef.child(empId).setValue(employee).addOnCompleteListener{
            Toast.makeText(this,"Data insert thanh cong",Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                err -> Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
            }
    }
}