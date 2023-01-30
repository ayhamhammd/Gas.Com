package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.blogspot.atifsoftwares.messegingapp.databinding.ActivityMonyBinding

class Mony : AppCompatActivity() {

    private lateinit var binding: ActivityMonyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val case = intent.getStringExtra("case").toString()
        Toast.makeText(this,"payment verified it can some time!",Toast.LENGTH_SHORT).show()
        binding.MonyBtn.setOnClickListener{
            when(case){
                "seller" ->{
                    val intent=Intent(this@Mony,sellerActivity::class.java)
                    startActivity(intent)
                }
                "customer" ->{
                    val intent=Intent(this@Mony,CustomerActivity::class.java)
                    startActivity(intent)
                }
            }

        }

    }
}