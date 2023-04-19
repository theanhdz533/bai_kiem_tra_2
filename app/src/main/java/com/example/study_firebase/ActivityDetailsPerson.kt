package com.example.study_firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.study_firebase.databinding.ActivityDetailsPersonBinding
import com.example.study_firebase.model.ProductModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ActivityDetailsPerson : AppCompatActivity() {
    lateinit var dbRef : DatabaseReference
    lateinit var binding: ActivityDetailsPersonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_person)
        binding = ActivityDetailsPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // display description
        details()

        // btn delete
        binding.btnDelete.setOnClickListener {
            deleteRecord(intent.getStringExtra("id").toString())
        }

    }



    // fun delete

    private fun deleteRecord(id:String) {
     dbRef = FirebaseDatabase.getInstance().getReference("product").child(id)
      val delete = dbRef.removeValue()
      delete.addOnSuccessListener {
          Toast.makeText(this,"Deleted!!!",Toast.LENGTH_SHORT).show()
          val intent = Intent(this,ActivityShowData::class.java)
          finish()
          startActivity(intent)
      }
          .addOnFailureListener {err->
              Toast.makeText(this,"Deleted error ${err.message}!!!",Toast.LENGTH_SHORT).show()
          }
    }

    private fun details() {
        binding.txtName.setText(" "+intent.getStringExtra("name"))
        binding.txtType.setText(" "+intent.getStringExtra("type"))
        binding.txtPrice.setText(" "+intent.getStringExtra("price")+" (VNĐ)")
        Glide.with(this)
            .load(intent.getStringExtra("link").toString())
            .into(binding.imgDetail)
    }

}