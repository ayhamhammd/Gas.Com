package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class messages : AppCompatActivity() {
    private lateinit var singOutBtn:Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        auth=FirebaseAuth.getInstance()
        singOutBtn=findViewById(R.id.signOutBtn)
        singOutBtn.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}