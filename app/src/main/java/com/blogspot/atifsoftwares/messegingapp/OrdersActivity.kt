package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class OrdersActivity : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    private lateinit var userId:String
    private lateinit var userArrayList:MutableList<String>
    private lateinit var firstNameTV: TextView
    private lateinit var secondNameTV: TextView
    private lateinit var thirdNameTV: TextView
    private lateinit var fourthNameTV: TextView
    private lateinit var firstCard:CardView
    private lateinit var secondCard:CardView
    private lateinit var thirdCard:CardView
    private lateinit var fourthCard:CardView
    private lateinit var button:Button
    private var card_one_num:Int?=null
    private var card_Two_num:Int?=null
    private var card_Three_num : Int? =null
    private var card_four_num  : Int? =null
    private lateinit var sellerID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)



        Init()
        userArrayList= mutableListOf()
        firstCard.visibility=View.INVISIBLE
        secondCard.visibility=View.INVISIBLE
        thirdCard.visibility=View.INVISIBLE
        fourthCard.visibility=View.INVISIBLE

        getdata()
        button.setOnClickListener{

            recreate()
        }

        firstCard.setOnClickListener{
            val intent=Intent(this,OrderHandelActivity::class.java)
            intent.putExtra("orderID",userArrayList[card_one_num!!])
            startActivity(intent)
            finish()
        }
        secondCard.setOnClickListener{
            val intent=Intent(this,OrderHandelActivity::class.java)
            intent.putExtra("orderID",userArrayList[card_Two_num!!])
            startActivity(intent)
            finish()
        }
        thirdCard.setOnClickListener{
            val intent=Intent(this,OrderHandelActivity::class.java)
            intent.putExtra("orderID",userArrayList[card_Three_num!!])
            startActivity(intent)
            finish()
        }
        fourthCard.setOnClickListener{
            val intent=Intent(this,OrderHandelActivity::class.java)
            intent.putExtra("orderID",userArrayList[card_four_num!!])
            startActivity(intent)
            finish()
        }

    }



    private fun getdata() {

        db= Firebase.firestore

        db.collection("orders")
            .whereEqualTo("seller",sellerID)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    userId=document.id
                    userArrayList.add(userId)
                }

                var num_of_cards=1
                var num_on_array =0

                for (users in userArrayList){
                    when (num_of_cards) {
                        1 -> {
                            firstCard.visibility= View.VISIBLE
                            firstNameTV.text=users
                            num_of_cards =2
                            this.card_one_num=num_on_array

                        }
                        2 -> {
                            secondCard.visibility= View.VISIBLE
                            secondNameTV.text=users
                            num_of_cards =3
                            this.card_Two_num=num_on_array

                        }
                        3 -> {
                            thirdCard.visibility= View.VISIBLE
                            thirdNameTV.text=users
                            num_of_cards  =4
                            this.card_Three_num=num_on_array

                        }
                        4 -> {
                            fourthCard.visibility= View.VISIBLE
                            fourthNameTV.text=users
                            num_of_cards=5
                            this.card_four_num=num_on_array

                        }
                        5 -> {
                            break
                        }
                    }
                    num_on_array++  }

            }



            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }

    }
    private fun Init() {
        firstCard=findViewById(R.id.firstCardOrder)
        secondCard=findViewById(R.id.secondCardOrder)
        thirdCard=findViewById(R.id.thirdCardOrder)
        fourthCard=findViewById(R.id.fourthCardOrder)
        firstNameTV=findViewById(R.id.firstCustomerNameTV)
        secondNameTV=findViewById(R.id.secondCustomerNameTV)
        thirdNameTV=findViewById(R.id.thirdCustomerNameTV)
        fourthNameTV=findViewById(R.id.fourthCustomerNameTV)
        button=findViewById(R.id.refreshBtnSeller)
        sellerID=intent.getStringExtra("sellerID").toString()
        this.card_one_num = 0
        this.card_Two_num = 0
        this.card_Three_num = 0
        this.card_four_num = 0
    }
}