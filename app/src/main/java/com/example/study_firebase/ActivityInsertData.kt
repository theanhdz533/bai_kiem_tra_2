package com.example.study_firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toFile
import com.example.study_firebase.databinding.ActivityInsertDataBinding
import com.example.study_firebase.model.ProductModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ActivityInsertData : AppCompatActivity() {
    lateinit var binding: ActivityInsertDataBinding
    // references
    lateinit var dbRef : DatabaseReference
    var path : String? = ""
    private val REQUEST_CODE_PICK_IMAGE = 100
    private val imgPost: AppCompatImageView by lazy {
        findViewById(R.id.imgProduct)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_data)
        binding = ActivityInsertDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // create table
        dbRef = FirebaseDatabase.getInstance().getReference("product")

        // btn save

        // btn choose pic
        binding.btnChoosePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            // firebase store
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/"+selectedImageUri?.lastPathSegment.toString())
            val file = selectedImageUri.toString()
            Log.e("test",file)
            val imageRef = storageRef.child(file)
            // Xử lý ảnh được chọn ở đây, ví dụ hiển thị ảnh bằng ImageView
            binding.imgProduct.visibility = View.VISIBLE
            imgPost.setImageURI(selectedImageUri)
            binding.btnSaveData.setOnClickListener {
                val uploadTask = storageRef.putFile(selectedImageUri!!)

                val getName = binding.edtNameProduct.text.toString()
                val getType = binding.edtTypeProduct.text.toString()
                val getPrice = binding.edtPrice.text.toString()


                // check data
                var count:Int = 0
                if (getName.isEmpty()){
                    binding.edtNameProduct.error = "Nhập Tên Sản Phẩm"
                    count++
                }
                if (getType.isEmpty()){
                    binding.edtTypeProduct.error = "Nhập Loại Sản Phẩm"
                    count++
                }
                if (getPrice.isEmpty()){
                    binding.edtPrice.error = "Nhập Giá Của Sản Phẩm"
                    count++
                }

                // push data
                if (count==0){
                    uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Upload thành công, lấy đường dẫn tới file trên Firebase Storage
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        val id = dbRef.push().key!!
                        val product = ProductModel(id,getName,getType,getPrice,downloadUrl)
                        // notification
                        dbRef.child(id).setValue(product)
                            .addOnCompleteListener {
                                Toast.makeText(this,"Data inserted!",Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {err->
                                Toast.makeText(this,"Data inserted error!!! ${err.message}",Toast.LENGTH_SHORT).show()
                            }
                    }
                }.addOnFailureListener { exception ->
                    // Upload thất bại
                }


                }
                binding.edtNameProduct.setText("")
                binding.edtPrice.setText("")
                binding.edtTypeProduct.setText("")
                binding.imgProduct.visibility = View.GONE
            }


        }
    }
}