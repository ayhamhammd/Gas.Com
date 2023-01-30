package com.blogspot.atifsoftwares.messegingapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates


//@Suppress("DEPRECATION")
class UserBuyingActivity : AppCompatActivity() {

    private lateinit var nameTv:TextView
    private lateinit var locationTv:TextView
    private lateinit var plusTv:TextView
    private lateinit var minusTv:TextView
    private lateinit var numberTv:TextView
    private lateinit var priceTv:TextView
    private lateinit var placeOrder:Button
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var PERMISSION_ID=52
    private lateinit var locationRequest:LocationRequest
    private var lLocation:Location?=null
    lateinit var name: String
    lateinit var custName:String
    lateinit var custLocation:String
    lateinit var location:String
    lateinit var numOfCard:String
    lateinit var id:String
    lateinit var phoneNum:String
    var n by Delegates.notNull<Boolean>()
    var mapLocation:String=""
    lateinit var onlinePay:CheckBox
    lateinit var cash:CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_buying)

        init()
        onlinePay.setOnClickListener{
            switchCheckedBox(it)
            n=true
        }
        cash.setOnClickListener{
            switchCheckedBox(it)
            n=false
        }


        plusTv.setOnClickListener{
           var num = numberTv.text.toString().toInt()
               num+=1
            val price = num*7
            numberTv.text=num.toString()
            priceTv.text="${price}JD"

        }
        minusTv.setOnClickListener{
            if (numberTv.text.toString().toInt()>0){
            var num = numberTv.text.toString().toInt()
            num-=1
            val price = num*7
            numberTv.text=num.toString()
            priceTv.text="${price}JD"}

        }

        placeOrder.setOnClickListener{
            getLastLocation()


        }
    }

    private fun putOrderToDataBase() {
        val db =Firebase.firestore
        // Create a new user with a first and last name
        val order = hashMapOf(
            "Name" to custName,
            "phoneNumber" to phoneNum,
            "location" to custLocation,
            "map_location" to mapLocation ,
            "numberOfCylenders" to numberTv.text.toString() ,
            "seller" to id,
            "online_pay" to n

        )

// Add a new document with a generated ID
        db.collection("orders")
            .add(order)
            .addOnSuccessListener { documentReference ->
               // Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this,"Order Placed Successfully",Toast.LENGTH_LONG).show()
                this@UserBuyingActivity.finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,"Error Please Try Again",Toast.LENGTH_LONG).show()
                Log.w("fail to add document", "Error adding document", e)
            }
        val sharedPreferences=getSharedPreferences("shared_pref", MODE_PRIVATE)
            val numberofgas=numberTv.text.toString()
            val userID =sharedPreferences.getString("userID",null).toString()
            db.collection("users")
                .document(userID)
                .get().addOnSuccessListener { result ->
                    if(result!==null){
                    val new = result.data as HashMap<String,String>
                    if(n==true){
                        new["balance"]="${(new["balance"]?.toDouble())!!-(numberofgas.toDouble()*7)}"
                    n=false
                    }
                    db.collection("users")
                        .document(userID)
                        .set(new).addOnSuccessListener {
                            Toast.makeText(this@UserBuyingActivity,"payment success",Toast.LENGTH_LONG).show()
                        }
                }

        }
    }
    private fun switchCheckedBox(v: View){
        when(v.id){
            R.id.gasComPay ->{
                cash.isChecked=false
            }
            R.id.cashPay ->{
                onlinePay.isChecked=false
            }
        }
    }
    private fun getLastLocation (){
        var mapLoc:String =""
        if(CheckPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{
                        task->

                    var location: Location? = task.result
                    if(location==null){

                        getNewLocation()
                        Toast.makeText(this,"${lLocation?.latitude},${lLocation?.longitude}",Toast.LENGTH_LONG).show()
                        Log.d("ayham", "${lLocation?.latitude},${lLocation?.longitude}")




                    }else{

                    Toast.makeText(this,"${location.latitude},${location.longitude}",Toast.LENGTH_LONG).show()
                        //Log.w("ayham", "${location.latitude},${location.longitude}")
                        mapLoc = location.latitude.toString() +"," + location.longitude.toString()
                        mapLocation = mapLoc
                        Log.w("ayhamLoc", mapLocation)
                        putOrderToDataBase()
                    // textView.text = "Your Current Coordinate are :\n Lat:"+location.latitude +"; Long:"+location.longitude
                    }
                }

            }else{
                Toast.makeText(this,"Please Turn On Your Location Service",Toast.LENGTH_LONG).show()
            }
        }else{
            RequestPermission()

        }


    }

    private fun getNewLocation(){
        locationRequest = LocationRequest()
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=2
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()

        )
    }

    private val locationCallback= object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                var lastLocation: Location? =p0.lastLocation
            //textView.text = "Your Current Coordinate are :\n Lat:"+lastLocation?.latitude +"; Long:"+lastLocation?.longitude
               lLocation=lastLocation

            }
        }
    private fun CheckPermission():Boolean{
        if (
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==
            PackageManager.PERMISSION_GRANTED

        )
        {return true}
        return false
    }

    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this
            , arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ),PERMISSION_ID

        )
    }

    private fun isLocationEnabled():Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("ayham", "you have the permission")

            }
        }
    }
    private fun init() {
        n=true
        custName=intent.getStringExtra("custname").toString()
        custLocation=intent.getStringExtra("custLocation").toString()
        name= intent.getStringExtra("name").toString()
        location =intent.getStringExtra("location").toString()
        numOfCard =intent.getStringExtra("numberOfCard").toString()
        id =intent.getStringExtra("sellerID").toString()
        phoneNum =intent.getStringExtra("phoneNum").toString()
        nameTv=findViewById(R.id.NameTV)
        locationTv=findViewById(R.id.LocationTV)
        plusTv=findViewById(R.id.plusTV)
        minusTv=findViewById(R.id.minusTV)
        numberTv=findViewById(R.id.numberTV)
        priceTv=findViewById(R.id.priceTV)
        placeOrder=findViewById(R.id.placeOrderBtn)
        nameTv.text=name
        locationTv.text=location
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        onlinePay=findViewById(R.id.gasComPay)
        cash = findViewById(R.id.cashPay)
        //Log.d("ayham", "${l} => ${location}")

    }
}