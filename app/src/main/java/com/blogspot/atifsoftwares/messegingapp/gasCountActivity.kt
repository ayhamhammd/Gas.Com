package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.blogspot.atifsoftwares.messegingapp.databinding.ActivityGasCountBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class gasCountActivity : AppCompatActivity() {
        private lateinit var db:FirebaseFirestore
        private lateinit var binding: ActivityGasCountBinding
        private lateinit var number:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGasCountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        number=findViewById(R.id.num)


    binding.OK.setOnClickListener{

        val sharedPreferences= getSharedPreferences("shared_pref", MODE_PRIVATE)
        val userID =sharedPreferences.getString("userID","").toString()
        db=Firebase.firestore
        db.collection("users")
            .document(userID)
            .get().addOnSuccessListener { result ->
                if(result!= null) {
                    var new = hashMapOf<String, String>()
                    new=result.data as HashMap<String, String>
                    new["numOfCylinders"]=number.text.toString()
                    db.collection("users").document(userID).set(new).addOnSuccessListener {
                        Toast.makeText(this@gasCountActivity,"number edited successfully",Toast.LENGTH_SHORT).show()
                        val intent =Intent(this@gasCountActivity,sellerActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }


    }
    }
}