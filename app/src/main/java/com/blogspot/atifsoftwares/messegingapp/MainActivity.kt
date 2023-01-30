package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var start: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start =findViewById(R.id.startBtn)
        start.setOnClickListener{
        val intent=Intent(this,varificationActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}