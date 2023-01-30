package com.blogspot.atifsoftwares.messegingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var Btnverify:Button
    private var showNumberEditText: TextView?=null
    private lateinit var resend:TextView
    private lateinit var otpCode:EditText
    private lateinit var auth:FirebaseAuth
    private lateinit var OTP:String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private lateinit var userID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)
        OTP=intent.getStringExtra("OTP").toString()
        resendToken=intent.getParcelableExtra("resendToken")!!
        phoneNumber=intent.getStringExtra("phoneNumber")!!

        init()
        val s ="verify" +phoneNumber
        showNumberEditText?.text=s


        resendOTPTvVisibility()
        Btnverify.setOnClickListener{
            val typedOTP = otpCode.text.toString()
            if((typedOTP.length==6) and(typedOTP.isNotEmpty())){
                val credential:PhoneAuthCredential=PhoneAuthProvider.getCredential(OTP,typedOTP)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Please Enter A VALID code",Toast.LENGTH_LONG).show()
            }
        }
        resend.setOnClickListener{
            resendVerificationCode()
            resendOTPTvVisibility()
        }


    }
    private fun resendOTPTvVisibility(){
        otpCode.setText("")
        resend.visibility=View.INVISIBLE
        resend.isEnabled=false
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resend.visibility =View.VISIBLE
            resend.isEnabled=true
        },30000)
    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.

            signInWithPhoneAuthCredential(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG","OnVerificationFailed:${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG","OnVerificationFailed:${e.toString()}")
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) { OTP=verificationId
            resendToken=token
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.


            // Save verification ID and resending token so we can use them later

        }
    }
    private fun resendVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)
            // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Authenticate Successfully",Toast.LENGTH_LONG).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG","signInWithPhoneAuthCredential: ${task.exception.toString()}")

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid

                    }
                    // Update UI
                }
            }
    }
    private fun sendToMain(){
        userID= auth.currentUser?.uid!!
        var intent=Intent(this,userInfoActivity::class.java)
        intent.putExtra("userid",userID)
        intent.putExtra("phoneNum",phoneNumber)
        startActivity(intent)
    }
    private fun init(){
        Btnverify=findViewById(R.id.verifyBtn)
        resend=findViewById(R.id.resendTV)
        otpCode=findViewById(R.id.OTPet)
        auth=FirebaseAuth.getInstance()
        showNumberEditText=findViewById(R.id.numShowEt)
    }
}
