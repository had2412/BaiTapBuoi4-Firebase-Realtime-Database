package com.example.crud_frd

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_frd.adapter.EmpAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {
    private lateinit var ds:ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var rvEmp: RecyclerView
    private lateinit var txtLoadingData: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        txtLoadingData = findViewById(R.id.txtLoadingData)
        rvEmp = findViewById(R.id.rvEmp)

        rvEmp.layoutManager = LinearLayoutManager(this)
        rvEmp.setHasFixedSize(true)
        ds = arrayListOf<EmployeeModel>()

        GetThongTinNV()
    }

    private fun GetThongTinNV() {
        rvEmp.visibility = View.GONE
        txtLoadingData.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ds.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        ds.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(ds)
                    rvEmp.adapter = mAdapter
                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingActivity,EmployeeDetailsActivity::class.java)
                            intent.putExtra("empID",ds[position].empID)
                            intent.putExtra("empName",ds[position].empName)
                            intent.putExtra("empAge",ds[position].empAge)
                            intent.putExtra("empSalary",ds[position].empSalary)
                            startActivity(intent)
                        }
                    })
                    rvEmp.visibility = View.VISIBLE
                    txtLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}