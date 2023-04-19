package com.example.study_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.study_firebase.adapter.ItemDataAdapter
import com.example.study_firebase.databinding.ActivityShowDataBinding
import com.example.study_firebase.model.ProductModel
import com.google.firebase.database.*

class ActivityShowData : AppCompatActivity() {
    lateinit var binding: ActivityShowDataBinding
    lateinit var productList:MutableList<ProductModel>
    lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)
        binding = ActivityShowDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

       adapter()
    }

    private fun adapter() {
        // set layout for adapter
        binding.rvPersonData.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rvPersonData.setHasFixedSize(true)

        productList =  mutableListOf()

        // get data person
        information()

    }

    private fun information() {
        binding.rvPersonData.visibility = View.GONE
        binding.txtTitle.visibility = View.GONE
        binding.txtLoad.visibility = View.VISIBLE

        // get data
        dbRef = FirebaseDatabase.getInstance().getReference("product")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        val dataItem = data.getValue(ProductModel::class.java)
                        productList.add(dataItem!!)

                    }
                    // set data
                    val adapter = ItemDataAdapter(productList)
                    val rv = findViewById<RecyclerView>(R.id.rvPersonData)
                    rv.adapter = adapter

                    // item click listener
                    adapter.setOnItemClickListener(object :ItemDataAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                           val intent = Intent(this@ActivityShowData,ActivityDetailsPerson::class.java)
                            // put data
                            intent.putExtra("id",productList[position].id)
                            intent.putExtra("name",productList[position].name)
                            intent.putExtra("type",productList[position].type)
                            intent.putExtra("price",productList[position].price)
                            intent.putExtra("link",productList[position].linkImg)
                            startActivity(intent)
                        }
                    })

                    // display data
                    binding.rvPersonData.visibility = View.VISIBLE
                    binding.txtTitle.visibility = View.VISIBLE
                    binding.txtLoad.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}