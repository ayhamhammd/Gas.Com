package com.blogspot.atifsoftwares.messegingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.blogspot.atifsoftwares.messegingapp.databinding.ActivityCustomerBinding
import com.blogspot.atifsoftwares.messegingapp.databinding.ActivitySellerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.zip.Inflater
import kotlin.concurrent.thread
import kotlin.properties.Delegates

 class sellerActivity : AppCompatActivity() {
    private lateinit var sellerID:String
    private var loop =true
    private lateinit var binding: ActivitySellerBinding
    private lateinit var db:FirebaseFirestore
    private lateinit var toggle:ActionBarDrawerToggle
    private var nav by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /*  binding=ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        */

    }

    @SuppressLint("SuspiciousIndentation")
    private fun content() {
        if(nav!=true){
    val db =Firebase.firestore

        Log.w("sellerID",sellerID)
    db.collection("orders")
        .whereEqualTo("seller",sellerID)
        .get()
        .addOnSuccessListener {
                result ->
            if ((!result.isEmpty)) {
           // Toast.makeText(this, "order detected successfully",Toast.LENGTH_LONG).show()
                goToNextActivity()
                loop=false
        }else{
                Toast.makeText(this, "No order",Toast.LENGTH_LONG).show()
                if(loop){
                    waitFor()}
            }}

    }else
        finish()




    }

    private fun goToNextActivity() {
        val intent =Intent(this,OrderAlertActivity::class.java)
        intent.putExtra("sellerID",sellerID)
        startActivity(intent)



    }

    private fun waitFor() {
        Handler(Looper.myLooper()!!).postDelayed({
            content()
        },5000)
    }

    private fun init(){
        val sharedPreferences=getSharedPreferences("shared_pref", MODE_PRIVATE)
        sellerID =sharedPreferences.getString("userID","").toString()
        nav=false
    }

    override fun onResume() {
        super.onResume()

        binding=ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        content()
        binding.apply {
            val sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
            val UserId = sharedPreferences.getString("userID","").toString()

            db = Firebase.firestore
            db.collection("users").document(UserId).get().addOnSuccessListener {result ->
                if(result !== null){
                    var user = result.data as HashMap<String, String>
                    toggle= ActionBarDrawerToggle(this@sellerActivity,Drawer,R.string.open,R.string.close)
                    Drawer.addDrawerListener(toggle)
                    toggle.syncState()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    val menuItemName =navViewSeller.menu.findItem(R.id.name_of_customer)
                    val menuItemPhoneNumber =navViewSeller.menu.findItem(R.id.phoneNumber_of_customer)
                    val menuItemBalance =navViewSeller.menu.findItem(R.id.balance)
                    menuItemName.setTitle(user["name"])
                    menuItemPhoneNumber.setTitle("Number of cylinders: ${user["numOfCylinders"]}")
                    menuItemBalance.setTitle("balance: "+"${user["balance"]}"+"JD")
                    navViewSeller.setNavigationItemSelectedListener {
                        when(it.itemId){
                            R.id.sign_out -> {
                                val intent=Intent(this@sellerActivity,messages::class.java)
                                finish()
                                nav=true
                                startActivity(intent)

                            }
                            R.id.balance ->{
                                val intent=Intent(this@sellerActivity,Mony::class.java)
                                intent.putExtra("case","seller")
                                finish()
                                nav=true
                                startActivity(intent)

                            }
                            R.id.phoneNumber_of_customer ->{
                                val intent=Intent(this@sellerActivity,gasCountActivity::class.java)
                                finish()
                                nav=true
                                startActivity(intent)


                            }
                        };true
                    }
                }
            }




        }
    }

}
