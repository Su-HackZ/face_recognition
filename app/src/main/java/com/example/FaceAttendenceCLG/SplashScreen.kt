package com.example.FaceAttendenceCLG

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class SplashScreen : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth= FirebaseAuth.getInstance()




        Handler().postDelayed({
            if (firebaseAuth.currentUser != null){
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()

            }
            else{ startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
                finish()
            }
        },500

        )



    }




    override fun onBackPressed() {
        finishAffinity()
    }


}