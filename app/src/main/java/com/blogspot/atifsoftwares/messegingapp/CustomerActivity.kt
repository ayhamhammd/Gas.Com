package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import com.blogspot.atifsoftwares.messegingapp.databinding.ActivityCustomerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class CustomerActivity : AppCompatActivity() {
    private lateinit var user_data:HashMap<String,String>
    private lateinit var userId:String
    private lateinit var db:FirebaseFirestore
    private lateinit var firstNameTV:TextView
    private lateinit var secondNameTV:TextView
    private lateinit var thirdNameTV:TextView
    private lateinit var fourthNameTV:TextView
    private lateinit var firstLocTV:TextView
    private lateinit var secondLocTV:TextView
    private lateinit var thirdLocTV:TextView
    private lateinit var fourthLocTV:TextView
    private lateinit var firstCard:CardView
    private lateinit var secondCard:CardView
    private lateinit var thirdCard:CardView
    private lateinit var fourthCard:CardView
    private lateinit var userArrayList:MutableList<Seller>
    private lateinit var BtnRfresh:Button
    private lateinit var phoneNumber: String
    private lateinit var custName:String
    private lateinit var custLocation: String
    private var card_one_num:Int?=null
    private var card_Two_num:Int?=null
    private var card_Three_num : Int? =null
    private var card_four_num  : Int? =null
    private lateinit var binding: ActivityCustomerBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*binding =ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        firstCard.visibility=View.INVISIBLE
        secondCard.visibility=View.INVISIBLE
        thirdCard.visibility=View.INVISIBLE
        fourthCard.visibility=View.INVISIBLE
        BtnRfresh.visibility=View.VISIBLE
        userArrayList= mutableListOf()
        getdata()
        BtnRfresh.setOnClickListener{
            getdata()
        }
        binding.apply {
           val sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
            val UserId = sharedPreferences.getString("userID","").toString()

            db = Firebase.firestore
            db.collection("users").document(UserId).get().addOnSuccessListener {result ->
                if(result !== null){
                   var user = result.data as HashMap<String, String>
                    toggle=ActionBarDrawerToggle(this@CustomerActivity,drawerLayout,R.string.open,R.string.close)
                    drawerLayout.addDrawerListener(toggle)
                    toggle.syncState()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    val menuItemName =navView.menu.findItem(R.id.name_of_customer)
                    val menuItemPhoneNumber =navView.menu.findItem(R.id.phoneNumber_of_customer)
                    val menuItemBalance =navView.menu.findItem(R.id.balance)
                    menuItemName.setTitle(user["name"])
                    menuItemPhoneNumber.setTitle(user["phone number"])
                    menuItemBalance.setTitle("balance: "+"${user["balance"]}"+"JD")
                    navView.setNavigationItemSelectedListener {
                        when(it.itemId){
                            R.id.sign_out -> {
                                val intent=Intent(this@CustomerActivity,messages::class.java)
                                startActivity(intent)
                            }
                        }
                        true
                    }
                }
            }



        }

        firstCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)

            val user1=userArrayList[card_one_num!!]
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            Log.d("ayham", "${user1.id} => ${user1.Name}")
            intent.putExtra("name",user1.Name)
            intent.putExtra("location",user1.location)
            intent.putExtra("sellerID",user1.id)
            intent.putExtra("numberOfCard","1")
            intent.putExtra("phoneNum",phoneNumber)
            startActivity(intent)

        }
        secondCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)
            val user2=userArrayList[card_Two_num!!]
            Log.w("ayh",card_Two_num.toString())
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            intent.putExtra("name",user2.Name)
            intent.putExtra("location",user2.location)
            intent.putExtra("sellerID",user2.id)
            intent.putExtra("numberOfCard",2)
            startActivity(intent)
        }
        thirdCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)
            val user3=userArrayList[card_Three_num!!]
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            intent.putExtra("name",user3.Name)
            intent.putExtra("location",user3.location)
            intent.putExtra("sellerID",user3.id)
            intent.putExtra("numberOfCard",3)
            startActivity(intent)
        }
        fourthCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)
            val user4=userArrayList[card_four_num!!]
            intent.putExtra("name",user4.Name)
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            intent.putExtra("location",user4.location)
            intent.putExtra("sellerID",user4.id)
            intent.putExtra("numberOfCard",4)
            startActivity(intent)


        }


*/

    }
    private fun getdata() {

     db=Firebase.firestore

    db.collection("users")
        .whereEqualTo("case","seller")
        .whereEqualTo("location",custLocation)
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                Log.d("seller",document.id)

                userId=document.id
                user_data= document.data as HashMap<String,String>

                val user=Seller(user_data.get("name").toString(),user_data.get("location").toString(),user_data.get("phone number").toString(),userId,user_data.get("case").toString(),user_data.get("numOfCylinders").toString())

                userArrayList.add(user)


            }

            var num_of_cards=1
            var num_on_array =0

            Log.d("TAGGGG",userArrayList.size.toString())
            for (users in userArrayList){
                Log.w("TAGGGG", "${users.id} = ${users.case}")

                    when (num_of_cards) {
                        1 -> {
                            firstCard.visibility=View.VISIBLE
                            firstNameTV.text=users.Name
                            firstLocTV.text=users.location
                            binding.firstCardCyl.text=users.cylinderCount
                            num_of_cards =2
                           this.card_one_num=num_on_array

                        }
                        2 -> {
                            secondCard.visibility=View.VISIBLE
                            secondNameTV.text=users.Name
                            secondLocTV.text=users.location
                            binding.secondCardCyl.text=users.cylinderCount
                            num_of_cards =3
                            this.card_Two_num=num_on_array

                        }
                        3 -> {
                            thirdCard.visibility=View.VISIBLE
                            thirdNameTV.text=users.Name
                            thirdLocTV.text=users.location
                            binding.thirdCardCyl.text=users.cylinderCount
                            num_of_cards  =4
                            this.card_Three_num=num_on_array

                        }
                        4 -> {
                            fourthCard.visibility=View.VISIBLE
                            fourthNameTV.text=users.Name
                            fourthLocTV.text=users.location
                            binding.fourthCardCyl.text=users.cylinderCount
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
    private fun init(){
        firstCard=findViewById(R.id.firstCard)
        secondCard=findViewById(R.id.secondCard)
        thirdCard=findViewById(R.id.thirdCard)
        fourthCard=findViewById(R.id.fourthCard)
        firstNameTV=findViewById(R.id.firstSellerNameTV)
        secondNameTV=findViewById(R.id.secondSellerNameTV)
        thirdNameTV=findViewById(R.id.thirdSellerNameTV)
        fourthNameTV=findViewById(R.id.fourthSellerNameTV)
        firstLocTV=findViewById(R.id.firstSellerLocationTV)
        secondLocTV=findViewById(R.id.secondSellerLocationTV)
        thirdLocTV=findViewById(R.id.thirdSellerLocationTV)
        fourthLocTV=findViewById(R.id.fourthSellerLocationTV)
        BtnRfresh=findViewById(R.id.refreshBtn)
        phoneNumber=intent.getStringExtra("phoneNum").toString()
        custName=intent.getStringExtra("name").toString()
        custLocation=intent.getStringExtra("location").toString()
        this.card_one_num = 0
        this.card_Two_num = 0
        this.card_Three_num = 0
        this.card_four_num = 0


    }

    override fun onResume() {
        super.onResume()
        binding =ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        firstCard.visibility=View.INVISIBLE
        secondCard.visibility=View.INVISIBLE
        thirdCard.visibility=View.INVISIBLE
        fourthCard.visibility=View.INVISIBLE
        BtnRfresh.visibility=View.VISIBLE
        userArrayList= mutableListOf()
        getdata()
        BtnRfresh.setOnClickListener{
            recreate()
        }
        binding.apply {
            val sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
            val UserId = sharedPreferences.getString("userID","").toString()

            db = Firebase.firestore
            db.collection("users").document(UserId).get().addOnSuccessListener {result ->
                if(result !== null){
                    var user = result.data as HashMap<String, String>
                    toggle=ActionBarDrawerToggle(this@CustomerActivity,drawerLayout,R.string.open,R.string.close)
                    drawerLayout.addDrawerListener(toggle)
                    toggle.syncState()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    val menuItemName =navView.menu.findItem(R.id.name_of_customer)
                    val menuItemPhoneNumber =navView.menu.findItem(R.id.phoneNumber_of_customer)
                    val menuItemBalance =navView.menu.findItem(R.id.balance)
                    menuItemName.setTitle(user["name"])
                    menuItemPhoneNumber.setTitle(user["phone number"])
                    menuItemBalance.setTitle("balance: "+"${user["balance"]}"+"JD")
                    navView.setNavigationItemSelectedListener {
                        when(it.itemId){
                            R.id.sign_out -> {
                                val intent=Intent(this@CustomerActivity,messages::class.java)
                                startActivity(intent)
                            }
                            R.id.balance ->
                            {
                                val intent=Intent(this@CustomerActivity,Mony::class.java)
                                intent.putExtra("case","customer")
                                startActivity(intent)
                            }
                        }
                        true
                    }
                }
            }



        }

        firstCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)

            val user1=userArrayList[card_one_num!!]
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            Log.d("ayham", "${user1.id} => ${user1.Name}")
            intent.putExtra("name",user1.Name)
            intent.putExtra("location",user1.location)
            intent.putExtra("sellerID",user1.id)
            intent.putExtra("numberOfCard","1")
            intent.putExtra("phoneNum",phoneNumber)
            startActivity(intent)

        }
        secondCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)
            val user2=userArrayList[card_Two_num!!]
            Log.w("ayh",card_Two_num.toString())
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            intent.putExtra("name",user2.Name)
            intent.putExtra("location",user2.location)
            intent.putExtra("sellerID",user2.id)
            intent.putExtra("numberOfCard",2)
            startActivity(intent)
        }
        thirdCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)
            val user3=userArrayList[card_Three_num!!]
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            intent.putExtra("name",user3.Name)
            intent.putExtra("location",user3.location)
            intent.putExtra("sellerID",user3.id)
            intent.putExtra("numberOfCard",3)
            startActivity(intent)
        }
        fourthCard.setOnClickListener{
            val intent=Intent(this,UserBuyingActivity::class.java)
            val user4=userArrayList[card_four_num!!]
            intent.putExtra("name",user4.Name)
            intent.putExtra("custname",custName)
            intent.putExtra("custLocation",custLocation)
            intent.putExtra("location",user4.location)
            intent.putExtra("sellerID",user4.id)
            intent.putExtra("numberOfCard",4)
            startActivity(intent)


        }




    }
}
