package com.example.study_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study_firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // activity insert
        binding.btnInsert.setOnClickListener {
            val intentInsertData = Intent(this,ActivityInsertData::class.java)
            startActivity(intentInsertData)
        }
        // activity show
        binding.btnShow.setOnClickListener {
            val intentShowData = Intent(this,ActivityShowData::class.java)
            startActivity(intentShowData)
        }
    }
}