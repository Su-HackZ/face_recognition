package com.example.FaceAttendenceCLG

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.FaceAttendenceCLG.databinding.ActivityAddSubjectBinding.inflate
import com.example.FaceAttendenceCLG.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signin.setOnClickListener {
            adminlogin()
        }

        binding.collage.setOnClickListener {
            val intent = Intent(this@AdminActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    fun adminlogin(){

        var hashMap : HashMap<String, String>
                = HashMap<String, String> ()
        hashMap.put("mba" , "123456")
        hashMap.put("mca" , "123456")
        hashMap.put("be" , "123456")
        hashMap.put("bca" , "123456")

        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (email.isBlank()){
            Toast.makeText(this,"Email can't be Blank", Toast.LENGTH_SHORT).show()
            return }
        if (password.isBlank()){
            Toast.makeText(this,"Password can't be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (!hashMap.containsKey(email) || hashMap.get(password) == password) {
            Toast.makeText(this@AdminActivity, "Please enter valid credentials", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val intent = Intent(this@AdminActivity, facilitieActivity::class.java)
        startActivity(intent)
        finish()

    }


}