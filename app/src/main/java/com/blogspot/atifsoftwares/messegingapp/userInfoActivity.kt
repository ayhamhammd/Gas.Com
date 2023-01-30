package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.blogspot.atifsoftwares.messegingapp.databinding.ActivityUserInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap


class userInfoActivity : AppCompatActivity() {
    private lateinit var nameET:EditText
    private lateinit var locationET:EditText
    private lateinit var customer:CheckBox
    private lateinit var seller:CheckBox
    private lateinit var contBTN:Button
    private lateinit var userID:String
    private lateinit var db:FirebaseFirestore
    private lateinit var phoneNumber:String
    private lateinit var case:String
    private lateinit var cylinderNumber:EditText
    private lateinit var binding:ActivityUserInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.linearLayout.visibility=View.INVISIBLE
        init()
        customer.setOnClickListener { v -> switchCheckedBox(v)
        case="customer"
            binding.linearLayout.visibility=View.INVISIBLE
        }
        seller.setOnClickListener { v -> switchCheckedBox(v)
        case="seller"
        binding.linearLayout.visibility = View.VISIBLE
        }

        contBTN.setOnClickListener{
            val name : String=nameET.text.toString()
            val location :String=locationET.text.toString()
            val numOfCylinders:String = cylinderNumber.text.toString()
            if(name.isNotEmpty()){
                if(location.isNotEmpty()){
                if((seller.isChecked) or (customer.isChecked)){

                  val user =HashMap<String,String>()
                    user.put("name",name)
                    user.put("location",location)
                    user.put("phone number",phoneNumber)
                    user.put("case",case)
                    user.put("balance","100")
                    if(case=="seller"){
                    user.put("numOfCylinders",numOfCylinders)}
                    db.collection("users")
                        .document(userID).set(user).addOnSuccessListener {

                            Toast.makeText(this,"user Info Added Successfully",Toast.LENGTH_LONG).show()
                        }
                    if(case=="customer"){
                        val intent=Intent(this,CustomerActivity::class.java)
                        intent.putExtra("userID",userID)
                        intent.putExtra("name",name)
                        intent.putExtra("phoneNum",phoneNumber)
                        intent.putExtra("location",location)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent2 =Intent(this,sellerActivity::class.java)
                        intent2.putExtra("userID",userID)
                        intent2.putExtra("name",name)
                        startActivity(intent2)
                        finish()
                    }

                }else{
                    Toast.makeText(this,"please Select One of The Options",Toast.LENGTH_LONG).show()


                }}else{
                    Toast.makeText(this,"Please Enter Your Location",Toast.LENGTH_LONG).show()
                    locationET.requestFocus()
                }
            }else{
                Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_LONG).show()
                nameET.requestFocus()
            }
        }
    }
    private fun switchCheckedBox(v : View) {
        when (v.id) {
            R.id.customerCB -> seller.isChecked = false
            R.id.sellerCB -> customer.isChecked = false
        }
    }

    private fun init() {
        val sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
        val editor =sharedPreferences.edit()
        nameET=findViewById(R.id.editName)
        locationET =findViewById(R.id.editLocation)
        customer=findViewById(R.id.customerCB)
        seller=findViewById(R.id.sellerCB)
        contBTN=findViewById(R.id.continueBtn2)
        cylinderNumber =findViewById(R.id.numberForSeller)
        db=FirebaseFirestore.getInstance()
        userID=intent.getStringExtra("userid")!!
        phoneNumber=intent.getStringExtra("phoneNum")!!

            editor.putString("userID",userID)
            editor.apply()
    }
}