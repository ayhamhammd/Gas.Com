package com.blogspot.atifsoftwares.messegingapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderHandelActivity : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    private lateinit var orderID:String
    private lateinit var data:HashMap<String,String>
    private lateinit var orderIdTV:TextView
    private lateinit var OrderName:TextView
    private lateinit var cylindersCount:TextView
    private lateinit var button: Button
    private lateinit var call:ImageView
    private lateinit var mapsIV:ImageView
    private var REQUEST_PHONE_CALL=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_handel)
        OrderName=findViewById(R.id.orderName123)
        cylindersCount=findViewById(R.id.CylinderNum)
        Init()
        getData()
        assignData()

        call.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            }else{



            startcall()}
        }
        mapsIV.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=${data.get("map_location")}&mode=d")
            )
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
        button.setOnClickListener{ db.collection("orders")
            .document(orderID)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Order Removed From Data Base",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
              Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
            }

            val sharedPreferences=getSharedPreferences("shared_pref", MODE_PRIVATE)
           // val numberofgas=numberTv.text.toString()
            val userID =sharedPreferences.getString("userID",null).toString()
            db.collection("users")
                .document(userID)
                .get().addOnSuccessListener { result ->
                    if(result!==null){
                        val new = result.data as HashMap<String,String>
                        new["balance"]="${(new["balance"]?.toDouble())!!+((cylindersCount.text.toString()).toDouble()*7)}"
                        new["numOfCylinders"]="${(new["numOfCylinders"]?.toDouble())!!-(cylindersCount.text.toString()).toDouble()}"
                        db.collection("users")
                            .document(userID)
                            .set(new).addOnSuccessListener {
                                Toast.makeText(this@OrderHandelActivity,"payment success",Toast.LENGTH_LONG).show()
                                val intent = Intent(this@OrderHandelActivity,sellerActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    }

                }


        }


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUEST_PHONE_CALL)startcall()
    }
    private fun startcall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.setData(Uri.parse("tel:"+data.get("phoneNumber")))
        startActivity(callIntent)
    }

    private fun assignData() {
        orderIdTV.text=orderID
        //OrderName.text=data.get("Name")
        //cylindersCount.text=data.get("numberOfCylenders")



    }

    private fun Init() {
        orderID=intent.getStringExtra("orderID").toString()
        //orderInfo= Orders(null,null,null,null)
        orderIdTV=findViewById(R.id.orderID11)
        data= hashMapOf()
        button=findViewById(R.id.DeliveredBtn)
        call=findViewById(R.id.call)
        mapsIV=findViewById(R.id.gotoMap)
        db=Firebase.firestore
    }

    private fun getData() {
       // db=Firebase.firestore
        db.collection("orders")
            .document(orderID)
            .get()
            .addOnSuccessListener {
                 document ->
                if (document != null) {
                    data=document.data as HashMap<String,String>
                    Log.d("myHash",data.toString())
                    OrderName.text=data.get("Name").toString()
                    cylindersCount.text=data.get("numberOfCylenders").toString()
                    //orderInfo.Name=data["Name"]
                    //orderInfo.Map_Location=data["map_location"]
                    //orderInfo.Num_Of_Gas_Cylinders=data["numberOfCylenders"]
                    //orderInfo.Phone_num=data["phoneNumber"]
                } else {
                    Log.d("ayham", "No such document")
                }
            }
                .addOnFailureListener { exception ->
                    Log.d("ayham", "get failed with ", exception)
                }


            }

    }
