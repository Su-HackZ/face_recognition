package com.example.FaceAttendenceCLG

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.FaceAttendenceCLG.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

      lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()


        binding.signin.setOnClickListener {
            loginUser()
        }
        binding.sigunup.setOnClickListener {

            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.forgetpassword.setOnClickListener {
            showDialog()
        }


        binding.collage.setOnClickListener {

            val intent = Intent(this@LoginActivity, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    fun loginUser(){
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (email.isBlank()){
            Toast.makeText(this,"Email can't be Blank", Toast.LENGTH_SHORT).show()
            return }
        if (password.isBlank()){
            Toast.makeText(this,"Password can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"server Error try later", Toast.LENGTH_SHORT).show()

                }
            }
    }







    private fun showDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(com.example.FaceAttendenceCLG.R.layout.forgetpassworddialog)
        val forgetemail = dialog.findViewById(com.example.FaceAttendenceCLG.R.id.forgetemail) as EditText
        val cancel = dialog.findViewById(com.example.FaceAttendenceCLG.R.id.cancel) as TextView
        val reset = dialog.findViewById(com.example.FaceAttendenceCLG.R.id.reset) as TextView






        cancel.setOnClickListener {
            dialog.dismiss()

        }


        reset.setOnClickListener {

            val email2 = forgetemail.text.toString()


            if (email2.isBlank()){
                Toast.makeText(this,"Email can't be Blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth!!.sendPasswordResetEmail(email2)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Link is sent to your Email", Toast.LENGTH_SHORT).show()
                    } else {
                        // failed!
                    }
                }



            dialog.dismiss()
        }






        dialog.show()

    }





}