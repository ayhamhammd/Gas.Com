package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class OrderAlertActivity : AppCompatActivity() {
    private lateinit var tv:TextView
    private var counter:Int=30
    private lateinit var button: Button
    private lateinit var sellerID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_alert)
        init()
        val mediaPlayer=MediaPlayer.create(this,R.raw.videoplayback)

            mediaPlayer.start()
                contents()
        button.setOnClickListener{
            val intent=Intent(this,OrdersActivity::class.java)
            intent.putExtra("sellerID",sellerID)
            startActivity(intent)
            mediaPlayer.stop()
        }

    }



    private fun contents() {

       if(counter >0 ) {counter--}

        tv.text=counter.toString()
        waitFor()

    }



    private fun waitFor() {
        Handler(Looper.myLooper()!!).postDelayed({
            contents()
        },1000)
    }

    private fun init() {
        tv=findViewById(R.id.counter)
        button=findViewById(R.id.continueButton)
        sellerID=intent.getStringExtra("sellerID").toString()

    }
}