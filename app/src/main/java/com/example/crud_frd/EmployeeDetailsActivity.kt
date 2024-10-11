package com.example.crud_frd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmpAge: TextView
    private lateinit var tvEmpSalary: TextView
    private lateinit var tvEmpID: TextView
    private lateinit var btnDelete: Button
    private lateinit var btnUpdate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)
        tvEmpID = findViewById(R.id.tvEmpId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmpAge = findViewById(R.id.tvEmpAge)
        tvEmpSalary = findViewById(R.id.tvEmpSalary)
        btnDelete = findViewById(R.id.btnDelete)
        btnUpdate = findViewById(R.id.btnUpdate)
        setValueToView()
        btnDelete.setOnClickListener{
            deleteRecord(
                intent.getStringExtra("empID").toString()
            )
        }
        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("empID").toString(),
                intent.getStringExtra("empName").toString()

            )
        }
    }

    private fun openUpdateDialog(empID: String, empName: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)
        mDialog.setView(mDialogView)

        // Khai báo các EditText và Button trong dialog
        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        // Đặt các giá trị hiện tại vào các trường
        etEmpName.setText(intent.getStringExtra("empName").toString())
        etEmpAge.setText(intent.getStringExtra("empAge").toString())
        etEmpSalary.setText(intent.getStringExtra("empSalary").toString())

        mDialog.setTitle("Updating ....")
        val alertDialog = mDialog.create()
        alertDialog.show()

        // Xử lý khi nhấn nút Update
            btnUpdateData.setOnClickListener{
                updateEmpData(
                    empID,
                    etEmpName.text.toString(),
                    etEmpAge.text.toString(),
                    etEmpSalary.text.toString()
                )

            Toast.makeText(application, "Employee Data updated", Toast.LENGTH_SHORT).show()
                tvEmpName.setText(etEmpName.text.toString())
                tvEmpAge.setText(etEmpAge.text.toString())
                tvEmpSalary.setText(etEmpSalary.text.toString())
                alertDialog.dismiss()
    }
    }

    private fun updateEmpData(
        id: String, name: String, age: String, salary: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo = EmployeeModel(id,name,age,salary)
        dbRef.setValue(empInfo)
    }


    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()
        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data da xoa", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{
            err ->
            Toast.makeText(this, "Delete err ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setValueToView() {
        tvEmpID.text = intent.getStringExtra("empID")
        tvEmpName.text = intent.getStringExtra("empName")
        tvEmpAge.text = intent.getStringExtra("empAge")
        tvEmpSalary.text = intent.getStringExtra("empSalary")
    }
}